import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';

import { Vet, VetApi } from 'petclinic-api';

interface IVetsPageState {
  vets: Vet[];
}

class VetsPage extends React.Component<RouteComponentProps, IVetsPageState> {
  constructor(props) {
    super(props);

    this.state = { vets: [] };
  }

  async componentDidMount() {
    const vets = await new VetApi().listVets();

    console.log('vets', vets);
    this.setState({ vets });
  }

  render() {
    const { vets } = this.state;

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
