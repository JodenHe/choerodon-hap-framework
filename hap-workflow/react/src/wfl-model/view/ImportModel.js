import React, { PureComponent } from 'react';
import { $l } from '@choerodon/boot';
import { Modal, TextField, Form, Upload, Output } from 'choerodon-ui/pro';

export default class ImportModel extends PureComponent {
  upload;

  constructor(props) {
    super(props);
    props.modal.handleOk(this.handleSubmitFile);
  }

  config = {
    headers: {
      'Access-Control-Allow-Origin': '*',
    },
    action: '/wfl/v2/repository/model/import',
    multiple: true,
    accept: ['.xml'],
    uploadImmediately: false,
    showUploadBtn: false,
    data: {
      category: 'default',
    },
    onUploadSuccess: (response) => {
      const res = JSON.parse(response);
      if (res.success) {
        Modal.info($l('hap.tip.success'));
      } else {
        Modal.error('hap.tip.failure');
      }
      this.props.modelDS.query();
      this.modal.close();
    },
  };

  handleChangeName = (value) => {
    this.config.data.name = value;
  };

  handleChangeKey = (value) => {
    this.config.data.key = value;
  };

  handleChangeCategory = (value) => {
    this.config.data.category = value;
  };

  saveUpload = (node) => {
    this.upload = node;
  };

  handleSubmitFile = async () => {
    await this.upload.startUpload();
  };

  renderUpload = () => (<Upload ref={this.saveUpload} {...this.config} />);

  render() {
    return (
      <Form labelWidth={125} style={{ width: 450 }}>
        <TextField label={$l('hap.name')} onChange={(value, oldValue) => this.handleChangeName(value, oldValue)} placeholder="留空默认" />
        <TextField label={$l('activiti.identification')} onChange={(value, oldValue) => this.handleChangeKey(value, oldValue)} placeholder="留空默认" />
        <TextField
          label={$l('activiti.category')}
          saveUpload
          onChange={(value, oldValue) => this.handleChangeCategory(value, oldValue)}
          defaultValue="default"
        />
        <Output lable={$l('activiti.bpmnfile')} renderer={this.renderUpload} />
      </Form>
    );
  }
}
