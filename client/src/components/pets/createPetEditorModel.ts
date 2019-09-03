import { ISelectOption } from '../../types';
import { OwnerApi, PetApi, PetType } from 'petclinic-api';

const toSelectOptions = (petTypes: PetType[]): ISelectOption[] =>
  petTypes.map(petType => ({
    value: petType.id,
    name: petType.name || '',
  }));

export default (
  ownerId: number,
  petLoaderPromise: Promise<any>
): Promise<any> => {
  return Promise.all([
    new PetApi().listPetTypes().then(toSelectOptions),
    new OwnerApi().getOwner({ ownerId }),
    petLoaderPromise,
  ]).then(results => ({
    petTypes: results[0],
    owner: results[1],
    pet: results[2],
  }));
};
