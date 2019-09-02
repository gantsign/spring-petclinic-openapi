import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';

import { IEditablePet, IOwner, ISelectOption } from '../../types';

import { url } from '../../util';

import LoadingPanel from './LoadingPanel';
import PetEditor from './PetEditor';

import createPetEditorModel from './createPetEditorModel';

interface IEditPetPageProps extends RouteComponentProps {}

interface IEditPetPageState {
  pet?: IEditablePet;
  owner?: IOwner;
  petTypes?: ISelectOption[];
}

class EditPetPage extends React.Component<
  IEditPetPageProps,
  IEditPetPageState
> {
  componentDidMount() {
    const ownerId = Number(this.props.match.params['ownerId']);
    const petId = Number(this.props.match.params['petId']);

    const fetchUrl = url(`/api/owner/${ownerId}/pet/${petId}`);

    const loadPetPromise = fetch(fetchUrl).then(response => response.json());

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
