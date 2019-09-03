import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';

import { Owner, OwnerApi } from 'petclinic-api';

import OwnerInformation from './OwnerInformation';
import PetsTable from './PetsTable';

interface IOwnersPageProps extends RouteComponentProps {}

interface IOwnerPageState {
  owner?: Owner;
}

class OwnersPage extends React.Component<IOwnersPageProps, IOwnerPageState> {
  constructor(props) {
    super(props);

    this.state = {};
  }

  componentDidMount() {
    const ownerId = Number(this.props.match.params['ownerId']);

    new OwnerApi()
      .getOwner({ ownerId })
      .then(owner => this.setState({ owner }));
  }

  render() {
    const { owner } = this.state;

    if (!owner) {
      return <h2>No Owner loaded</h2>;
    }

    return (
      <div>
        <OwnerInformation owner={owner} />
        <PetsTable owner={owner} />
      </div>
    );
  }
}

export default withRouter(OwnersPage);
