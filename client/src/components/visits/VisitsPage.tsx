import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';
import { IEditableVisit, IError } from '../../types';
import { Owner, OwnerApi, VisitApi, VisitFields } from 'petclinic-api';

import { url } from '../../util';
import { NotEmpty } from '../form/Constraints';

import DateInput from '../form/DateInput';
import Input from '../form/Input';
import PetDetails from './PetDetails';

import moment from 'moment';

interface IVisitsPageProps extends RouteComponentProps {}

interface IVisitsPageState {
  visit?: IEditableVisit;
  owner?: Owner;
  error?: IError;
}

class VisitsPage extends React.Component<IVisitsPageProps, IVisitsPageState> {
  constructor(props) {
    super(props);

    this.onInputChange = this.onInputChange.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  componentDidMount() {
    const ownerId = Number(this.props.match.params['ownerId']);

    new OwnerApi().getOwner({ ownerId }).then(owner =>
      this.setState({
        owner,
        visit: { date: '', description: '' },
      })
    );
  }

  onSubmit(event) {
    event.preventDefault();

    const petId = Number(this.props.match.params['petId']);
    const { owner, visit } = this.state;

    if (!owner) {
      throw new Error('Invalid state: no owner');
    }
    if (!visit) {
      throw new Error('Invalid state: no visit');
    }

    const request: VisitFields = {
      date: moment(visit.date, 'YYYY-MM-DD').toDate(),
      description: visit.description,
    };

    new VisitApi()
      .addVisit({ ownerId: owner.id, petId, visitFields: request })
      .then(() => {
        this.props.history.push({
          pathname: '/owners/' + owner.id,
        });
      })
      .catch(response => {
        console.log('ERROR?!...', response);
        this.setState({ error: response });
      });
  }

  onInputChange(name: string, value: string) {
    const { visit } = this.state;

    this.setState({ visit: Object.assign({}, visit, { [name]: value }) });
  }

  render() {
    if (!this.state) {
      return <h2>Loading...</h2>;
    }

    const { owner, error, visit } = this.state;
    if (!owner) {
      return <h2>Loading...</h2>;
    }

    const petId = Number(this.props.match.params['petId']);

    const pet = owner.pets.find(candidate => candidate.id === petId);
    if (!pet) {
      return <h2>Loading...</h2>;
    }

    return (
      <div>
        <h2>Visits</h2>
        <b>Pet</b>
        <PetDetails owner={owner} pet={pet} />

        <form
          className="form-horizontal"
          method="POST"
          action={url('/api/owner')}
        >
          <div className="form-group has-feedback">
            <DateInput
              object={visit}
              error={error}
              label="Date"
              name="date"
              onChange={this.onInputChange}
            />
            <Input
              object={visit}
              error={error}
              constraint={NotEmpty}
              label="Description"
              name="description"
              onChange={this.onInputChange}
            />
          </div>
          <div className="form-group">
            <div className="col-sm-offset-2 col-sm-10">
              <button
                className="btn btn-default"
                type="submit"
                onClick={this.onSubmit}
              >
                Add Visit
              </button>
            </div>
          </div>
        </form>
      </div>
    );
  }
}

export default withRouter(VisitsPage);
