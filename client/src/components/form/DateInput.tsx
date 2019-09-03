/// <reference path="../../../node_modules/@types/webpack-env/index.d.ts" />

import * as React from 'react';

import DatePicker from 'react-datepicker';
import moment from 'moment';

import { IError, IInputChangeHandler } from '../../types';

import FieldFeedbackPanel from './FieldFeedbackPanel';

export default ({
  object,
  error,
  name,
  label,
  onChange,
}: {
  object: any;
  error?: IError;
  name: string;
  label: string;
  onChange: IInputChangeHandler;
}) => {
  const handleOnChange = (value: Date) => {
    const dateString = value ? moment(value).format('YYYY-MM-DD') : '';
    onChange(name, dateString);
  };

  const selectedValue = object[name]
    ? moment(object[name], 'YYYY-MM-DD').toDate()
    : null;
  const fieldError = error && error.fieldErrors && error.fieldErrors[name];
  const valid = !fieldError && selectedValue != null;

  const cssGroup = `form-group ${fieldError ? 'has-error' : ''}`;

  return (
    <div className={cssGroup}>
      <label className="col-sm-2 control-label">{label}</label>

      <div className="col-sm-10">
        <DatePicker
          selected={selectedValue}
          onChange={handleOnChange}
          className="form-control"
          dateFormat="yyyy-MM-dd"
        />
        <span
          className="glyphicon glyphicon-ok form-control-feedback"
          aria-hidden="true"
        ></span>
        <FieldFeedbackPanel valid={valid} fieldError={fieldError} />
      </div>
    </div>
  );
};
