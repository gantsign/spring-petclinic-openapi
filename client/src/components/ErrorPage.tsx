import * as React from 'react';
import { FailingApi } from 'petclinic-api';

import { IError } from '../types';
import PageErrorMessage from './PageErrorMessage';
import extractError from '../data/extractError';

interface IErrorPageState {
  error?: IError;
}

interface IErrorPageProps {}

export default class ErrorPage extends React.Component<
  IErrorPageProps,
  IErrorPageState
> {
  constructor(props) {
    super(props);
    this.state = {};
  }

  async componentDidMount() {
    try {
      await new FailingApi().failingRequest();
    } catch (response) {
      const error = await extractError(response);
      this.setState({ error });
    }
  }

  render() {
    const { error } = this.state || {};

    return (
      <div>
        <PageErrorMessage error={error} />

        <img src="/images/pets.png" alt="" />
      </div>
    );
  }
}
