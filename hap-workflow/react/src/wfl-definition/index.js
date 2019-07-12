import React, { PureComponent } from 'react';
import { ContentPro as Content } from '@choerodon/boot';
import { Button, Modal, Table, DataSet, Tooltip } from 'choerodon-ui/pro';
import { ProcessDefinitionDataSet, DeploymentDataSet } from './stores';
import Definition from './view/Definition';

export default class Index extends PureComponent {
  processDefinitionDataSet = new DataSet(ProcessDefinitionDataSet);

  deploymentDataSet = new DataSet(DeploymentDataSet);

  render() {
    return (
      <Content>
        <Definition processDefinition={this.processDefinitionDataSet} deployment={this.deploymentDataSet} />
      </Content>
    );
  }
}
