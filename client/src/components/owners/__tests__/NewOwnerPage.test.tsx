import React from 'react';
import renderer from 'react-test-renderer';
import NewOwnerPage from '../NewOwnerPage';
import { StaticRouter } from 'react-router-dom';

it('renders correctly', async () => {
  const tree = renderer
    .create(
      <StaticRouter>
        <NewOwnerPage />
      </StaticRouter>
    )
    .toJSON();

  expect(tree).toMatchSnapshot();
});
