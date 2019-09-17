import * as React from 'react';

import DatePicker from 'react-datepicker';

import { connect, ErrorMessage, Field } from 'formik';

const DateInput = ({ name, label, ...rest }) => {
  const valid = !rest.formik.errors[name];
  const touched = !!rest.formik.touched[name];

  const warn = touched && !valid;
  const cssGroup = `form-group ${warn ? 'has-error' : ''}`;
  const icon = warn ? 'glyphicon-remove' : 'glyphicon-ok';

  return (
    <div className={cssGroup}>
      <label htmlFor={name} className="col-sm-2 control-label">
        {label}
      </label>

      <div className="col-sm-10">
        <Field
          name={name}
          render={({ field }) => (
            <DatePicker
              id={field.name}
              name={field.name}
              selected={field.value}
              onChange={date => rest.formik.setFieldValue(field.name, date)}
              onBlur={field.onBlur}
              className="form-control"
              dateFormat="yyyy-MM-dd"
            />
          )}
        />

        <span
          className={`glyphicon ${icon} form-control-feedback`}
          aria-hidden="true"
        ></span>

        <ErrorMessage name={name} component="span" className="help-inline" />
      </div>
    </div>
  );
};

export default connect(DateInput);
