import React, { Component } from 'react';
import { Button, DataSet, Form, Modal, Select, Table, TextField } from 'choerodon-ui/pro';
import { ContentPro as Content } from '@choerodon/boot';
import RelationDataSet from './stores/RelationDataSet';

const { Column } = Table;

function sloveUniqueName(masterTable, relationTable) {
  return masterTable > relationTable ? `${masterTable || ''}-${relationTable || ''}` : `${relationTable || ''}-${masterTable || ''}`;
}

export default class Relation extends Component {
  relationDataSet = new DataSet(RelationDataSet);

  newModalKey = Modal.key();

  handleMasterTableOnChange = async () => {
    const { relationDataSet } = this;
    await relationDataSet.current.getField('masterColumn').set('lookupUrl', `/metadata/selectColumn?table=${relationDataSet.current.get('masterTable')}`);
    await relationDataSet.current.getField('masterColumn').fetchLookup();
    relationDataSet.current.set('uniqueName', sloveUniqueName(relationDataSet.current.get('masterTable'), relationDataSet.current.get('relationTable')));
  };

  handleRelationTableOnChange = async () => {
    const { relationDataSet } = this;
    await relationDataSet.current.getField('relationColumn').set('lookupUrl', `/metadata/selectColumn?table=${relationDataSet.current.get('relationTable')}`);
    await relationDataSet.current.getField('relationColumn').fetchLookup();
    relationDataSet.current.set('uniqueName', sloveUniqueName(relationDataSet.current.get('masterTable'), relationDataSet.current.get('relationTable')));
  };

  handleNewRelationModalOk = async () => {
    const { relationDataSet } = this;
    if (await relationDataSet.current.validate()) {
      await relationDataSet.submit();
    } else {
      return false;
    }
  };

  handleNewRelationModalCancel = async () => {
    const { relationDataSet } = this;
    relationDataSet.remove(relationDataSet.current);
  };

  openNewRelationModal = () => {
    const { relationDataSet } = this;
    relationDataSet.create();
    Modal.open({
      drawer: true,
      key: this.newModalKey,
      title: '新建关系',
      okText: '保存',
      children: (
        <Form dataSet={relationDataSet}>
          <Select name="relationType" placeholder="请选择" required />
          <Select name="masterTable" placeholder="请选择" required onChange={this.handleMasterTableOnChange} />
          <Select name="masterColumn" placeholder="请选择" required />
          <Select name="relationTable" placeholder="请选择" required onChange={this.handleRelationTableOnChange} />
          <Select name="relationColumn" placeholder="请选择" required />
          <TextField name="uniqueName" disabled />
        </Form>
      ),
      onOk: this.handleNewRelationModalOk,
      onCancel: this.handleNewRelationModalCancel,
    });
  };

  render() {
    const { relationDataSet } = this;
    return (
      <Content>
        <Table
          dataSet={relationDataSet}
          buttons={[<Button key="commit" funcType="flat" icon="playlist_add" color="blue" onClick={this.openNewRelationModal}>新建</Button>]}
          selectionMode="false"
        >
          <Column name="uniqueName" />
          <Column name="lockedBy" />
          <Column name="relationType" />
          <Column name="masterTable" />
          <Column name="masterColumn" />
          <Column name="relationTable" />
          <Column name="relationColumn" />
          <Column
            header="删除"
            align="center"
            width={100}
            lock="right"
            renderer={({ record }) => (
              <Button
                funcType="flat"
                icon="delete"
                color="blue"
                onClick={() => relationDataSet.delete(record)}
                disabled={record.status === 'add'}
              />
            )}
          />
        </Table>
      </Content>
    );
  }
}
