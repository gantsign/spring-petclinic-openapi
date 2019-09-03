import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';

import { ISelectOption } from '../../types';

import { Owner, Pet, PetApi } from 'petclinic-api';

import LoadingPanel from './LoadingPanel';
import PetEditor from './PetEditor';

import createPetEditorModel from './createPetEditorModel';

interface IEditPetPageProps extends RouteComponentProps {}

interface IEditPetPageState {
  pet?: Pet;
  owner?: Owner;
  petTypes?: ISelectOption[];
}

class EditPetPage extends React.Component<
  IEditPetPageProps,
  IEditPetPageState
> {
  componentDidMount() {
    const ownerId = Number(this.props.match.params['ownerId']);
    const petId = Number(this.props.match.params['petId']);

    const loadPetPromise = new PetApi().getPet({ ownerId, petId });

    createPetEditorModel(ownerId, loadPetPromise).then(model =>
      this.setState(model)
    );
  }

  render() {
    if (!this.state) {
      return <LoadingPanel />;
    }

    const { pet, owner, petTypes } = this.state;

    if (!pet) {
      return <LoadingPanel />;
    }
    if (!owner) {
      return <LoadingPanel />;
    }
    if (!petTypes) {
      return <LoadingPanel />;
    }

    return <PetEditor pet={pet} owner={owner} petTypes={petTypes} />;
  }
}

export default withRouter(EditPetPage);
