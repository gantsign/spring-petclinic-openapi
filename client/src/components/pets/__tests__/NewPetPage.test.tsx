import React from 'react';
import renderer from 'react-test-renderer';
import NewPetPage from '../NewPetPage';
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

  const petMock = new PetApi();
  petMock.listPetTypes = async () => [
    {
      id: 3,
      name: 'typeName3',
    },
  ];

  const component = renderer.create(
    <StaticRouter location="/owners/1/pets/new">
      <PetApiContext.Provider value={petMock}>
        <OwnerApiContext.Provider value={ownerMock}>
          <Route path="/owners/:ownerId(\d+)/pets/new" component={NewPetPage} />
        </OwnerApiContext.Provider>
      </PetApiContext.Provider>
    </StaticRouter>
  );

  const page = component.root.find(node => node.type.name === 'NewPetPage');

  await waitForExpect(() => {
    expect(page.instance.state.data.owner.id).toBe(1);
    expect(component.toJSON()).toMatchSnapshot();
  });
});
