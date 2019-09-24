import * as React from 'react';

interface ILoadingProps {}

interface ILoadingState {
  timer;
  show: boolean;
}

export default class Loading extends React.Component<
  ILoadingProps,
  ILoadingState
> {
  componentDidMount() {
    const timer = setTimeout(() => {
      const { timer } = this.state;
      this.setState({
        timer,
        show: true,
      });
    }, 500);
    this.setState({
      timer,
      show: false,
    });
  }

  componentWillUnmount() {
    clearTimeout(this.state.timer);
  }

  render() {
    const { show } = this.state || {};
    if (!show) {
      return <></>;
    }
    return (
      <div role="alert" className="alert alert-info">
        Loading...
      </div>
    );
  }
}
