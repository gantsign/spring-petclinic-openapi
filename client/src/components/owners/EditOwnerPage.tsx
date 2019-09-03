import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';

import OwnerEditor from './OwnerEditor';

import { Owner, OwnerApi } from 'petclinic-api';

interface IEditOwnerPageProps extends RouteComponentProps {}

interface IEditOwnerPageState {
  owner: Owner;
}

class EditOwnerPage extends React.Component<
  IEditOwnerPageProps,
  IEditOwnerPageState
> {
  componentDidMount() {
    const ownerId = Number(this.props.match.params['ownerId']);

    new OwnerApi()
      .getOwner({ ownerId })
      .then(owner => this.setState({ owner }));
  }
  render() {
    const owner = this.state && this.state.owner;
    if (owner) {
      return <OwnerEditor initialOwner={owner} />;
    }
    return null;
  }
}

export default withRouter(EditOwnerPage);
