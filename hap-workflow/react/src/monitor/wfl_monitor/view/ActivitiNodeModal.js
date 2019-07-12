import React, { Component } from 'react';
import { DataSet, Modal, Table } from 'choerodon-ui/pro';
import { axiosPro as axios } from '@choerodon/boot';
import ActivitiNodeDS from '../stores/ActivitiNodeDS';

const { Column } = Table;

export default class ActivitiNodeModal extends Component {
  constructor(props) {
    super(props);
    this.dataSet = new DataSet(ActivitiNodeDS);
    props.modal.handleOk(this.handleOk);
    this.taskId = props.taskId;
    this.id = props.id;
    this.instanceResponseExtDS = props.instanceResponseExtDS;
    this.dataSet.queryUrl = `/wfl/definition/user-tasks/${this.id}`;
    this.dataSet.query();
  }

  taskAction(p) {
    p = p || {};
    p.action = p.action || 'complete';
    const param = {
      assignee: p.targetUser || null,
      action: p.action,
      variables: [],
      jumpTarget: p.jumpTarget || null,
      jumpTargetName: p.jumpTargetName || null,
      currentTaskId: p.targetTaskId || null,
    };
    axios.post(`/wfl/runtime/execute/${this.id}`, param)
      .then((args) => {
        if (args.success === false) {
          Modal.error(args.message);
        } else {
          this.instanceResponseExtDS.query(this.instanceResponseExtDS.currentPage);
          Modal.success('操作完成!');
        }
      });
  }

  handleOk = () => {
    if (this.dataSet.selected.length === 0) {
      Modal.info('请选择需要跳转的节点');
      return false;
    }
    this.taskAction({
      action: 'jump',
      jumpTarget: this.dataSet.current.get('nodeId'),
      jumpTargetName: this.dataSet.current.get('name'),
      targetTaskId: this.taskId,
    });
  };

  /**
   * 渲染表格内容
   */
  render() {
    return (
      <Table
        dataSet={this.dataSet}
        queryFieldsLimit={4}
      >
        <Column name="nodeId" />
        <Column name="name" />
      </Table>
    );
  }
}
