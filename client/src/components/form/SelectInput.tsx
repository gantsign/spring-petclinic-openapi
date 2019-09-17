import * as React from 'react';

import { connect, ErrorMessage, Field } from 'formik';

const SelectInput = ({ name, label, options, ...rest }) => {
  const valid = !rest.formik.errors[name];
  const touched = !!rest.formik.touched[name];

  const warn = touched && !valid;
  const cssGroup = `form-group ${warn ? 'has-error' : ''}`;

  return (
    <div className={cssGroup}>
      <label htmlFor={name} className="col-sm-2 control-label">
        {label}
      </label>

      <div className="col-sm-10">
        <Field name={name} component="select" size={5} className="form-control">
          {options.map(option => (
            <option key={option.value} value={option.value as string}>
              {option.name}
            </option>
          ))}
        </Field>

        <ErrorMessage name={name} component="span" className="help-inline" />
      </div>
    </div>
  );
};

export default connect(SelectInput);
