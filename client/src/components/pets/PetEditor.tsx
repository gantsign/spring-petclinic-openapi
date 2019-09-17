import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';

import { Form, Formik } from 'formik';
import * as Yup from 'yup';

import { Owner, Pet, PetApi, PetFields, RestError } from 'petclinic-api';

import Input from '../form/Input';
import DateInput from '../form/DateInput';
import SelectInput from '../form/SelectInput';

import { ISelectOption } from '../../types';

interface IPetEditorProps extends RouteComponentProps {
  pet: Pet | PetFields;
  owner: Owner;
  petTypes: ISelectOption[];
}

interface IPetEditorState {
  error?: RestError;
}

class PetEditor extends React.Component<IPetEditorProps, IPetEditorState> {
  constructor(props) {
    super(props);
    this.onSubmit = this.onSubmit.bind(this);

    this.state = {};
  }

  async onSubmit(values, { setSubmitting }) {
    const request: PetFields = {
      birthDate: values.birthDate,
      name: values.name,
      typeId: Number(values.typeId),
    };

    const initialPet = this.props.pet;
    const petId = (initialPet as Pet).id;

    try {
      await this.savePet(petId, request);
    } finally {
      setSubmitting(false);
    }
  }

  savePet = async (petId: number | undefined, petFields: PetFields) => {
    const { owner } = this.props;

    try {
      await (petId === undefined
        ? new PetApi().addPet({ ownerId: owner.id, petFields })
        : new PetApi().updatePet({ ownerId: owner.id, petId, petFields }));
    } catch (response) {
      console.log('ERROR?!...', response);
      this.setState({ error: response });
    }

    this.props.history.push({
      pathname: '/owners/' + owner.id,
    });
  };

  render() {
    const { owner, petTypes } = this.props;

    const initialPet = this.props.pet;
    const petId = (initialPet as Pet).id;

    const formLabel = petId === undefined ? 'Add Pet' : 'Update Pet';

    return (
      <div>
        <h2>{formLabel}</h2>
        <Formik
          initialValues={initialPet}
          validationSchema={Yup.object().shape({
            name: Yup.string()
              .max(30, 'Must be at most 30 characters')
              .required('Required'),
            birthDate: Yup.date()
              .nullable()
              .required('Required'),
            typeId: Yup.number().required('Required'),
          })}
          onSubmit={this.onSubmit}
        >
          {({ isSubmitting }) => (
            <Form className="form-horizontal">
              <div className="form-group has-feedback">
                <div className="form-group">
                  <label className="col-sm-2 control-label">Owner</label>
                  <div className="col-sm-10">
                    {owner.firstName} {owner.lastName}
                  </div>
                </div>

                <Input name="name" label="Name" />
                <DateInput name="birthDate" label="Birth date" />
                <SelectInput name="typeId" label="Type" options={petTypes} />
              </div>
              <div className="form-group">
                <div className="col-sm-offset-2 col-sm-10">
                  <button
                    className="btn btn-default"
                    type="submit"
                    disabled={isSubmitting}
                  >
                    {formLabel}
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

export default withRouter(PetEditor);
