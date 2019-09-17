import * as React from 'react';

import { connect, ErrorMessage, Field } from 'formik';

const Input = ({ name, label, ...rest }) => {
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
        <Field id={name} name={name} className="form-control" {...rest} />

        <span
          className={`glyphicon ${icon} form-control-feedback`}
          aria-hidden="true"
        ></span>

        <ErrorMessage name={name} component="span" className="help-inline" />
      </div>
    </div>
  );
};

export default connect(Input);
