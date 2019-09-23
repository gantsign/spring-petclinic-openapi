import React from 'react';
import renderer from 'react-test-renderer';
import WelcomePage from '../WelcomePage';
import App from '../App';
import { StaticRouter } from 'react-router-dom';

it('renders correctly', () => {
  const tree = renderer
    .create(
      <StaticRouter>
        <App>
          <WelcomePage />
        </App>
      </StaticRouter>
    )
    .toJSON();
  expect(tree).toMatchSnapshot();
});
