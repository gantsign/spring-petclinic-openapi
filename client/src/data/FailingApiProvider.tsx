import * as React from 'react';
import { FailingApi } from 'petclinic-api';
import { Subtract } from 'utility-types';

export interface WithFailingApiProps {
  failingApi: FailingApi;
}

export const FailingApiContext = React.createContext(new FailingApi());

export const withFailingApi = <P extends WithFailingApiProps>(
  Component: React.ComponentType<P>
) =>
  class WithFailingApi extends React.Component<
    Subtract<P, WithFailingApiProps>
  > {
    static contextType = FailingApiContext;
    context!: React.ContextType<typeof FailingApiContext>;

    render() {
      return <Component failingApi={this.context} {...(this.props as P)} />;
    }
  };
