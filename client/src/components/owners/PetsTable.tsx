import * as React from 'react';

import { Link } from 'react-router-dom';
import { Owner, Pet } from 'petclinic-api';

import moment from 'moment';

const VisitsTable = ({ ownerId, pet }: { ownerId: number; pet: Pet }) => (
  <table className="table-condensed">
    <thead>
      <tr>
        <th>Visit Date</th>
        <th>Description</th>
      </tr>
    </thead>
    <tbody>
      {pet.visits.map(visit => (
        <tr key={visit.id}>
          <td>{moment(visit.date).format('YYYY-MM-DD')}</td>
          <td>{visit.description}</td>
        </tr>
      ))}
      <tr>
        <td>
          <Link to={`/owners/${ownerId}/pets/${pet.id}/edit`}>Edit Pet</Link>
        </td>
        <td>
          <Link to={`/owners/${ownerId}/pets/${pet.id}/visits/new`}>
            Add Visit
          </Link>
        </td>
      </tr>
    </tbody>
  </table>
);

export default ({ owner }: { owner: Owner }) => {
  const ownerId = owner.id;
  if (ownerId === undefined) {
    return <></>;
  }

  return (
    <section>
      <h2>Pets and Visits</h2>
      <table className="table table-striped">
        <tbody>
          {owner.pets.map(pet => (
            <tr key={pet.id}>
              <td style={{ verticalAlign: 'top' }}>
                <dl className="dl-horizontal">
                  <dt>Name</dt>
                  <dd>{pet.name}</dd>
                  <dt>Birth Date</dt>
                  <dd>{moment(pet.birthDate).format('YYYY-MM-DD')}</dd>
                  <dt>Type</dt>
                  <dd>{pet.type.name}</dd>
                </dl>
              </td>
              <td style={{ verticalAlign: 'top' }}>
                <VisitsTable ownerId={ownerId} pet={pet} />
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  );
};
