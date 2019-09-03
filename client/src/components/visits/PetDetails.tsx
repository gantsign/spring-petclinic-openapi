import * as React from 'react';

import { Owner, Pet } from 'petclinic-api';

import moment from 'moment';

export default ({ owner, pet }: { owner: Owner; pet: Pet }) => (
  <table className="table table-striped">
    <thead>
      <tr>
        <th>Name</th>
        <th>Birth Date</th>
        <th>Type</th>
        <th>Owner</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>{pet.name}</td>
        <td>{moment(pet.birthDate).format('YYYY-MM-DD')}</td>
        <td>{pet.type.name}</td>
        <td>
          {owner.firstName} {owner.lastName}
        </td>
      </tr>
    </tbody>
  </table>
);
