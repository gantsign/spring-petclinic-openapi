import React from 'react';
import renderer from 'react-test-renderer';
import EditOwnerPage from '../EditOwnerPage';
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
    pets: [],
  });

  const component = renderer.create(
    <StaticRouter location="/owners/1/edit">
      <OwnerApiContext.Provider value={mock}>
        <Route path="/owners/:ownerId(\d+)/edit" component={EditOwnerPage} />
      </OwnerApiContext.Provider>
    </StaticRouter>
  );

  const page = component.root.find(node => node.type.name === 'EditOwnerPage');

  await waitForExpect(() => {
    expect(page.instance.state.owner.id).toBe(1);
    expect(component.toJSON()).toMatchSnapshot();
  });
});
