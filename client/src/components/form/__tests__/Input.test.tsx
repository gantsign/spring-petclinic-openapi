import * as React from 'react';

import { Form, Formik } from 'formik';
import * as Yup from 'yup';

import '@testing-library/jest-dom/extend-expect';
import { fireEvent, render, wait } from '@testing-library/react';

import Input from '../Input';

describe('Input', () => {
  it('should render correctly without field error', async () => {
    const result = { values: {} };
    const onSubmit = values => {
      result.values = values;
    };

    const { getByLabelText, getByText, container } = render(
      <Formik initialValues={{ myField: 'blabla' }} onSubmit={onSubmit}>
        <Form id="form">
          <Input name="myField" label="My Field" />
          <button type="submit">Submit</button>
        </Form>
      </Formik>
    );

    const input = getByLabelText('My Field') as HTMLInputElement;

    // Make sure input field's value is correct
    expect(input.value).toBe('blabla');

    // We don't have any errors
    const formGroup = container.getElementsByClassName('form-group')[0];
    expect(formGroup).not.toHaveClass('has-error');
    expect(container.getElementsByClassName('help-inline').length).toBe(0);

    // Change to new value
    fireEvent.change(input, { target: { value: 'My new value' } });
    expect(input.value).toBe('My new value');
    fireEvent(
      getByText('Submit'),
      new MouseEvent('click', {
        bubbles: true,
        cancelable: true,
      })
    );

    // Make sure value is changed
    await wait();
    expect(result.values['myField']).toBe('My new value');
  });

  it('should render correctly with field error', async () => {
    const result = { values: {} };
    const onSubmit = values => {
      result.values = values;
    };

    const { getByLabelText, getByText, container } = render(
      <Formik
        initialValues={{ myField: '' }}
        onSubmit={onSubmit}
        validationSchema={Yup.object().shape({
          myField: Yup.string().required('Required'),
        })}
      >
        <Form id="form">
          <Input name="myField" label="My Field" />
          <button type="submit">Submit</button>
        </Form>
      </Formik>
    );

    const input = getByLabelText('My Field') as HTMLInputElement;

    // Make sure input field's value is correct
    expect(input.value).toBe('');

    // Submit form
    fireEvent(
      getByText('Submit'),
      new MouseEvent('click', {
        bubbles: true,
        cancelable: true,
      })
    );

    // Check for errors
    await wait();

    const formGroup = container.getElementsByClassName('form-group')[0];
    expect(formGroup).toHaveClass('has-error');

    const errorContainer = container.getElementsByClassName('help-inline')[0];
    expect(errorContainer.innerHTML).toBe('Required');
  });
});
