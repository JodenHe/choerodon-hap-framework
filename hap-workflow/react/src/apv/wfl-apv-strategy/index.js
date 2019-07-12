import React, { PureComponent } from 'react';
import { ContentPro as Content } from '@choerodon/boot';
import { DataSet, Table } from 'choerodon-ui/pro';
import ApvStrategyDS from './stores/ApvStrategyDS';


const { Column } = Table;

export default class Index extends PureComponent {
  apvStrategyDS = new DataSet(ApvStrategyDS);

  render() {
    return (
      <Content>
        <Table
          buttons={['add', 'save', 'delete']}
          dataSet={this.apvStrategyDS}
          queryFieldsLimit={2}
        >
          <Column name="code" editor />
          <Column name="description" editor />
          <Column name="condition" editor />
          <Column name="enableFlag" editor width={140} />
        </Table>
      </Content>
    );
  }
}
