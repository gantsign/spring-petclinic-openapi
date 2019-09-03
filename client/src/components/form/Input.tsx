import * as React from 'react';

import { IConstraint, IError, IInputChangeHandler } from '../../types';

import FieldFeedbackPanel from './FieldFeedbackPanel';

const NoConstraint: IConstraint = {
  message: '',
  validate: v => true,
};

export default ({
  object,
  error,
  name,
  constraint = NoConstraint,
  label,
  onChange,
}: {
  object: any;
  error?: IError;
  name: string;
  constraint?: IConstraint;
  label: string;
  onChange: IInputChangeHandler;
}) => {
  const handleOnChange = event => {
    const { value } = event.target;

    // run validation (if any)
    const fieldError =
      constraint.validate(value) === false
        ? { field: name, message: constraint.message }
        : undefined;

    // invoke callback
    onChange(name, value, fieldError);
  };

  const value = object[name];
  const fieldError = error && error.fieldErrors && error.fieldErrors[name];
  const valid = !fieldError && value !== null && value !== undefined;

  const cssGroup = `form-group ${fieldError ? 'has-error' : ''}`;

  return (
    <div className={cssGroup}>
      <label className="col-sm-2 control-label">{label}</label>

      <div className="col-sm-10">
        <input
          type="text"
          name={name}
          className="form-control"
          value={value}
          onChange={handleOnChange}
        />

        <FieldFeedbackPanel valid={valid} fieldError={fieldError} />
      </div>
    </div>
  );
};
