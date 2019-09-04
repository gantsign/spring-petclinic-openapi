import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';

import { ISelectOption } from '../../types';
import { Owner, PetFields } from 'petclinic-api';

import LoadingPanel from './LoadingPanel';
import PetEditor from './PetEditor';

import createPetEditorModel from './createPetEditorModel';

interface INewPetPageProps extends RouteComponentProps {}

interface INewPetPageState {
  pet?: PetFields;
  owner?: Owner;
  petTypes?: ISelectOption[];
}

const NEW_PET: PetFields = {
  name: '',
};

class NewPetPage extends React.Component<INewPetPageProps, INewPetPageState> {
  async componentDidMount() {
    const ownerId = Number(this.props.match.params['ownerId']);

    const model = await createPetEditorModel(ownerId, Promise.resolve(NEW_PET));
    this.setState(model);
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
