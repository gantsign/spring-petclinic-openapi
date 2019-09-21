import * as React from 'react';
import { VisitApi } from 'petclinic-api';
import { Subtract } from 'utility-types';

export interface WithVisitApiProps {
  visitApi: VisitApi;
}

export const VisitApiContext = React.createContext(new VisitApi());

export const withVisitApi = <P extends WithVisitApiProps>(
  Component: React.ComponentType<P>
) =>
  class WithVisitApi extends React.Component<Subtract<P, WithVisitApiProps>> {
    static contextType = VisitApiContext;
    context!: React.ContextType<typeof VisitApiContext>;

    render() {
      return <Component visitApi={this.context} {...(this.props as P)} />;
    }
  };
