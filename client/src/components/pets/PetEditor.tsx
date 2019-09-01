import * as React from 'react';

import { submitForm, url } from '../../util';

import Input from '../form/Input';
import DateInput from '../form/DateInput';
import SelectInput from '../form/SelectInput';

import {
  IEditablePet,
  IError,
  IOwner,
  IPetRequest,
  IRouterContext,
  ISelectOption,
} from '../../types';

interface IPetEditorProps {
  pet: IEditablePet;
  owner: IOwner;
  petTypes: ISelectOption[];
}

interface IPetEditorState {
  editablePet?: IEditablePet;
  error?: IError;
}

export default class PetEditor extends React.Component<
  IPetEditorProps,
  IPetEditorState
> {
  context: IRouterContext;

  static contextTypes = {
    router: React.PropTypes.object.isRequired,
  };

  constructor(props) {
    super(props);
    this.onInputChange = this.onInputChange.bind(this);
    this.onSubmit = this.onSubmit.bind(this);

    this.state = { editablePet: Object.assign({}, props.pet) };
  }

  onSubmit(event) {
    event.preventDefault();

    const { owner } = this.props;
    const { editablePet } = this.state;

    if (!editablePet) {
      throw new Error('Invalid state: no editablePet');
    }

    const { typeId = null } = editablePet;

    if (typeId == null) {
      throw new Error('Invalid state: no typeId');
    }

    const request: IPetRequest = {
      birthDate: editablePet.birthDate,
      name: editablePet.name,
      typeId,
    };

    const url = editablePet.isNew
      ? '/api/owner/' + owner.id + '/pet'
      : '/api/owner/' + owner.id + '/pet/' + editablePet.id;
    submitForm(
      editablePet.isNew ? 'POST' : 'PUT',
      url,
      request,
      (status, response) => {
        if (status === 201 || status === 204) {
          this.context.router.push({
            pathname: '/owners/' + owner.id,
          });
        } else {
          console.log('ERROR?!...', response);
          this.setState({ error: response });
        }
      }
    );
  }

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

    const formLabel = editablePet.isNew ? 'Add Pet' : 'Update Pet';

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
