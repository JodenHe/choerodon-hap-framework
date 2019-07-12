import React, { Component } from 'react';
import { DataSet, Modal, Table } from 'choerodon-ui/pro';
import { axiosPro as axios } from '@choerodon/boot';
import EmployeeDS from '../stores/EmployeeDS';

const { Column } = Table;

export default class EmployeeModal extends Component {
  employeeDS = new DataSet(EmployeeDS);

  constructor(props) {
    super(props);
    props.modal.handleOk(this.handleOk);
    this.instanceResponseExtDS = props.instanceResponseExtDS;
    this.dataSet = props.dataSet;
    this.taskId = props.taskId;
    this.isSingle = props.isSingle;
  }

  handleOk = () => {
    if (this.employeeDS.selected.length === 1) {
      if (this.isSingle) {
        this.taskAction({ action: 'delegate', targetUser: this.employeeDS.current.get('employeeCode'), targetTaskId: this.taskId });
      } else {
        this.dataSet.current.set('delegateUserCode', this.employeeDS.current.get('employeeCode'));
        this.dataSet.current.set('delegateUserName', this.employeeDS.current.get('name'));
      }
    } else {
      Modal.info('请选择要转交的人员');
      return false;
    }
  };

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
    axios.post(`/wfl/runtime/execute/${this.taskId}`, param)
      .then((args) => {
        this.instanceResponseExtDS.query(this.instanceResponseExtDS.currentPage);
        if (args.success) {
          Modal.success('操作完成!');
        } else {
          Modal.error(args.message);
        }
      });
  }

  /**
   * 渲染表格内容
   */
  render() {
    return (
      <Table
        dataSet={this.employeeDS}
        queryFieldsLimit={1}
      >
        <Column name="employeeCode" />
        <Column name="name" />
        <Column name="unitName" />
        <Column name="positionName" />
      </Table>
    );
  }
}
