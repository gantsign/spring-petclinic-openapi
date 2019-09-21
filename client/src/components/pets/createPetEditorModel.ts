import { IPetType, ISelectOption } from '../../types';
import { url, submitForm } from '../../util';

const toSelectOptions = (petTypes: IPetType[]): ISelectOption[] => petTypes.map(petType => ({ value: petType.id, name: petType.name }));

export default (ownerId: string, petLoaderPromise: Promise<any>): Promise<any> => {
  return Promise.all(
    [fetch(url('/api/pet-type'))
      .then(response => response.json())
      .then(toSelectOptions),
    fetch(url('/api/owner/' + ownerId))
      .then(response => response.json()),
      petLoaderPromise,
    ]
  ).then(results => ({
    petTypes: results[0],
    owner: results[1],
    pet: results[2]
  }));
};
