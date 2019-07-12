import React, { PureComponent } from 'react';
import { ContentPro as Content } from '@choerodon/boot';
import { DataSet, Table, TextField } from 'choerodon-ui/pro';
import ApproveCandidateRuleDS from './stores/ApproveCandidateRuleDS';


const { Column } = Table;
const textField = <TextField />;

export default class Index extends PureComponent {
  approveCandidateRuleDS = new DataSet(ApproveCandidateRuleDS);

  render() {
    return (
      <Content>
        <Table
          buttons={['add', 'save', 'delete']}
          dataSet={this.approveCandidateRuleDS}
          queryFieldsLimit={2}
        >
          <Column name="code" editor />
          <Column name="description" editor />
          <Column name="expression" editor />
          <Column name="enableFlag" editor width={140} />
        </Table>
      </Content>
    );
  }
}
