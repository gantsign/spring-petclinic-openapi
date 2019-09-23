import React from 'react';
import renderer from 'react-test-renderer';
import ErrorPage from '../ErrorPage';
import waitForExpect from 'wait-for-expect';
import { FailingApiContext } from '../../data/FailingApiProvider';
import { FailingApi } from 'petclinic-api';

it('renders correctly', async () => {
  const mock = new FailingApi();
  mock.failingRequest = () => {
    throw {
      json: async () => ({
        message: 'error1',
      }),
    };
  };

  const component = renderer.create(
    <FailingApiContext.Provider value={mock}>
      <ErrorPage />
    </FailingApiContext.Provider>
  );

  const page = component.root.find(node => node.type.name === 'ErrorPage');

  await waitForExpect(() => {
    expect(page.instance.state.error.message).toBe('error1');
    expect(component.toJSON()).toMatchSnapshot();
  });
});
