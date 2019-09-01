import * as React from 'react';

import { IOwner } from '../../types';
import { url } from '../../util';

import OwnerInformation from './OwnerInformation';
import PetsTable from './PetsTable';

interface IOwnersPageProps {
  params?: { ownerId?: string };
}

interface IOwnerPageState {
  owner?: IOwner;
}

export default class OwnersPage extends React.Component<
  IOwnersPageProps,
  IOwnerPageState
> {
  constructor(props) {
    super(props);

    this.state = {};
  }

  componentDidMount() {
    const { params } = this.props;

    if (params && params.ownerId) {
      const fetchUrl = url(`/api/owner/${params.ownerId}`);
      fetch(fetchUrl)
        .then(response => response.json())
        .then(owner => this.setState({ owner }));
    }
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
