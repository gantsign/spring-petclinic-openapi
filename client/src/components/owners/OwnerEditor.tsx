import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';

import { Form, Formik } from 'formik';
import * as Yup from 'yup';

import Input from '../form/Input';

import { Owner, OwnerApi, OwnerFields, RestError } from 'petclinic-api';

interface IOwnerEditorProps extends RouteComponentProps {
  initialOwner: Owner | OwnerFields;
}

interface IOwnerEditorState {
  error?: RestError;
}

class OwnerEditor extends React.Component<
  IOwnerEditorProps,
  IOwnerEditorState
> {
  constructor(props) {
    super(props);

    this.state = {};
  }

  onSubmit = async (values, { setSubmitting }) => {
    const { initialOwner } = this.props;
    const ownerId = (initialOwner as Owner).id;

    try {
      await this.saveOwner(ownerId, values);
    } finally {
      setSubmitting(false);
    }
  };

  saveOwner = async (
    ownerId: number | undefined,
    ownerFields: OwnerFields
  ): Promise<void> => {
    let newOwner;
    try {
      newOwner = await (ownerId === undefined
        ? new OwnerApi().addOwner({ ownerFields })
        : new OwnerApi().updateOwner({ ownerId, ownerFields }));
    } catch (response) {
      console.log('ERROR?!...', response);
      this.setState({ error: response });
    }

    this.props.history.push({
      pathname: '/owners/' + newOwner.id,
    });
  };

  render() {
    const { initialOwner } = this.props;
    const ownerId = (initialOwner as Owner).id;

    return (
      <div>
        <h2>New Owner</h2>
        <Formik
          initialValues={initialOwner}
          validationSchema={Yup.object().shape({
            firstName: Yup.string()
              .matches(
                /^[a-zA-Z]+$/,
                'Only ASCII characters A to Z are allowed'
              )
              .max(30, 'Must be at most 30 characters')
              .required('Required'),
            lastName: Yup.string()
              .matches(
                /^[a-zA-Z]+$/,
                'Only ASCII characters A to Z are allowed'
              )
              .max(30, 'Must be at most 30 characters')
              .required('Required'),
            address: Yup.string()
              .max(255, 'Must be at most 255 characters')
              .required('Required'),
            city: Yup.string()
              .max(80, 'Must be at most 80 characters')
              .required('Required'),
            telephone: Yup.string()
              .matches(/^[0-9]+$/, 'Only digits 0 to 9 are allowed')
              .max(10, 'Must be at most 10 digits')
              .required('Required'),
          })}
          onSubmit={this.onSubmit}
        >
          {({ isSubmitting }) => (
            <Form className="form-horizontal">
              <div className="form-group has-feedback">
                <Input name="firstName" label="First Name" />
                <Input name="lastName" label="Last Name" />
                <Input name="address" label="Address" />
                <Input name="city" label="City" />
                <Input name="telephone" label="Telephone" />
              </div>
              <div className="form-group">
                <div className="col-sm-offset-2 col-sm-10">
                  <button
                    className="btn btn-default"
                    type="submit"
                    disabled={isSubmitting}
                  >
                    {ownerId === undefined ? 'Add Owner' : 'Update Owner'}
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

export default withRouter(OwnerEditor);
