import * as React from 'react';
import { OwnerApi } from 'petclinic-api';
import { Subtract } from 'utility-types';

export interface WithOwnerApiProps {
  ownerApi: OwnerApi;
}

export const OwnerApiContext = React.createContext(new OwnerApi());

export const withOwnerApi = <P extends WithOwnerApiProps>(
  Component: React.ComponentType<P>
) =>
  class WithOwnerApi extends React.Component<Subtract<P, WithOwnerApiProps>> {
    static contextType = OwnerApiContext;
    context!: React.ContextType<typeof OwnerApiContext>;

    render() {
      return <Component ownerApi={this.context} {...(this.props as P)} />;
    }
  };
