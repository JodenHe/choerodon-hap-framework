import React, { Component } from 'react';
import CacheRoute, { CacheSwitch } from 'react-router-cache-route';
import { asyncRouter, nomatch, createRouteWrapper } from '@choerodon/boot';

const ModelingData = asyncRouter(() => import('./src/modeling/data'));
const ModelingHistory = asyncRouter(() => import('./src/modeling/history'));
const ModelingRelation = asyncRouter(() => import('./src/modeling/relation'));
const ModelingDataSet = asyncRouter(() => import('./src/modeling/dataset'));
const ModelingPages = asyncRouter(() => import('./src/modeling/pages'));
const ModelingPagesDataSet = asyncRouter(() => import('./src/modeling/pages/dataset'));
const ModelingPagesLayout = asyncRouter(() => import('./src/modeling/pages/layout/test'));
const MetadataItem = asyncRouter(() => import('./src/modeling/pages/MetadataItem'));
const MetadataItemPage = asyncRouter(() => import('./src/modeling/metadata'));
const Metadata = createRouteWrapper('hap-modeling/metadata', MetadataItemPage);

export default ({ match }) => (
  <CacheSwitch>
    <CacheRoute exact path={`${match.url}/modeling/data`} cacheKey={`${match.url}/modeling/data`} component={ModelingData} />
    <CacheRoute exact path={`${match.url}/modeling/history`} cacheKey={`${match.url}/modeling/history`} component={ModelingHistory} />
    <CacheRoute exact path={`${match.url}/modeling/relation`} cacheKey={`${match.url}/modeling/relation`} component={ModelingRelation} />
    <CacheRoute exact path={`${match.url}/modeling/dataset`} cacheKey={`${match.url}/modeling/dataset`} component={ModelingDataSet} />
    <CacheRoute exact path={`${match.url}/modeling/pages`} cacheKey={`${match.url}/modeling/pages`} component={ModelingPages} />
    <CacheRoute exact path={`${match.url}/modeling/metadataitem/:code`} cacheKey={`${match.url}/modeling/metadataitem`} component={MetadataItem} />
    <CacheRoute exact path={`${match.url}/metadata/:code`} cacheKey={`${match.url}/metadata`} component={Metadata} />
    <CacheRoute
      exact
      path={`${match.url}/modeling/pages/dataset/:name`}
      cacheKey={`${match.url}/modeling/pages/dataset/:name`}
      component={ModelingPagesDataSet}
    />
    <CacheRoute exact path={`${match.url}/modeling/pages/layout`} cacheKey={`${match.url}/modeling/pages/layout`} component={ModelingPagesLayout} />
    <CacheRoute path="*" component={nomatch} />
  </CacheSwitch>
);
