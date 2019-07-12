import React, { Component } from 'react';
import { Button, Modal, Table, Tooltip } from 'choerodon-ui/pro';
import EmployeeModal from './EmployeeModal';

const { Column } = Table;

export default ({ dataSet }) => {
  function openEmployeeModal() {
    Modal.open({
      title: '选择员工',
      drawer: true,
      children: (
        <EmployeeModal dataSet={dataSet} isSingle={false} />
      ),
      style: { width: 500 },
      okText: '确定',
    });
  }

  return (
    <Table
      dataSet={dataSet}
      queryFieldsLimit={4}
    >
      <Column name="taskName" />
      <Column name="assigneeName" />
      <Column name="delegateUserCode" />
      <Column
        name="edit"
        header="操作"
        lock="right"
        width={70}
        minWidth={70}
        renderer={({ record }) => (
          <Tooltip title="转交">
            <Button
              funcType="flat"
              icon="forward"
              onClick={() => openEmployeeModal(record)}
            />
          </Tooltip>
        )}
      />
    </Table>
  );
};
