import * as React from 'react';
import { PetApi } from 'petclinic-api';
import { Subtract } from 'utility-types';

export interface WithPetApiProps {
  petApi: PetApi;
}

export const PetApiContext = React.createContext(new PetApi());

export const withPetApi = <P extends WithPetApiProps>(
  Component: React.ComponentType<P>
) =>
  class WithPetApi extends React.Component<Subtract<P, WithPetApiProps>> {
    static contextType = PetApiContext;
    context!: React.ContextType<typeof PetApiContext>;

    render() {
      return <Component petApi={this.context} {...(this.props as P)} />;
    }
  };
