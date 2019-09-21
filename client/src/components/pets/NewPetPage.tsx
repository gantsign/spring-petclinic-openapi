import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';

import { IError, ISelectOption } from '../../types';
import { Owner, PetFields } from 'petclinic-api';

import LoadingPanel from './LoadingPanel';
import PetEditor from './PetEditor';

import createPetEditorModel from './createPetEditorModel';

import PageErrorMessage from '../PageErrorMessage';
import extractError from '../../data/extractError';

interface INewPetPageProps extends RouteComponentProps {}

interface IPetData {
  pet: PetFields;
  owner: Owner;
  petTypes: ISelectOption[];
}

interface INewPetPageState {
  error?: IError;
  data?: IPetData;
}

const NEW_PET: PetFields = {
  name: '',
  birthDate: undefined,
  typeId: undefined,
};

class NewPetPage extends React.Component<INewPetPageProps, INewPetPageState> {
  async componentDidMount() {
    const ownerId = Number(this.props.match.params['ownerId']);

    try {
      const data = await createPetEditorModel(
        ownerId,
        Promise.resolve(NEW_PET)
      );
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

export default withRouter(NewPetPage);
