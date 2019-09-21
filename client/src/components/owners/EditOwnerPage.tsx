import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';

import OwnerEditor from './OwnerEditor';

import { Owner, OwnerApi } from 'petclinic-api';

import { IError } from '../../types';
import PageErrorMessage from '../PageErrorMessage';
import extractError from '../../data/extractError';

interface IEditOwnerPageProps extends RouteComponentProps {}

interface IEditOwnerPageState {
  error?: IError;
  owner?: Owner;
}

class EditOwnerPage extends React.Component<
  IEditOwnerPageProps,
  IEditOwnerPageState
> {
  async componentDidMount() {
    const ownerId = Number(this.props.match.params['ownerId']);

    try {
      const owner = await new OwnerApi().getOwner({ ownerId });
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
      return <div>Loading...</div>;
    }

    return <OwnerEditor initialOwner={owner} />;
  }
}

export default withRouter(EditOwnerPage);
