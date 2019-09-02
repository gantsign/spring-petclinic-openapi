// React and Hot Loader
import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { AppContainer } from 'react-hot-loader';
// The Application
import Root from './Root';

require('./styles/less/petclinic.less');

// Render Application
const mountPoint = document.getElementById('mount');
ReactDOM.render(
  <AppContainer>
    <Root />
  </AppContainer>,
  mountPoint
);

declare var module: any;
if (module.hot) {
  module.hot.accept('./Root', () => {
    const NextApp = require('./Root').default;
    ReactDOM.render(
      <AppContainer>
        <NextApp />
      </AppContainer>,
      mountPoint
    );
  });
}
