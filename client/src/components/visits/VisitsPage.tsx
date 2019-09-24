import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';
import { Owner, VisitFields } from 'petclinic-api';
import { withOwnerApi, WithOwnerApiProps } from '../../data/OwnerApiProvider';
import { withVisitApi, WithVisitApiProps } from '../../data/VisitApiProvider';

import { Form, Formik } from 'formik';
import * as Yup from 'yup';

import DateInput from '../form/DateInput';
import Input from '../form/Input';
import PetDetails from './PetDetails';

import PageErrorMessage from '../PageErrorMessage';
import { IError } from '../../types';
import extractError from '../../data/extractError';

import Loading from '../Loading';

interface IVisitsPageProps
  extends RouteComponentProps,
    WithOwnerApiProps,
    WithVisitApiProps {}

interface IVisitsPageState {
  error?: IError;
  owner?: Owner;
}

class VisitsPage extends React.Component<IVisitsPageProps, IVisitsPageState> {
  async componentDidMount() {
    const ownerId = Number(this.props.match.params['ownerId']);

    try {
      const owner = await this.props.ownerApi.getOwner({ ownerId });
      this.setState({ owner });
    } catch (response) {
      const error = await extractError(response);
      this.setState({ error });
    }
  }

  onSubmit = async (values, { setSubmitting }) => {
    const petId = Number(this.props.match.params['petId']);
    const { owner } = this.state;
    if (!owner) {
      throw new Error('Invalid state: owner undefined');
    }

    try {
      await this.saveVisit(owner.id, petId, values);
    } finally {
      setSubmitting(false);
    }
  };

  saveVisit = async (
    ownerId: number,
    petId: number,
    visitFields: VisitFields
  ) => {
    try {
      await this.props.visitApi.addVisit({ ownerId, petId, visitFields });
    } catch (response) {
      const error = await extractError(response);
      const { owner } = this.state || {};
      this.setState({ error, owner });
    }
    this.props.history.push({
      pathname: '/owners/' + ownerId,
    });
  };

  render() {
    const { error, owner } = this.state || {};

    if (!owner) {
      if (error) {
        return <PageErrorMessage error={error} />;
      }
      return <Loading />;
    }

    const petId = Number(this.props.match.params['petId']);

    const pet = owner.pets.find(candidate => candidate.id === petId);
    if (!pet) {
      return <PageErrorMessage error={{ message: 'Pet not found' }} />;
    }

    return (
      <div>
        <PageErrorMessage error={error} />

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

export default withOwnerApi(withVisitApi(withRouter(VisitsPage)));
