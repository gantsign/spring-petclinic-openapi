import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';

import { Owner } from 'petclinic-api';
import { withOwnerApi, WithOwnerApiProps } from '../../data/OwnerApiProvider';

import OwnerInformation from './OwnerInformation';
import PetsTable from './PetsTable';

import { IError } from '../../types';
import PageErrorMessage from '../PageErrorMessage';
import extractError from '../../data/extractError';

import Loading from '../Loading';

interface IOwnersPageProps extends RouteComponentProps, WithOwnerApiProps {}

interface IOwnerPageState {
  error?: IError;
  owner?: Owner;
}

class OwnersPage extends React.Component<IOwnersPageProps, IOwnerPageState> {
  async componentDidMount() {
    const ownerId = Number(this.props.match.params['ownerId']);

    try {
      const owner = await this.props.ownerApi.getOwner({ ownerId });

      this.setState({ owner });
    } catch (response) {
      const error = await extractError(response);
      this.setState({ error });
    }
  }

  render() {
    const { error, owner } = this.state || {};

    if (error) {
      return <PageErrorMessage error={error} />;
    }

    if (!owner) {
      return <Loading />;
    }

    return (
      <div>
        <OwnerInformation owner={owner} />
        <PetsTable owner={owner} />
      </div>
    );
  }
}

export default withOwnerApi(withRouter(OwnersPage));
