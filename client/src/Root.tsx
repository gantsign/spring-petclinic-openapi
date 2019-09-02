import * as React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
// Routes
import configureRoutes from './configureRoutes';

const Root = () => <Router>{configureRoutes()}</Router>;

export default Root;
