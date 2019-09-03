// ------------------------------------ ERROR ------------------------------------
export interface IFieldError {
  field: string;
  message: string;
}

export interface IFieldErrors {
  [index: string]: IFieldError;
}

export interface IError {
  fieldErrors: IFieldErrors;
}

// ------------------------------------ FORM --------------------------------------
export interface IConstraint {
  message: string;
  validate: (value: any) => boolean;
}

export type IInputChangeHandler = (
  name: string,
  value: string,
  error?: IFieldError
) => void;

export interface ISelectOption {
  value: string | number;
  name: string;
}

// ------------------------------------ MODEL .------------------------------------

export type IPetTypeId = number;

export interface IEditablePet {
  name: string;
  birthDate?: string;
  typeId?: IPetTypeId;
}

export interface IEditableVisit {
  date: string;
  description: string;
}
