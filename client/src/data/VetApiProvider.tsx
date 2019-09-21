import * as React from 'react';
import { VetApi } from 'petclinic-api';
import { Subtract } from 'utility-types';

export interface WithVetApiProps {
  vetApi: VetApi;
}

export const VetApiContext = React.createContext(new VetApi());

export const withVetApi = <P extends WithVetApiProps>(
  Component: React.ComponentType<P>
) =>
  class WithVetApi extends React.Component<Subtract<P, WithVetApiProps>> {
    static contextType = VetApiContext;
    context!: React.ContextType<typeof VetApiContext>;

    render() {
      return <Component vetApi={this.context} {...(this.props as P)} />;
    }
  };
