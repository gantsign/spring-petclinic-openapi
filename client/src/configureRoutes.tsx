import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import App from './components/App';

import WelcomePage from './components/WelcomePage';
import FindOwnersPage from './components/owners/FindOwnersPage';
import OwnersPage from './components/owners/OwnersPage';
import NewOwnerPage from './components/owners/NewOwnerPage';
import EditOwnerPage from './components/owners/EditOwnerPage';
import NewPetPage from './components/pets/NewPetPage';
import EditPetPage from './components/pets/EditPetPage';
import VisitsPage from './components/visits/VisitsPage';
import VetsPage from './components/vets/VetsPage';
import ErrorPage from './components/ErrorPage';

import NotFoundPage from './components/NotFoundPage';

export default () => (
  <Route
    render={() => (
      <App>
        <Switch>
          <Route path="/" exact component={WelcomePage} />
          <Route path="/owners/list" component={FindOwnersPage} />
          <Route path="/owners/new" component={NewOwnerPage} />
          <Route path="/owners/:ownerId(\d+)/edit" component={EditOwnerPage} />
          <Route
            path="/owners/:ownerId(\d+)/pets/:petId(\d+)/edit"
            component={EditPetPage}
          />
          <Route path="/owners/:ownerId(\d+)/pets/new" component={NewPetPage} />
          <Route
            path="/owners/:ownerId(\d+)/pets/:petId(\d+)/visits/new"
            component={VisitsPage}
          />
          <Route path="/owners/:ownerId(\d+)" component={OwnersPage} />
          <Route path="/vets" component={VetsPage} />
          <Route path="/error" component={ErrorPage} />
          <Route path="*" component={NotFoundPage} />
        </Switch>
      </App>
    )}
  />
);
