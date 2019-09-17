import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';
import {
  Owner,
  OwnerApi,
  RestError,
  VisitApi,
  VisitFields,
} from 'petclinic-api';

import { Form, Formik } from 'formik';
import * as Yup from 'yup';

import DateInput from '../form/DateInput';
import Input from '../form/Input';
import PetDetails from './PetDetails';

interface IVisitsPageProps extends RouteComponentProps {}

interface IVisitsPageState {
  owner: Owner;
  error?: RestError;
}

class VisitsPage extends React.Component<IVisitsPageProps, IVisitsPageState> {
  constructor(props) {
    super(props);

    this.onSubmit = this.onSubmit.bind(this);
  }

  async componentDidMount() {
    const ownerId = Number(this.props.match.params['ownerId']);

    const owner = await new OwnerApi().getOwner({ ownerId });

    this.setState({ owner });
  }

  async onSubmit(values, { setSubmitting }) {
    const petId = Number(this.props.match.params['petId']);
    const { owner } = this.state;

    try {
      await this.saveVisit(owner.id, petId, values);
    } finally {
      setSubmitting(false);
    }
  }

  saveVisit = async (
    ownerId: number,
    petId: number,
    visitFields: VisitFields
  ) => {
    try {
      await new VisitApi().addVisit({ ownerId, petId, visitFields });
    } catch (response) {
      console.log('ERROR?!...', response);
      this.setState({ error: response });
    }
    this.props.history.push({
      pathname: '/owners/' + ownerId,
    });
  };

  render() {
    if (!this.state) {
      return <h2>Loading...</h2>;
    }

    const { owner } = this.state;
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

        <Formik
          initialValues={{ date: undefined, description: '' }}
          validationSchema={Yup.object().shape({
            date: Yup.date()
              .nullable()
              .required('Required'),
            description: Yup.string()
              .max(255, 'Must be at most 255 characters')
              .required('Required'),
          })}
          onSubmit={this.onSubmit}
        >
          {({ isSubmitting }) => (
            <Form className="form-horizontal">
              <div className="form-group has-feedback">
                <DateInput name="date" label="Date" />
                <Input name="description" label="Description" />
              </div>
              <div className="form-group">
                <div className="col-sm-offset-2 col-sm-10">
                  <button
                    className="btn btn-default"
                    type="submit"
                    disabled={isSubmitting}
                  >
                    Add Visit
                  </button>
                </div>
              </div>
            </Form>
          )}
        </Formik>
      </div>
    );
  }
}

export default withRouter(VisitsPage);
