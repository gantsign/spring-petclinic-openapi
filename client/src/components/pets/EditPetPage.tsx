import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';

import { IError, ISelectOption } from '../../types';

import { Owner, Pet, PetApi } from 'petclinic-api';

import LoadingPanel from './LoadingPanel';
import PetEditor from './PetEditor';

import createPetEditorModel from './createPetEditorModel';

import PageErrorMessage from '../PageErrorMessage';
import extractError from '../../data/extractError';

interface IEditPetPageProps extends RouteComponentProps {}

interface IPetData {
  pet: Pet;
  owner: Owner;
  petTypes: ISelectOption[];
}

interface IEditPetPageState {
  error?: IError;
  data?: IPetData;
}

class EditPetPage extends React.Component<
  IEditPetPageProps,
  IEditPetPageState
> {
  async componentDidMount() {
    const ownerId = Number(this.props.match.params['ownerId']);
    const petId = Number(this.props.match.params['petId']);

    try {
      const loadPetPromise = new PetApi().getPet({ ownerId, petId });

      const data = await createPetEditorModel(ownerId, loadPetPromise);

      this.setState({ data });
    } catch (response) {
      const error = await extractError(response);
      this.setState({ error });
    }
  }

  render() {
    const { error, data } = this.state || {};

    if (error) {
      return <PageErrorMessage error={error} />;
    }

    if (!data) {
      return <LoadingPanel />;
    }

    const { pet, owner, petTypes } = data;

    return <PetEditor pet={pet} owner={owner} petTypes={petTypes} />;
  }
}

export default withRouter(EditPetPage);
