// React and Hot Loader
import * as React from 'react';
import * as ReactDOM from 'react-dom';

import './styles/scss/petclinic.scss';
// The Application
import Root from './Root';

// Render Application
const mountPoint = document.getElementById('mount');
ReactDOM.render(<Root />, mountPoint);
