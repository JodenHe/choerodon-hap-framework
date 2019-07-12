import React, { Component } from 'react';
import CacheRoute, { CacheSwitch } from 'react-router-cache-route';
import { asyncRouter, nomatch } from '@choerodon/boot';

const Task = asyncRouter(() => import('./src/activiti/wfl-task'));
const AutoDelegateConfig = asyncRouter(() => import('./src/activiti/auto-delegate-config'));
const ApvStrategy = asyncRouter(() => import('./src/apv/wfl-apv-strategy'));
const ApvRule = asyncRouter(() => import('./src/apv/wfl-apv-rule'));
const ApvType = asyncRouter(() => import('./src/apv/wfl-apv-type'));
const Model = asyncRouter(() => import('./src/wfl-model'));
const Definition = asyncRouter(() => import('./src/wfl-definition'));
const Test = asyncRouter(() => import('./src/wfl-process_test'));
const Start = asyncRouter(() => import('./src/wfl-my-start'));
const History = asyncRouter(() => import('./src/wfl-history'));
const Carbon = asyncRouter(() => import('./src/wfl-carbon'));
const Monitor = asyncRouter(() => import('./src/monitor/wfl_monitor'));
const RuntimeException = asyncRouter(() => import('./src/wfl-runtime-exception'));
const MyTask = asyncRouter(() => import('./src/wfl-my-task'));
const VacationTest = asyncRouter(() => import('./src/wfl-vacation-test'));

export default class RouteIndex extends Component {
  render() {
    const { match } = this.props;
    return (
      <CacheSwitch>
        <CacheRoute exact path={`${match.url}/wfl-apv-strategy`} cacheKey={`${match.url}/wfl-apv-strategy`} component={ApvStrategy} />
        <CacheRoute exact path={`${match.url}/wfl-apv-rule`} cacheKey={`${match.url}/wfl-apv-rule`} component={ApvRule} />
        <CacheRoute exact path={`${match.url}/wfl-apv-type`} cacheKey={`${match.url}/wfl-apv-type`} component={ApvType} />
        <CacheRoute exact path={`${match.url}/wfl-model`} cacheKey={`${match.url}/wfl-model`} component={Model} />
        <CacheRoute exact path={`${match.url}/wfl-task`} cacheKey={`${match.url}/wfl-task`} component={Task} />
        <CacheRoute exact path={`${match.url}/wfl-auto-delegate`} cacheKey={`${match.url}/wfl-auto-delegate`} component={AutoDelegateConfig} />
        <CacheRoute exact path={`${match.url}/wfl-test`} cacheKey={`${match.url}/wfl-test`} component={Test} />
        <CacheRoute exact path={`${match.url}/wfl-definition`} cacheKey={`${match.url}/wfl-definition`} component={Definition} />
        <CacheRoute exact path={`${match.url}/wfl-my-start`} cacheKey={`${match.url}/wfl-my-start`} component={Start} />
        <CacheRoute exact path={`${match.url}/wfl-history`} cacheKey={`${match.url}/wfl-history`} component={History} />
        <CacheRoute exact path={`${match.url}/wfl-carbon`} cacheKey={`${match.url}/wfl-carbon`} component={Carbon} />
        <CacheRoute exact path={`${match.url}/wfl-monitor`} cacheKey={`${match.url}/wfl-monitor`} component={Monitor} />
        <CacheRoute exact path={`${match.url}/wfl-runtime-exception`} cacheKey={`${match.url}/wfl-runtime-exception`} component={RuntimeException} />
        <CacheRoute exact path={`${match.url}/wfl-my-task`} cacheKey={`${match.url}/wfl-my-task`} component={MyTask} />
        <CacheRoute exact path={`${match.url}/wfl-vacation-test`} cacheKey={`${match.url}/wfl-vacation-test`} component={VacationTest} />
        <CacheRoute path="*" component={nomatch} />
      </CacheSwitch>
    );
  }
}
