import React, { PureComponent } from 'react';
import { ContentPro as Content } from '@choerodon/boot';
import { DataSet } from 'choerodon-ui/pro';
import { ModelRequestDataSet, ModelDataSet } from './stores';
import Model from './view/Model';

export default class Index extends PureComponent {
  modelRequestDS = new DataSet(ModelRequestDataSet);

  modelDS = new DataSet(ModelDataSet);

  render() {
    return (
      <Content>
        <Model modelRequest={this.modelRequestDS} modelDataSet={this.modelDS} />
      </Content>
    );
  }
}
