import React from 'react';
import renderer from 'react-test-renderer';
import waitForExpect from 'wait-for-expect';
import Loading from '../Loading';

it('renders correctly', async () => {
  const component = renderer.create(<Loading />);

  await waitForExpect(() => {
    expect(component.root.instance.state.show).toBe(true);
    expect(component.toJSON()).toMatchSnapshot();
  });
});
