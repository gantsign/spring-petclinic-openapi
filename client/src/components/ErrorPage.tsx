import * as React from 'react';
import {
  withFailingApi,
  WithFailingApiProps,
} from '../data/FailingApiProvider';

import { IError } from '../types';
import PageErrorMessage from './PageErrorMessage';
import extractError from '../data/extractError';

import Loading from './Loading';

interface IErrorPageState {
  error?: IError;
}

interface IErrorPageProps extends WithFailingApiProps {}

class ErrorPage extends React.Component<IErrorPageProps, IErrorPageState> {
  async componentDidMount() {
    try {
      await this.props.failingApi.failingRequest();
    } catch (response) {
      const error = await extractError(response);
      this.setState({ error });
    }
  }

  render() {
    const { error } = this.state || {};

    if (!error) {
      return <Loading />;
    }

    return (
      <div>
        <PageErrorMessage error={error} />

        <img src="/images/pets.png" alt="" />
      </div>
    );
  }
}

export default withFailingApi(ErrorPage);
