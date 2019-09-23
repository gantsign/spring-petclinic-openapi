import React from 'react';
import renderer from 'react-test-renderer';
import OwnersPage from '../OwnersPage';
import waitForExpect from 'wait-for-expect';
import { OwnerApiContext } from '../../../data/OwnerApiProvider';
import { OwnerApi } from 'petclinic-api';
import { Route, StaticRouter } from 'react-router-dom';

it('renders correctly', async () => {
  const mock = new OwnerApi();
  mock.getOwner = async () => ({
    id: 1,
    firstName: 'firstName1',
    lastName: 'lastName1',
    address: 'address1',
    city: 'city1',
    telephone: '012345',
    pets: [
      {
        id: 2,
        name: 'petName2',
        birthDate: new Date(2019, 0, 1),
        typeId: 3,
        type: {
          id: 3,
          name: 'typeName3',
        },
        visits: [
          {
            id: 4,
            date: new Date(2019, 1, 2),
            description: 'description4',
          },
        ],
      },
    ],
  });

  const component = renderer.create(
    <StaticRouter location="/owners/1">
      <OwnerApiContext.Provider value={mock}>
        <Route path="/owners/:ownerId(\d+)" component={OwnersPage} />
      </OwnerApiContext.Provider>
    </StaticRouter>
  );

  const page = component.root.find(node => node.type.name === 'OwnersPage');

  await waitForExpect(() => {
    expect(page.instance.state.owner.id).toBe(1);
    expect(component.toJSON()).toMatchSnapshot();
  });
});
