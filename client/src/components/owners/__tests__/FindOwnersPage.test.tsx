import React from 'react';
import renderer from 'react-test-renderer';
import FindOwnersPage from '../FindOwnersPage';
import waitForExpect from 'wait-for-expect';
import { OwnerApiContext } from '../../../data/OwnerApiProvider';
import { OwnerApi } from 'petclinic-api';
import { StaticRouter } from 'react-router-dom';

it('renders correctly', async () => {
  const mock = new OwnerApi();
  mock.listOwners = async () => [
    {
      id: 1,
      firstName: 'firstName1',
      lastName: 'lastName1',
      address: 'address1',
      city: 'city1',
      telephone: '012345',
      pets: [],
    },
  ];

  const component = renderer.create(
    <StaticRouter location="/owners/list?lastName=lastName1">
      <OwnerApiContext.Provider value={mock}>
        <FindOwnersPage />
      </OwnerApiContext.Provider>
    </StaticRouter>
  );

  const page = component.root.find(node => node.type.name === 'FindOwnersPage');

  await waitForExpect(() => {
    expect(page.instance.state.owners[0].id).toBe(1);
    expect(component.toJSON()).toMatchSnapshot();
  });
});
