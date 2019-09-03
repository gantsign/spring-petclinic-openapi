import { url } from '../index';

describe('util', () => {
  describe('url', () => {
    it('returns url with full path', () => {
      expect(url('xxx')).toEqual('http://localhost:8080/xxx');
    });
  });
});
