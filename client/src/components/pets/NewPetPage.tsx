import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';

import { IEditablePet, IOwner, ISelectOption } from '../../types';

import LoadingPanel from './LoadingPanel';
import PetEditor from './PetEditor';

import createPetEditorModel from './createPetEditorModel';

interface INewPetPageProps extends RouteComponentProps {}

interface INewPetPageState {
  pet?: IEditablePet;
  owner?: IOwner;
  petTypes?: ISelectOption[];
}

const NEW_PET: IEditablePet = {
  isNew: true,
  name: '',
};

class NewPetPage extends React.Component<INewPetPageProps, INewPetPageState> {
  componentDidMount() {
    const ownerId = Number(this.props.match.params['ownerId']);

    createPetEditorModel(ownerId, Promise.resolve(NEW_PET)).then(model =>
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

export default withRouter(NewPetPage);
