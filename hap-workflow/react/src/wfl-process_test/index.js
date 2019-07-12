import React, { PureComponent } from 'react';
import { observer } from 'mobx-react';
import { ContentPro as Content, axiosPro as axios, $l } from '@choerodon/boot';
import { Button, Modal, Table, DataSet, Tooltip, Form, TextField, NumberField, Select } from 'choerodon-ui/pro';
import ProcessDefinitionDataSet from './stores/ProcessDefinitionDataSet';
import ProcessInstancesDataSet from './stores/ProcessInstancesDataSet';

@observer
export default class Index extends PureComponent {
  processDefinition = new DataSet(ProcessDefinitionDataSet);

  processInstances = new DataSet(ProcessInstancesDataSet);

  baseUrl = '/wfl/runtime/process-instances?userId=';

  constructor(props) {
    super(props);
    this.state = {
      loading: false,
    };
    this.processInstances.getField('processDefinitionId').set('options', this.processDefinition);
    this.processInstances.create();
    this.queryDataSet();
  }

  queryDataSet() {
    const { processDefinition } = this;
    processDefinition.setQueryParameter('latest', 'true');
    processDefinition.setQueryParameter('size', 9999);
    processDefinition.setQueryParameter('sort', 'deploymentId');
    processDefinition.setQueryParameter('order', 'desc');
    processDefinition.query();
  }

  async handleSubmit() {
    if (await this.processInstances.current.validate()) {
      this.setState({
        loading: true,
      });
      const submitUrl = this.baseUrl + this.processInstances.current.get('userId');
      axios.post(submitUrl, this.processInstances.current.toJSONData())
        .then((response) => {
          if (response.success !== undefined && !response.success) {
            Modal.error($l('hap.error:', response.message));
          } else {
            Modal.info($l('hap.tip.success'));
          }
          this.setState({
            loading: false,
          });
        });
    }
  }

  render() {
    return (
      <Content>
        <Form dataSet={this.processInstances} style={{ width: 375 }}>
          <TextField label="模拟用户" name="userId" />
          <TextField label="业务主键" name="businessKey" />
          <div label="选择流程" style={{ whiteSpace: 'nowrap' }}>
            <Select placeholder="请选择" name="processDefinitionId" />
            <Button funcType="flat" icon="sync" onClick={() => this.queryDataSet()} style={{ marginLeft: 10 }} />
          </div>
          <Button color="blue" loading={this.state.loading} onClick={() => this.handleSubmit()}>启动流程</Button>
        </Form>
      </Content>
    );
  }
}
