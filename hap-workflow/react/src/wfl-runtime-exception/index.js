import React, { Component } from 'react';
import { ContentPro as Content } from '@choerodon/boot';
import { DataSet } from 'choerodon-ui/pro';
import Log from './view/Log';
import DSConfig from './stores/RuntimeExceptionStore';

export default class Index extends Component {
  ds = new DataSet(DSConfig);

  render() {
    return (
      <Content>
        <Log ds={this.ds} />
      </Content>
    );
  }
}
