import * as React from 'react';
import { IError } from '../types';

const PageErrorMessage: React.FunctionComponent<{ error?: IError }> = ({
  error,
}) => {
  if (!error) {
    return <></>;
  }
  return (
    <div role="alert" className="alert alert-warning">
      <strong>Error!</strong> {error.message}
    </div>
  );
};

export default PageErrorMessage;
