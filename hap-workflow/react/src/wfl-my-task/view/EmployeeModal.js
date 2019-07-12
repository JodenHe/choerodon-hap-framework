import React, { Component } from 'react';
import { DataSet, Table } from 'choerodon-ui/pro';
import EmployeeDS from '../stores/EmployeeDS';


const { Column } = Table;

export default class EmployeeModal extends Component {
  dataSet = new DataSet(EmployeeDS);

  constructor(props) {
    super(props);
    props.modal.handleOk(this.handleOk);
  }

  handleOk = () => {
    if (this.dataSet.selected.length === 1) {
      // get params
      const { func } = this.props;
      const targetUser = this.dataSet.current.get('employeeCode');
      if (this.props.isAddSign) {
        func({ action: 'addSign', targetUser });
      } else {
        func({ action: 'delegate', targetUser });
      }
    }
  };

  /**
   * 渲染表格内容
   */
  render() {
    return (
      <Table
        dataSet={this.dataSet}
        queryFieldsLimit={2}
      >
        <Column name="employeeCode" />
        <Column name="name" />
        <Column name="unitName" />
        <Column name="positionName" />
      </Table>
    );
  }
}
