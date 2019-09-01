import * as React from 'react';
import * as Enzyme from 'enzyme';
import * as Adapter from 'enzyme-adapter-react-15';

import { NotEmpty } from '../../../../src/components/form/Constraints';
import FieldFeedbackPanel from '../../../../src/components/form/FieldFeedbackPanel';
import { IInputChangeHandler, IError, IConstraint } from '../../../../src/types';

import Input from '../../../../src/components/form/Input';

const shallow = Enzyme.shallow;
Enzyme.configure({adapter: new Adapter()});

describe('Input', () => {

  const onChange: IInputChangeHandler = (name, value, error) => {
    onChangeResult = { name, value, error };
  };

  let object: any | null = null;
  let onChangeResult: any | null = null;

  beforeEach(() => {
    object = {
      myField: 'blabla'
    };

    onChangeResult = null;
  });

  it('should render correctly without field error', () => {
    const error = {
      fieldErrors: {}
    };

    const input = shallow(<Input object={object}
      label='My Field'
      name='myField'
      error={error}
      onChange={onChange}
      />);

    // Make sure label is rendered correctly
    expect(input.find('.control-label').text()).toBe('My Field');

    // Make sure input field's value is correct
    expect(input.find('input').props().value).toBe('blabla');

    // we don't have any errors
    expect(input.find('.has-error').length).toBe(0);
    expect(input.find(FieldFeedbackPanel).props().valid).toBe(true);

    // change to new value
    input.find('input').simulate('change', { target: { value: 'My new value' } });

    // make sure callback is called
    expect(onChangeResult).toBeTruthy();
    expect(onChangeResult.name).toBe('myField');
    expect(onChangeResult.value).toBe('My new value');
    expect(onChangeResult.error).toBeFalsy();
  });

  it('should render correctly with field error', () => {

    const error = {
      fieldErrors: {
        myField: {
          field: 'myField',
          message: 'There was an error'
        }
      }
    };

    const input = shallow(<Input object={object}
      label='My Field'
      name='myField'
      error={error}
      onChange={onChange}
      />);

    // Make sure label is rendered correctly
    expect(input.find('.control-label').text()).toBe('My Field');

    // Make sure input field's value is correct
    expect(input.find('input').props().value).toBe('blabla');

    // we don't have any errors
    expect(input.find('.has-error').length).toBe(1);
    expect(input.find(FieldFeedbackPanel).props().valid).toBe(false);
    expect(input.find(FieldFeedbackPanel).props().fieldError).toBe(error.fieldErrors.myField);
  });

  it('should checked constrains on input change', () => {

    const error = {
      fieldErrors: {}
    };

    const constraint: IConstraint = {
      message: 'Invalid',
      validate: jest.fn()
    };

    const input = shallow(<Input object={object}
      label='My Field'
      name='myField'
      error={error}
      onChange={onChange}
      constraint={constraint as any}
      />);

      input.find('input').simulate('change', { target: { value: 'My new value' } });
      expect(constraint.validate).toHaveBeenCalledWith('My new value');
  });
});
