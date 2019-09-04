import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';
import { url } from '../../util';
import { Owner, Pet, PetApi, PetFields } from 'petclinic-api';

import Input from '../form/Input';
import DateInput from '../form/DateInput';
import SelectInput from '../form/SelectInput';

import { IEditablePet, IError, IPetTypeId, ISelectOption } from '../../types';

import moment from 'moment';

interface IPetEditorProps extends RouteComponentProps {
  pet: Pet | PetFields;
  owner: Owner;
  petTypes: ISelectOption[];
}

interface IPetEditorState {
  editablePet?: IEditablePet;
  error?: IError;
}

class PetEditor extends React.Component<IPetEditorProps, IPetEditorState> {
  constructor(props) {
    super(props);
    this.onInputChange = this.onInputChange.bind(this);
    this.onSubmit = this.onSubmit.bind(this);

    const initialPet: Pet | PetFields = props.pet;
    const birthDate = initialPet.birthDate;

    this.state = {
      editablePet: {
        name: initialPet.name || '',
        birthDate:
          birthDate !== undefined
            ? moment(birthDate).format('YYYY-MM-DD')
            : undefined,
        typeId: initialPet.typeId as IPetTypeId,
      },
    };
  }

  onSubmit(event) {
    event.preventDefault();

    const { editablePet } = this.state;

    if (!editablePet) {
      throw new Error('Invalid state: no editablePet');
    }

    const { typeId = null } = editablePet;

    if (typeId == null) {
      throw new Error('Invalid state: no typeId');
    }

    const request: PetFields = {
      birthDate: new Date(editablePet.birthDate || ''),
      name: editablePet.name,
      typeId: Number(typeId),
    };

    const initialPet = this.props.pet;
    const petId = (initialPet as Pet).id;

    this.savePet(petId, request);
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

  onInputChange(name: string, value: string) {
    const { editablePet } = this.state;
    const modifiedPet = Object.assign({}, editablePet, { [name]: value });

    this.setState({ editablePet: modifiedPet });
  }

  render() {
    const { owner, petTypes } = this.props;
    const { editablePet, error } = this.state;

    if (!editablePet) {
      return <></>;
    }

    const initialPet = this.props.pet;
    const petId = (initialPet as Pet).id;

    const formLabel = petId === undefined ? 'Add Pet' : 'Update Pet';

    return (
      <div>
        <h2>{formLabel}</h2>
        <form
          className="form-horizontal"
          method="POST"
          action={url('/api/owner')}
        >
          <div className="form-group has-feedback">
            <div className="form-group">
              <label className="col-sm-2 control-label">Owner</label>
              <div className="col-sm-10">
                {owner.firstName} {owner.lastName}
              </div>
            </div>

            <Input
              object={editablePet}
              error={error}
              label="Name"
              name="name"
              onChange={this.onInputChange}
            />
            <DateInput
              object={editablePet}
              error={error}
              label="Birth date"
              name="birthDate"
              onChange={this.onInputChange}
            />
            <SelectInput
              object={editablePet}
              error={error}
              label="Type"
              name="typeId"
              options={petTypes}
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
                {formLabel}
              </button>
            </div>
          </div>
        </form>
      </div>
    );
  }
}

export default withRouter(PetEditor);
