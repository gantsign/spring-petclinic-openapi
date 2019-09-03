import * as React from 'react';
import OwnerEditor from './OwnerEditor';
import { OwnerFields } from 'petclinic-api';

const newOwner = (): OwnerFields => ({
  firstName: '',
  lastName: '',
  address: '',
  city: '',
  telephone: '',
});

export default () => <OwnerEditor initialOwner={newOwner()} />;
