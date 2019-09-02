import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';

import OwnerEditor from './OwnerEditor';

import { IOwner } from '../../types';
import { url } from '../../util';

interface IEditOwnerPageProps extends RouteComponentProps {}

interface IEditOwnerPageState {
  owner: IOwner;
}

class EditOwnerPage extends React.Component<
  IEditOwnerPageProps,
  IEditOwnerPageState
> {
  componentDidMount() {
    const ownerId = Number(this.props.match.params['ownerId']);

    const fetchUrl = url(`/api/owner/${ownerId}`);
    fetch(fetchUrl)
      .then(response => response.json())
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
