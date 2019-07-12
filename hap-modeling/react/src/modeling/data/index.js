import React, { Component } from 'react';
import { Button, CheckBox, DataSet, IntlField, Modal, Select, Table, TextField, Tooltip } from 'choerodon-ui/pro';
import { ContentPro as Content } from '@choerodon/boot';
import TableDataSet from './stores/TableDataSet';
import ColumnDataSet from './stores/ColumnDataSet';

const { Column } = Table;

function insertColumnNameEditor(record) {
  return record.status === 'add' ? <TextField prefix="X_" /> : null;
}

function insertTableNameEditor(record) {
  return record.status === 'add' ? <TextField /> : null;
}

function insertColumnNameRender({ record }) {
  const text = record.get('columnName') || '';
  return record.status === 'add' ? `X_${text}` : text;
}

function insertTableNameRender({ record }) {
  const text = record.get('tableName') || '';
  if (record.status === 'add') {
    if (record.get('multiLanguage')) {
      return `x_${text}_b`;
    } else {
      return `x_${text}`;
    }
  }
  return text;
}

function insertSelectEditor(record) {
  return record.status === 'add' ? <Select /> : null;
}

export default class Data extends Component {
  tableDataSet = new DataSet(TableDataSet);

  columnDataSet = new DataSet(ColumnDataSet);

  columnModalKey = Modal.key();

  saveColumnTable() {
    const { columnDataSet, tableDataSet } = this;
    columnDataSet.created.forEach((record) => {
      record.set('tableName', tableDataSet.current.get('tableName'));
    });
    columnDataSet.submit();
  }

  openColumnModal(tableRecord) {
    const { columnDataSet } = this;
    columnDataSet.queryParameter.tableName = tableRecord.get('tableName');
    columnDataSet.query();

    Modal.open({
      drawer: true,
      key: this.columnModalKey,
      maskClosable: false,
      title: `修改字段 [${tableRecord.get('tableName')}]`,
      children: (
        <Table
          dataSet={columnDataSet}
          buttons={['add',
            <Button funcType="flat" icon="delete" onClick={this.saveColumnTable} color="blue">保存</Button>]}
          selectionMode="false"
        >
          <Column name="columnName" editor={insertColumnNameEditor} renderer={insertColumnNameRender} width={200} />
          <Column name="displayType" editor={insertSelectEditor} />
          <Column name="description" editor={<IntlField clearButton />} />
          <Column name="nullable" editor={record => <CheckBox value disabled={record.status !== 'add'} />} align="center" />
          <Column
            name="multiLanguage"
            editor={record => <CheckBox value disabled={record.status !== 'add' || tableRecord.get('multiLanguage') !== true} />}
            align="center"
          />
          <Column
            header="删除"
            align="center"
            renderer={({ record }) => (
              <Button
                funcType="flat"
                icon="delete"
                color="blue"
                onClick={() => columnDataSet.delete(record)}
                disabled={record.status === 'add' || !record.get('columnName').startsWith('X_')}
              />
            )}
          />
        </Table>
      ),
      style: { width: 760 },
    });
  }

  render() {
    const { tableDataSet } = this;
    return (
      <Content>
        <Table dataSet={tableDataSet} buttons={['add', 'save']} selectionMode="false">
          <Column name="tableName" editor={insertTableNameEditor} renderer={insertTableNameRender} />
          <Column name="multiLanguage" editor={record => (record.status === 'add' ? <CheckBox /> : null)} align="center" />
          <Column name="lockedBy" />
          <Column name="description" editor={<IntlField clearButton />} />
          <Column
            header="操作"
            align="center"
            width={100}
            lock="right"
            renderer={({ record }) => [
              <Tooltip title="编辑">
                <Button
                  funcType="flat"
                  color="blue"
                  icon="mode_edit"
                  disabled={record.status === 'add'}
                  onClick={this.openColumnModal.bind(this, record)}
                />
              </Tooltip>,
              <Tooltip title="删除">
                <Button
                  funcType="flat"
                  color="blue"
                  icon="delete"
                  onClick={() => tableDataSet.delete(record)}
                  disabled={record.status === 'add' || !record.get('tableName').startsWith('x_')}
                />
              </Tooltip>,
            ]}
          />
        </Table>
      </Content>
    );
  }
}
