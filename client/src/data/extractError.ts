import { IError } from '../types';

const extractError = (response): Promise<IError> => {
  if (response.json) {
    return response.json();
  } else {
    return Promise.resolve({ message: `${response}` });
  }
};

export default extractError;
