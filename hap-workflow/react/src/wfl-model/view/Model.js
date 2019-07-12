import React, { PureComponent } from 'react';
import { $l, axiosPro as axios } from '@choerodon/boot';
import { Button, Form, Modal, Table, TextField, Tooltip } from 'choerodon-ui/pro';
import EditApproveChain from './EditApproveChain';
import ImportModel from './ImportModel';

const { Column } = Table;
const modalKey = {
  addKey: Modal.key(),
  importKey: Modal.key(),
  editApproveChain: Modal.key(),
};

export default class Model extends PureComponent {
  addModal;

  constructor(props) {
    super(props);
    this.state = {
      loadingId: undefined,
    };
  }

  handleOnOkAddModel = async () => {
    const { modelRequest, modelDataSet } = this.props;

    if (await modelRequest.current.validate()) {
      const { name, version, description } = modelRequest.current.data;
      modelRequest.current.set('metaInfo', JSON.stringify({
        name,
        version,
        description,
      }));
      const result = await axios.post('/wfl/repository/models', modelRequest.current.data);
      if (result) {
        Modal.info($l('hap.tip.success'));
        this.addModal.close();
        await modelDataSet.query();
        return true;
      } else {
        Modal.error($l('hap.failure'));
      }
    }
    return false;
  };

  openAddModel = () => {
    const { modelRequest } = this.props;

    this.addModal = Modal.open({
      key: modalKey.addKey,
      title: $l('wfl.newmodel'),
      drawer: true,
      okText: $l('hap.save'),
      style: { width: 450 },
      destroyOnClose: true,
      afterClose: () => modelRequest.current.reset(),
      onOk: this.handleOnOkAddModel,
      children: (
        <Form labelWidth={100} dataSet={modelRequest}>
          <TextField name="name" />
          <TextField name="key" />
          <TextField name="category" />
          <TextField name="description" />
        </Form>
      ),
    });
  };

  openImportModal = () => {
    Modal.open({
      key: modalKey.importKey,
      title: $l('wfl.importmodel'),
      drawer: true,
      okText: $l('wfl.sysfile.upload'),
      style: { width: 600 },
      destroyOnClose: true,
      children: (
        <ImportModel modelDS={this.props.modelDataSet} />
      ),
    });
  };

  handleOnClickEditModel(record) {
    const { id } = record.data;
    window.open(`/modeler_new.html?modelId=${id}`);
  }

  handleOnClickEditApproveChain(record) {
    const { id, name } = record.data;
    Modal.open({
      key: modalKey.editApproveChain,
      title: $l('wfl.editapprovechain'),
      drawer: true,
      style: { width: 1200 },
      destroyOnClose: true,
      children: <EditApproveChain modelId={id} name={name} />,
      okCancel: false,
    });
  }

  async handleOnClickDeleteModel(record) {
    try {
      const { id } = record.data;
      const button = await Modal.confirm({
        title: $l('hap.tip.info'),
        children: (
          <h2>{$l('hap.tip.delete_confirm')}</h2>
        ),
      });
      if (button === 'ok') {
        const url = `/repository/models/${id}`;
        await axios.delete(url);
        await this.props.modelDataSet.query();
      }
    } catch (e) {
      Modal.error(e.message);
    }
  }

  async handleOnClickDeployModel(id) {
    this.setState({ loadingId: id });
    await axios.get(`/wfl/repository/model/${id}/deploy`)
      .then((response) => {
        if (response.success) {
          this.props.modelDataSet.query();
          Modal.info($l('wfl.release.success', '发布成功'));
        } else {
          Modal.error(response.message);
        }
      }).catch(error => Modal.error(error));
    this.setState({ loadingId: undefined });
  }

  AddBtn = (<Button funcType="flat" color="blue" icon="add" onClick={() => this.openAddModel()}>{$l('hap.new')}</Button>);

  importBth = (
    <Button funcType="flat" color="blue" icon="file_upload" onClick={() => this.openImportModal()}>{$l('wfl.sysfile.upload')}</Button>
  );

  renderColumnRelease(record) {
    if (record.get('deploymentId')) {
      return (
        <Tooltip title={$l('wfl.notneedrelease', '不需要发布')}>
          <Button
            funcType="flat"
            color="gray"
            loading={this.state.loadingId === record.get('id')}
            icon="done"
            onClick={() => this.handleOnClickDeployModel(record.get('id'))}
          />
        </Tooltip>
      );
    } else {
      return (
        <Tooltip title={$l('wfl.needrelease', '需要发布新版本')}>
          <Button
            funcType="flat"
            color="blue"
            loading={this.state.loadingId === record.get('id')}
            icon="done"
            onClick={() => this.handleOnClickDeployModel(record.get('id'))}
          />
        </Tooltip>
      );
    }
  }

  render() {
    return (
      <Table buttons={[this.AddBtn, this.importBth]} dataSet={this.props.modelDataSet}>
        <Column name="key" />
        <Column name="name" />
        <Column name="createTime" />
        <Column name="lastUpdateTime" />
        <Column
          header={$l('hap.action')}
          minWidth={200}
          renderer={({ record }) => ([
            <Tooltip title={$l('hap.edit')}>
              <Button funcType="flat" color="blue" icon="mode_edit" onClick={() => this.handleOnClickEditModel(record)} />
            </Tooltip>,
            // <Tooltip title={$l('wfl.editapprovechain')}>
            //   <Button funcType="flat" color="blue" icon="manage_person" onClick={() => this.handleOnClickEditApproveChain(record)} />
            // </Tooltip>,
            <Tooltip title={$l('hap.delete')}>
              <Button funcType="flat" color="blue" icon="delete" onClick={() => this.handleOnClickDeleteModel(record)} />
            </Tooltip>,
            <Tooltip title={$l('hap.export')}>
              <Button funcType="flat" color="blue" icon="export" href={`/wfl/repository/model/${record.get('id')}/export?type=bpmn20`} download />
            </Tooltip>,
            this.renderColumnRelease(record),
          ])}
        />
      </Table>
    );
  }
}
