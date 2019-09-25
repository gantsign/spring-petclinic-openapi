import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';

import { Vet } from 'petclinic-api';
import { withVetApi, WithVetApiProps } from '../../data/VetApiProvider';

import { IError } from '../../types';
import PageErrorMessage from '../PageErrorMessage';
import extractError from '../../data/extractError';

import Loading from '../Loading';

interface IVetsPageState {
  error?: IError;
  vets?: Vet[];
}

interface IVetsPageProps extends RouteComponentProps, WithVetApiProps {}

class VetsPage extends React.Component<IVetsPageProps, IVetsPageState> {
  async componentDidMount() {
    try {
      const vets = await this.props.vetApi.listVets({});

      console.log('vets', vets);
      this.setState({ vets });
    } catch (response) {
      const error = await extractError(response);
      this.setState({ error });
    }
  }

  render() {
    const { error, vets } = this.state || {};

    if (error) {
      return <PageErrorMessage error={error} />;
    }

    if (!vets) {
      return <Loading />;
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

export default withVetApi(withRouter(VetsPage));
