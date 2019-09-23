import React from 'react';
import renderer from 'react-test-renderer';
import EditPetPage from '../EditPetPage';
import waitForExpect from 'wait-for-expect';
import { OwnerApiContext } from '../../../data/OwnerApiProvider';
import { PetApiContext } from '../../../data/PetApiProvider';
import { OwnerApi, PetApi } from 'petclinic-api';
import { Route, StaticRouter } from 'react-router-dom';

it('renders correctly', async () => {
  const ownerMock = new OwnerApi();
  ownerMock.getOwner = async () => ({
    id: 1,
    firstName: 'firstName1',
    lastName: 'lastName1',
    address: 'address1',
    city: 'city1',
    telephone: '012345',
    pets: [],
  });

  const petType = {
    id: 3,
    name: 'typeName3',
  };

  const petMock = new PetApi();
  petMock.listPetTypes = async () => [petType];
  petMock.getPet = async () => ({
    id: 2,
    name: 'petName2',
    birthDate: new Date(2019, 0, 1),
    typeId: 3,
    type: petType,
    visits: [],
  });

  const component = renderer.create(
    <StaticRouter location="/owners/1/pets/2/edit">
      <PetApiContext.Provider value={petMock}>
        <OwnerApiContext.Provider value={ownerMock}>
          <Route
            path="/owners/:ownerId(\d+)/pets/:petId(\d+)/edit"
            component={EditPetPage}
          />
        </OwnerApiContext.Provider>
      </PetApiContext.Provider>
    </StaticRouter>
  );

  const page = component.root.find(node => node.type.name === 'EditPetPage');

  await waitForExpect(() => {
    expect(page.instance.state.data.owner.id).toBe(1);
    expect(component.toJSON()).toMatchSnapshot();
  });
});
