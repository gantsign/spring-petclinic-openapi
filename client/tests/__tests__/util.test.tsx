import { url, submitForm } from '../../src/util';

import * as React from 'react';

import { GlobalWithFetchMock } from "jest-fetch-mock";

const customGlobal: GlobalWithFetchMock = global as GlobalWithFetchMock;
customGlobal.fetch = require("jest-fetch-mock");
customGlobal.fetchMock = customGlobal.fetch;
const fetchMock: any = fetch;

describe('util', () => {
  describe('url', () => {
    it('returns url with full path', () => {
      expect(url('xxx')).toEqual('http://localhost:8080/xxx');
    });
  });

  describe('submitForm', () => {
    beforeEach(() => fetchMock.mockClear());

    it('submits all data', () => {
      fetchMock.mockResponse(JSON.stringify({ 'x': 'y' }),
      { status: 200, headers: { 'content-type': 'application/json' } });
      return submitForm('POST', '/some-enzyme', { name: 'Test' }, (status, response) => {
        // make sure request data is passed to fetch as expected 
        expect(fetchMock.mock.calls.length).toEqual(1);
        expect(fetchMock.mock.calls[0][0]).toEqual('http://localhost:8080//some-enzyme');
        expect(fetchMock.mock.calls[0][1].method).toEqual('POST');
        expect(fetchMock.mock.calls[0][1].body).toEqual(JSON.stringify({ name: 'Test' }));

        // make sure response from fetch is correctly passed to the onSuccess callback
        expect(status).toEqual(200);
        expect(response).toEqual({ 'x': 'y' });
      });
    });

    it('works with No Content (204) responses', () => {
      fetchMock.mockResponse('', { status: 204 });
      return submitForm('PUT', '/somewhere', { name: 'Test' }, (status, response) => {
        expect(fetchMock.mock.calls.length).toEqual(1);
        expect(status).toEqual(204);
        expect(response).toEqual({});
      });
    });
  });
});
