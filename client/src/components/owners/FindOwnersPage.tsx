import * as React from 'react';

import { Link, RouteComponentProps, withRouter } from 'react-router-dom';
import { Owner, OwnerApi } from 'petclinic-api';

import OwnersTable from './OwnersTable';

import * as H from 'history';

import QueryString from 'query-string';

interface IFindOwnersPageProps extends RouteComponentProps {}

interface IFindOwnersPageState {
  owners?: Owner[];
  filter?: string;
}

const getFilterFromLocation = (location: H.Location): string | undefined => {
  const query = QueryString.parse(location.search);
  const lastName = query['lastName'];
  if (Array.isArray(lastName)) {
    return lastName[0];
  }
  if (lastName === null) {
    return undefined;
  }
  return lastName;
};

class FindOwnersPage extends React.Component<
  IFindOwnersPageProps,
  IFindOwnersPageState
> {
  constructor(props) {
    super(props);
    this.onFilterChange = this.onFilterChange.bind(this);
    this.submitSearchForm = this.submitSearchForm.bind(this);

    this.state = {
      filter: getFilterFromLocation(props.location),
    };
  }

  componentDidMount() {
    const { filter } = this.state;
    if (typeof filter === 'string') {
      // only load data on mount (initially) if filter is specified
      // i.e. lastName query param in URI was set
      this.fetchData(filter);
    }
  }

  componentWillReceiveProps(nextProps: IFindOwnersPageProps) {
    const { location } = nextProps;

    // read the filter from URI
    const filter = getFilterFromLocation(location);

    // set state
    this.setState({ filter });

    // load data according to filter
    this.fetchData(filter || '');
  }

  onFilterChange(event) {
    this.setState({
      filter: event.target.value as string,
    });
  }

  /**
   * Invoked when the submit button was pressed.
   *
   * This method updates the URL with the entered lastName. The change of the URL
   * leads to new properties and thus results in rendering.
   */
  submitSearchForm() {
    const { filter } = this.state;

    this.props.history.push({
      pathname: '/owners/list',
      search: `?lastName=${encodeURIComponent(filter || '')}`,
    });
  }

  /**
   * Actually loads data from the server
   */
  fetchData(filter: string) {
    new OwnerApi().listOwners({ lastName: filter || '' }).then(owners => {
      this.setState({ owners });
    });
  }

  render() {
    const filter = this.state.filter || '';
    const owners = this.state.owners || [];

    const showResults: boolean = filter.length > 0 || owners.length > 0;

    return (
      <div>
        <section>
          <h2>Find Owners</h2>

          <form
            className="form-horizontal"
            onSubmit={event => {
              event.preventDefault();
              return false;
            }}
          >
            <div className="form-group">
              <div className="control-group" id="lastName">
                <label className="col-sm-2 control-label">Last name </label>
                <div className="col-sm-10">
                  <input
                    className="form-control"
                    name="filter"
                    value={filter}
                    onChange={this.onFilterChange}
                    size={30}
                    maxLength={80}
                  />
                  {/* <span className='help-inline'><form:errors path='*'/></span> TODO */}
                </div>
              </div>
            </div>
            <div className="form-group">
              <div className="col-sm-offset-2 col-sm-10">
                <button
                  type="button"
                  onClick={this.submitSearchForm}
                  className="btn btn-default"
                >
                  Find Owner
                </button>
              </div>
            </div>
          </form>
        </section>
        {showResults && <OwnersTable owners={owners} />}
        <Link className="btn btn-default" to="/owners/new">
          {' '}
          Add Owner
        </Link>
      </div>
    );
  }
}

export default withRouter(FindOwnersPage);
