import React, { Component } from 'react';
import { observer } from 'mobx-react';
import { Button, Modal, Lov, Select, Table, TextField, Tooltip, DataSet } from 'choerodon-ui/pro';
import { Content, $l } from '@choerodon/boot';
import TableDataSet from './stores/TableDataSet';
import RuleEditDataSet from './stores/RuleEditDataSet';

const { Column } = Table;

function insertSelectEditor(record) {
  return record.status === 'add' ? <Select /> : null;
}

const ruleEditModalKey = Modal.key();

@observer
export default class Index extends Component {
  tableDS = new DataSet(TableDataSet);

  ruleDS = new DataSet(RuleEditDataSet);

  ruleEditModal;

  handleRuleEditSave(tableRecord) {
    const { tableId, tableName } = tableRecord.data;
    this.ruleDS.data.forEach((record) => {
      record.set('tableId', tableId);
      record.set('tableName', tableName);
    });
    this.ruleDS.submit();
  }

  async openRuleEditModal(tableRecord) {
    const { tableId, tableName } = tableRecord.data;
    this.ruleDS.setQueryParameter('tableId', tableId);
    await this.ruleDS.query();
    this.ruleDS.getField('tableField').set('lookupUrl', `/fnd/flex/column/queryAllObj?tableName=${tableName}`);
    await this.ruleDS.getField('tableField').fetchLookup();

    this.ruleEditModal = Modal.open({
      drawer: true,
      key: ruleEditModalKey,
      title: $l('datapermission.assignrule', '分配屏蔽规则'),
      footer: (
        <Button color="blue" onClick={() => this.ruleEditModal.close()}>{$l('hap.close')}</Button>
      ),
      children: (
        <Table
          dataSet={this.ruleDS}
          buttons={[
            'add',
            <Button funcType="flat" icon="save" color="blue" onClick={() => this.handleRuleEditSave(tableRecord)}>{$l('hap.save')}</Button>,
            'delete',
          ]}
        >
          <Column name="rule" editor={<Lov />} />
          <Column name="tableField" editor={<Select />} />
        </Table>
      ),
    });
  }

  render() {
    return (
      <Content>
        <Table dataSet={this.tableDS} buttons={['add', 'save', 'delete']} queryFieldsLimit={2}>
          <Column name="tableName" editor={insertSelectEditor} />
          <Column name="description" editor={<TextField />} />
          <Column
            header={$l('datapermission.assignrule', '分配屏蔽规则')}
            align="center"
            width={140}
            renderer={({ record }) => {
              if (record.status !== 'add') {
                return (
                  <Tooltip title={$l('datapermission.assignrule', '分配屏蔽规则')}>
                    <Button
                      funcType="flat"
                      icon="mode_edit"
                      color="blue"
                      onClick={() => this.openRuleEditModal(record)}
                      disabled={record.status === 'add'}
                    />
                  </Tooltip>
                );
              }
            }}
          />
        </Table>
      </Content>
    );
  }
}
