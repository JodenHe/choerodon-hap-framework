import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Button, DataSet, Form, IntlField, Modal, Table, TextField } from 'choerodon-ui/pro';
import { ContentPro as Content } from '@choerodon/boot';
import MetadataDataSet from './stores/MetadataDataSet';

const { Column } = Table;

@withRouter
export default class Index extends Component {
  metadataDS = new DataSet(MetadataDataSet);

  addMetadataModalKey = Modal.key();

  handleNewDatasetModalOk = async () => {
    const { metadataDS } = this;
    if (await metadataDS.current.validate()) {
      await metadataDS.submit();
    } else {
      return false;
    }
  };

  handleNewDatasetModalCancel = async () => {
    const { metadataDS } = this;
    metadataDS.remove(metadataDS.current);
  };

  openNewDatasetModal = () => {
    const { metadataDS } = this;
    metadataDS.create();
    Modal.open({
      drawer: true,
      key: this.addMetadataModalKey,
      title: '新建页面',
      okText: '保存',
      children: (
        <Form dataSet={metadataDS}>
          <TextField name="url" required />
          <IntlField name="name" required />
          <IntlField name="description" />
        </Form>
      ),
      onOk: this.handleNewDatasetModalOk,
      onCancel: this.handleNewDatasetModalCancel,
    });
  };

  render() {
    const { metadataDS } = this;
    const addBtn = (
      <Button
        key="add"
        color="blue"
        funcType="flat"
        icon="playlist_add"
        onClick={this.openNewDatasetModal}
      >
        新建
      </Button>
    );

    return (
      <Content>
        <Table
          dataSet={metadataDS}
          buttons={[addBtn, 'save']}
          selectionMode="false"
        >
          <Column name="url" />
          <Column name="name" editor={<IntlField />} />
          <Column name="description" editor={<IntlField />} />
          <Column name="lockedBy" />
          <Column
            header="操作"
            align="center"
            width={100}
            lock="right"
            renderer={({ record }) => [
              <Button
                funcType="flat"
                icon="mode_edit"
                href={`/withoutsider.html#/hap-modeling/modeling/metadataitem/${record.get('url')}`}
                target="HAP_CORE_MODELING_METADATAITEM"
              />,
              <Button
                funcType="flat"
                icon="delete"
                onClick={() => metadataDS.delete(record)}
              />,
            ]}
          />
        </Table>
      </Content>
    );
  }
}
