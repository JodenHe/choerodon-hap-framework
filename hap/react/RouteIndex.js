import React, { Component } from 'react';
import CacheRoute, { CacheSwitch } from 'react-router-cache-route';
import { nomatch } from '@choerodon/boot';

export default class RouteIndex extends Component {
  render() {
    return (
      <CacheSwitch>
        <CacheRoute path="*" component={nomatch} />
      </CacheSwitch>
    );
  }
}
