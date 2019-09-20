import * as React from 'react';
import loadable from '@loadable/component';
import { Route, Switch } from 'react-router-dom';

import App from './components/App';

const WelcomePage = loadable(() => import('./components/WelcomePage'));
const FindOwnersPage = loadable(() =>
  import('./components/owners/FindOwnersPage')
);
const OwnersPage = loadable(() => import('./components/owners/OwnersPage'));
const NewOwnerPage = loadable(() => import('./components/owners/NewOwnerPage'));
const EditOwnerPage = loadable(() =>
  import('./components/owners/EditOwnerPage')
);
const NewPetPage = loadable(() => import('./components/pets/NewPetPage'));
const EditPetPage = loadable(() => import('./components/pets/EditPetPage'));
const VisitsPage = loadable(() => import('./components/visits/VisitsPage'));
const VetsPage = loadable(() => import('./components/vets/VetsPage'));
const ErrorPage = loadable(() => import('./components/ErrorPage'));

const NotFoundPage = loadable(() => import('./components/NotFoundPage'));

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
