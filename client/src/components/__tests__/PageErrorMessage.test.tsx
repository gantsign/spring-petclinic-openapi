import React from 'react';
import renderer from 'react-test-renderer';
import PageErrorMessage from '../PageErrorMessage';

it('renders correctly', () => {
  const tree = renderer
    .create(<PageErrorMessage error={{ message: 'msg1' }} />)
    .toJSON();
  expect(tree).toMatchSnapshot();
});
