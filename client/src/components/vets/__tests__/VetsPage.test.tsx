import React from 'react';
import renderer from 'react-test-renderer';
import VetsPage from '../VetsPage';
import waitForExpect from 'wait-for-expect';
import { VetApiContext } from '../../../data/VetApiProvider';
import { VetApi } from 'petclinic-api';
import { StaticRouter } from 'react-router-dom';

it('renders correctly', async () => {
  const mock = new VetApi();
  mock.listVets = async () => [
    {
      id: 1,
      firstName: 'firstName1',
      lastName: 'lastName1',
      specialties: [
        {
          id: 2,
          name: 'specialtyName2',
        },
      ],
    },
  ];

  const component = renderer.create(
    <StaticRouter>
      <VetApiContext.Provider value={mock}>
        <VetsPage />
      </VetApiContext.Provider>
    </StaticRouter>
  );

  const page = component.root.find(node => node.type.name === 'VetsPage');

  await waitForExpect(() => {
    expect(page.instance.state.vets[0].id).toBe(1);
    expect(component.toJSON()).toMatchSnapshot();
  });
});
