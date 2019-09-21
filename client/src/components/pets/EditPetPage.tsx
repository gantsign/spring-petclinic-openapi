import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';

import { IError, ISelectOption } from '../../types';

import { Owner, Pet } from 'petclinic-api';
import { withOwnerApi, WithOwnerApiProps } from '../../data/OwnerApiProvider';
import { withPetApi, WithPetApiProps } from '../../data/PetApiProvider';

import LoadingPanel from './LoadingPanel';
import PetEditor, { toSelectOptions } from './PetEditor';

import PageErrorMessage from '../PageErrorMessage';
import extractError from '../../data/extractError';

interface IEditPetPageProps
  extends RouteComponentProps,
    WithOwnerApiProps,
    WithPetApiProps {}

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

    const { ownerApi, petApi } = this.props;

    try {
      const loadPetTypesPromise = petApi.listPetTypes().then(toSelectOptions);
      const loadOwnerPromise = ownerApi.getOwner({ ownerId });
      const loadPetPromise = petApi.getPet({ ownerId, petId });

      this.setState({
        data: {
          petTypes: await loadPetTypesPromise,
          owner: await loadOwnerPromise,
          pet: await loadPetPromise,
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
      return <LoadingPanel />;
    }

    const { pet, owner, petTypes } = data;

    return <PetEditor pet={pet} owner={owner} petTypes={petTypes} />;
  }
}

export default withOwnerApi(withPetApi(withRouter(EditPetPage)));
