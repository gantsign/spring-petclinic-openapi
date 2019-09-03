import * as React from 'react';

interface IErrorPageState {
  error?: {
    error: string;
    message: string;
  };
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

  componentDidMount() {
    fetch('http://localhost:8080/api/oops')
      .then(response => response.json())
      .then(error => this.setState({ error }));
  }

  render() {
    const { error } = this.state;

    return (
      <div>
        <img src="/images/pets.png" alt="" />

        <h2>Something happened...</h2>
        {error ? (
          <div>
            <p>
              <b>Status:</b> {error.error}
            </p>
            <p>
              <b>Message:</b> {error.message}
            </p>
          </div>
        ) : (
          <p>
            <b>Unknown error</b>
          </p>
        )}
      </div>
    );
  }
}
