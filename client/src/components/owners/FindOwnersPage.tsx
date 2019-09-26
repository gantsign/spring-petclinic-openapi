import * as React from 'react';

import { Link, RouteComponentProps, withRouter } from 'react-router-dom';
import { Owner } from 'petclinic-api';
import { withOwnerApi, WithOwnerApiProps } from '../../data/OwnerApiProvider';

import OwnersTable from './OwnersTable';

import * as H from 'history';

import QueryString from 'query-string';

import { ErrorMessage, Field, Form, Formik } from 'formik';

import { IError } from '../../types';
import PageErrorMessage from '../PageErrorMessage';
import extractError from '../../data/extractError';

interface IFindOwnersPageProps extends RouteComponentProps, WithOwnerApiProps {}

interface IFindOwnersPageState {
  error?: IError;
  owners?: Owner[];
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
  async componentDidMount() {
    const filter = getFilterFromLocation(this.props.location);
    if (typeof filter === 'string') {
      // only load data on mount (initially) if filter is specified
      // i.e. lastName query param in URI was set
      this.fetchData(filter);
    }
  }

  /**
   * Invoked when the submit button was pressed.
   *
   * This method updates the URL with the entered lastName. The change of the URL
   * leads to new properties and thus results in rendering.
   */
  submitSearchForm = async values => {
    const { filter } = values;

    this.props.history.push({
      pathname: '/owners/list',
      search: `?lastName=${encodeURIComponent(filter || '')}`,
    });

    return this.fetchData(filter);
  };

  /**
   * Actually loads data from the server
   */
  async fetchData(filter: string) {
    try {
      const owners = await this.props.ownerApi.listOwners({
        lastName: filter || '',
      });

      this.setState({ owners });
    } catch (response) {
      const error = await extractError(response);
      const { owners } = this.state || {};
      this.setState({ error, owners });
    }
  }

  render() {
    const filter = getFilterFromLocation(this.props.location) || '';
    const { error, owners = [] } = this.state || {};

    const showResults: boolean = filter.length > 0 || owners.length > 0;

    return (
      <div>
        <PageErrorMessage error={error} />

        <section>
          <h2>Find Owners</h2>
          <Formik initialValues={{ filter }} onSubmit={this.submitSearchForm}>
            {() => (
              <Form className="form-horizontal">
                <div className="form-group">
                  <div className="control-group" id="lastName">
                    <label className="col-sm-2 control-label">Last name </label>
                    <div className="col-sm-10">
                      <Field
                        name="filter"
                        className="form-control"
                        size={30}
                        maxLength={80}
                      />
                      <ErrorMessage name="filter" component="div" />
                    </div>
                  </div>
                </div>
                <div className="form-group">
                  <div className="col-sm-offset-2 col-sm-10">
                    <button type="submit" className="btn btn-default">
                      Find Owner
                    </button>
                  </div>
                </div>
              </Form>
            )}
          </Formik>
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

export default withOwnerApi(withRouter(FindOwnersPage));
