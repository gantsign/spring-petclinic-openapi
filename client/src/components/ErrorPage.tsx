import * as React from 'react';

interface IErrorPageState {
  error?: {
    error: string;
    message: string;
  };
}

export default class ErrorPage extends React.Component<void, IErrorPageState> {
  constructor() {
    super();
    this.state = {};
  }

  componentDidMount() {
    fetch('http://localhost:8080/api/oops')
      .then(response => response.json())
      .then(error => this.setState({error}));
  }

  render() {
    const { error } = this.state;

    return <span>
      <img src='/images/pets.png' />

      <h2>Something happened...</h2>
      { error ?
        <span>
          <p><b>Status:</b> {error.error}</p>
          <p><b>Message:</b> {error.message}</p>
        </span>
        :
        <p><b>Unknown error</b></p>
      }
    </span>;
  }
};
