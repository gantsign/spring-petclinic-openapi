import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';

import { url } from '../../util';

import Input from '../form/Input';

import { Digits, NotEmpty } from '../form/Constraints';

import { IError, IFieldError, IFieldErrors } from '../../types';
import { Owner, OwnerApi, OwnerFields } from 'petclinic-api';

interface IOwnerEditorProps extends RouteComponentProps {
  initialOwner?: Owner | OwnerFields;
}

interface IOwnerEditorState {
  owner?: OwnerFields;
  error?: IError;
}

class OwnerEditor extends React.Component<
  IOwnerEditorProps,
  IOwnerEditorState
> {
  constructor(props) {
    super(props);
    this.onInputChange = this.onInputChange.bind(this);
    this.onSubmit = this.onSubmit.bind(this);

    this.state = {
      owner: Object.assign({}, props.initialOwner),
    };
  }

  onSubmit(event) {
    event.preventDefault();

    const { owner } = this.state;

    if (!owner) {
      return;
    }

    const { initialOwner } = this.props;
    const ownerId = (initialOwner as Owner).id;

    (ownerId === undefined
      ? new OwnerApi().addOwner({ ownerFields: owner })
      : new OwnerApi().updateOwner({ ownerId, ownerFields: owner })
    )
      .then(newOwner =>
        this.props.history.push({
          pathname: '/owners/' + newOwner.id,
        })
      )
      .catch(response => {
        console.log('ERROR?!...', response);
        this.setState({ error: response });
      });
  }

  onInputChange(
    name: string,
    value: string,
    fieldError: IFieldError | undefined
  ) {
    const { owner, error } = this.state;
    const modifiedOwner: OwnerFields = Object.assign({}, owner, {
      [name]: value,
    });

    const newState: IOwnerEditorState = { owner: modifiedOwner };

    const newFieldErrors: IFieldErrors | undefined =
      fieldError === undefined
        ? undefined
        : error
        ? Object.assign({}, error.fieldErrors, { [name]: fieldError })
        : { [name]: fieldError };
    const newError: IError | undefined =
      newFieldErrors === undefined
        ? undefined
        : { fieldErrors: newFieldErrors };
    if (newError) {
      newState.error = newError;
    }

    this.setState(newState);
  }

  render() {
    const { owner, error } = this.state;

    if (!owner) {
      return;
    }

    const { initialOwner } = this.props;
    const ownerId = (initialOwner as Owner).id;

    return (
      <div>
        <h2>New Owner</h2>
        <form
          className="form-horizontal"
          method="POST"
          action={url('/api/owner')}
        >
          <div className="form-group has-feedback">
            <Input
              object={owner}
              error={error}
              constraint={NotEmpty}
              label="First Name"
              name="firstName"
              onChange={this.onInputChange}
            />
            <Input
              object={owner}
              error={error}
              constraint={NotEmpty}
              label="Last Name"
              name="lastName"
              onChange={this.onInputChange}
            />
            <Input
              object={owner}
              error={error}
              constraint={NotEmpty}
              label="Address"
              name="address"
              onChange={this.onInputChange}
            />
            <Input
              object={owner}
              error={error}
              constraint={NotEmpty}
              label="City"
              name="city"
              onChange={this.onInputChange}
            />
            <Input
              object={owner}
              error={error}
              constraint={Digits(10)}
              label="Telephone"
              name="telephone"
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
                {ownerId === undefined ? 'Add Owner' : 'Update Owner'}
              </button>
            </div>
          </div>
        </form>
      </div>
    );
  }
}

export default withRouter(OwnerEditor);
