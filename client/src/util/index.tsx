declare var __API_SERVER_URL__;
const BACKEND_URL =
  typeof __API_SERVER_URL__ === 'undefined'
    ? 'http://localhost:8080'
    : __API_SERVER_URL__;

export const url = (path: string): string => `${BACKEND_URL}/${path}`;
