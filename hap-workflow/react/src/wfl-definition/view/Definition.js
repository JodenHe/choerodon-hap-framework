import React from 'react';
import { $l, axiosPro as axios } from '@choerodon/boot';
import { Button, Modal, Table, DataSet, Tooltip } from 'choerodon-ui/pro';
import DetailModal from './DetailModal';

const { Column } = Table;
const key = Modal.key();

export default ({ processDefinition, deployment }) => {
  let modal;

  /* function handleDeleteRecord(record) {
    Modal.confirm({
      title: '确认',
      children: (
        <div>
          确定删除 {record.get('name')}
          <br />
          <span style={{ color: 'red' }}>这将会删除所有相关的数据!</span>
          <ul>
            <li>正在运行的流程</li>
            <li>历史流程</li>
          </ul>
        </div>
      ),
    }).then((button) => {
      if (button === 'ok') {
        axios.delete(`/wfl/repository/deployments/${record.get('deploymentId')}?cascade=true`)
          .then(processDefinition.remove(record));
      }
    });
  } */

  function handleCloseModal() {
    modal.close();
  }

  async function handleOpenDetail(record) {
    await axios.get(`/wfl/repository/deployments/${record.get('deploymentId')}`)
      .then((res) => {
        deployment.create(res);
      })
      .catch((err) => {
        Modal.error('未知的错误', err);
      });
    modal = Modal.open({
      key,
      title: `查看[${record.get('name')}]`,
      drawer: true,
      style: { width: 1100 },
      destroyOnClose: true,
      footer: (
        <Button color="blue" onClick={() => handleCloseModal()}>{$l('hap.close')}</Button>
      ),
      children: (
        <DetailModal processDefinition={processDefinition} deployment={deployment} />
      ),
    });
  }

  return (
    <Table dataSet={processDefinition} buttons={['delete']} queryFieldsLimit={2}>
      <Column name="name" />
      <Column
        name="id"
        renderer={({ record }) => <Button funcType="flat" color="blue" onClick={() => handleOpenDetail(record)}>{record.get('id')}</Button>}
      />
      <Column name="key" />
      <Column name="category" />
      <Column name="description" />
    </Table>
  );
};
