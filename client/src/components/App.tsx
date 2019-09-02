import * as React from 'react';

import { RouteComponentProps, withRouter } from 'react-router-dom';

import Menu from './Menu';

interface IAppProps extends RouteComponentProps {
  children: React.ReactNode;
}

interface IAppState {}

class App extends React.Component<IAppProps, IAppState> {
  render() {
    return (
      <div>
        <Menu name={this.props.location.pathname} />
        <div className="container-fluid">
          <div className="container xd-container">
            {this.props.children}

            <div className="container">
              <div className="row">
                <div className="col-12 text-center">
                  <img
                    src="/images/spring-pivotal-logo.png"
                    alt="Sponsored by Pivotal"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default withRouter(App);
