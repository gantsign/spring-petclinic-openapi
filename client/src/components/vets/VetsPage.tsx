import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';

import { Vet, VetApi } from 'petclinic-api';

import { IError } from '../../types';
import PageErrorMessage from '../PageErrorMessage';
import extractError from '../../data/extractError';

interface IVetsPageState {
  error?: IError;
  vets?: Vet[];
}

class VetsPage extends React.Component<RouteComponentProps, IVetsPageState> {
  async componentDidMount() {
    try {
      const vets = await new VetApi().listVets();

      console.log('vets', vets);
      this.setState({ vets });
    } catch (response) {
      const error = await extractError(response);
      this.setState({ error });
    }
  }

  render() {
    const { error, vets = [] } = this.state || {};

    if (error) {
      return <PageErrorMessage error={error} />;
    }

    if (!vets) {
      return <h2>Veterinarians</h2>;
    }

    return (
      <div>
        <h2>Veterinarians</h2>
        <table className="table table-striped">
          <thead>
            <tr>
              <th>Name</th>
              <th>Specialties</th>
            </tr>
          </thead>
          <tbody>
            {vets.map(vet => (
              <tr key={vet.id}>
                <td>
                  {vet.firstName} {vet.lastName}
                </td>
                <td>
                  {vet.specialties.length > 0
                    ? vet.specialties
                        .map(specialty => specialty.name)
                        .join(', ')
                    : 'none'}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    );
  }
}

export default withRouter(VetsPage);
