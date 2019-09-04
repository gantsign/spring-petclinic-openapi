import { ISelectOption } from '../../types';
import { Owner, OwnerApi, PetApi, PetType } from 'petclinic-api';

const toSelectOptions = (petTypes: PetType[]): ISelectOption[] =>
  petTypes.map(petType => ({
    value: petType.id,
    name: petType.name || '',
  }));

export default async <T>(
  ownerId: number,
  petLoaderPromise: Promise<T>
): Promise<{ petTypes: ISelectOption[]; owner: Owner; pet: T }> => {
  return {
    petTypes: await new PetApi().listPetTypes().then(toSelectOptions),
    owner: await new OwnerApi().getOwner({ ownerId }),
    pet: await petLoaderPromise,
  };
};
