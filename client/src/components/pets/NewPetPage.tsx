import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';

import { IError, ISelectOption } from '../../types';
import { Owner, PetFields } from 'petclinic-api';

import Loading from '../Loading';
import PetEditor, { toSelectOptions } from './PetEditor';

import { withOwnerApi, WithOwnerApiProps } from '../../data/OwnerApiProvider';
import { withPetApi, WithPetApiProps } from '../../data/PetApiProvider';

import PageErrorMessage from '../PageErrorMessage';
import extractError from '../../data/extractError';

interface INewPetPageProps
  extends RouteComponentProps,
    WithOwnerApiProps,
    WithPetApiProps {}

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

    const { ownerApi, petApi } = this.props;

    try {
      const loadPetTypesPromise = petApi.listPetTypes().then(toSelectOptions);
      const loadOwnerPromise = ownerApi.getOwner({ ownerId });

      this.setState({
        data: {
          petTypes: await loadPetTypesPromise,
          owner: await loadOwnerPromise,
          pet: NEW_PET,
        },
      });
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
      return <Loading />;
    }

    const { pet, owner, petTypes } = data;

    return <PetEditor pet={pet} owner={owner} petTypes={petTypes} />;
  }
}

export default withOwnerApi(withPetApi(withRouter(NewPetPage)));
