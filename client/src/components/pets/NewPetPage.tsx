import * as React from 'react';

import { IEditablePet, IOwner, ISelectOption } from '../../types';

import LoadingPanel from './LoadingPanel';
import PetEditor from './PetEditor';

import createPetEditorModel from './createPetEditorModel';

interface INewPetPageProps {
  params: { ownerId: string };
}

interface INewPetPageState {
  pet?: IEditablePet;
  owner?: IOwner;
  petTypes?: ISelectOption[];
}

const NEW_PET: IEditablePet = {
  id: null,
  isNew: true,
  name: '',
  birthDate: null,
  typeId: null,
};

export default class NewPetPage extends React.Component<
  INewPetPageProps,
  INewPetPageState
> {
  componentDidMount() {
    createPetEditorModel(
      this.props.params.ownerId,
      Promise.resolve(NEW_PET)
    ).then(model => this.setState(model));
  }

  render() {
    if (!this.state) {
      return <LoadingPanel />;
    }

    return <PetEditor {...this.state} />;
  }
}
