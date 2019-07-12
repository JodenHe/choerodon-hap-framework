import React, { Component } from 'react';
import { Table, TextField, Button, Modal, Select, DataSet, Form, IntlField } from 'choerodon-ui/pro';
import { withRouter } from 'react-router-dom';
import { ContentPro as Content } from '@choerodon/boot';
import MainDataSet from './stores/MainDataSet';

const { Column } = Table;

@withRouter
export default class DataSetDefine extends Component {
  mainDataSet = new DataSet(MainDataSet);

  newModalKey = Modal.key();

  handleNewDatasetModalOk = async () => {
    const { mainDataSet } = this;
    if (await mainDataSet.current.validate()) {
      await mainDataSet.submit();
    } else {
      return false;
    }
  };

  handleNewDatasetModalCancel = async () => {
    const { mainDataSet } = this;
    mainDataSet.remove(mainDataSet.current);
  };

  openNewDatasetModal = () => {
    const { mainDataSet } = this;
    mainDataSet.create();
    Modal.open({
      drawer: true,
      key: this.newModalKey,
      title: '新建页面',
      okText: '保存',
      children: (
        <Form dataSet={mainDataSet}>
          <TextField name="url" required />
          <IntlField name="name" required />
          <IntlField name="description" />
        </Form>
      ),
      onOk: this.handleNewDatasetModalOk,
      onCancel: this.handleNewDatasetModalCancel,
    });
  };

  handleClickEdit = () => {
    const name = this.mainDataSet.current.get('name');
    this.props.history.push(`/hap-modeling/modeling/pages/dataset/${name}`);
  }

  render() {
    const { mainDataSet } = this;
    return (
      <Content>
        <Table
          dataSet={mainDataSet}
          buttons={[<Button key="commit" funcType="flat" icon="playlist_add" color="blue" onClick={this.openNewDatasetModal}>新建</Button>]}
          selectionMode="false"
        >
          <Column name="url" />
          <Column name="name" editor={<IntlField />} />
          <Column name="description" editor={<IntlField />} />
          <Column name="lockedBy" />
          <Column
            header="编辑"
            align="center"
            width={100}
            lock="right"
            renderer={({ record }) => (
              <Button
                funcType="flat"
                icon="mode_edit"
                onClick={this.handleClickEdit}
                disabled={record.status === 'add'}
              />
            )}
          />
          <Column
            header="删除"
            align="center"
            width={100}
            lock="right"
            renderer={({ record }) => (
              <Button
                funcType="flat"
                icon="delete"
                onClick={() => mainDataSet.delete(record)}
                disabled={record.status === 'add'}
              />
            )}
          />
        </Table>
      </Content>
    );
  }
}
