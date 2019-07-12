import React, { PureComponent } from 'react';
import { $l, axiosPro as axios, ContentPro as Content } from '@choerodon/boot';
import { Button, DataSet, Modal, Table, Tooltip } from 'choerodon-ui/pro';
import MyStartDataSet from './stores/MyStartDataSet';
import CurrentTaskDataSet from './stores/CurrentTaskDataSet';
import TaskHistoricDS from './stores/TaskHistoricDS';
import TaskResponseExtDS from './stores/TaskResponseExtDS';
import MyStartModal from './view/MyStartModal';

const { Column } = Table;
const modalKey = Modal.key();

function startRenderer({ record }) {
  return `${record.get('startUserName')}(${record.get('startUserId')})`;
}

function statusRender({ record }) {
  if (record.get('suspended')) {
    return <span style={{ color: 'goldenrod' }}>挂起中</span>;
  } else if (record.get('endTime')) {
    return <span style={{ color: '#5d6d7c' }}>已结束</span>;
  } else {
    return <span style={{ color: 'green' }}>运行中</span>;
  }
}

function assigneeRenderer({ record }) {
  if (record.get('currentApprover')) {
    return [
      `${record.get('currentApprover')}`,
    ];
  }
  return '';
}

export default class Index extends PureComponent {
  openTaskEditModal = async () => {
    const id = this.myStartDS.current.get('id');
    this.taskDS.queryUrl = `/wfl/instance/${id}`;
    await this.taskDS.query();

    Modal.open({
      key: modalKey,
      title: $l('hap.details'),
      drawer: true,
      destroyOnClose: true,
      children: (
        <MyStartModal taskDS={this.taskDS} myStartDS={this.myStartDS} taskHistoricDS={this.taskHistoricDS} />
      ),
      okCancel: false,
      okText: $l('hap.close'),
      style: {
        width: 800,
      },
    });
  };

  constructor(props) {
    super(props);
    this.currentTasks = new DataSet(CurrentTaskDataSet);
    this.taskHistoricDS = new DataSet(TaskHistoricDS);
    this.taskDS = new DataSet({
      ...TaskResponseExtDS,
      children: {
        historicTaskList: this.taskHistoricDS,
      },
    });

    this.myStartDS = new DataSet({
      ...MyStartDataSet,
      children: {
        currentTasks: this.currentTasks,
      },
    });
    this.myStartDS.queryDataSet.addEventListener('update', ({ record }) => {
      if (record.get('status') === 'end') {
        record.set('finished', true);
      } else if (record.get('status') === 'running') {
        record.set('finished', false);
        record.set('suspended', false);
      } else if (record.get('status') === 'suspended') {
        record.set('finished', false);
        record.set('suspended', true);
      } else if (!record.get('status')) {
        record.set('finished', '');
        record.set('suspended', '');
      }
    });
  }

  revokeAccess = () => {
    Modal.confirm({
      modalKey,
      title: 'Confirm',
      children: (
        <div>
          <p>{$l('是否撤回流程')}</p>
        </div>
      ),
    }).then((button) => {
      const id = this.myStartDS.current.get('id');
      axios.post(`/wfl/runtime/prc/back/${id}`, {})
        .then((res) => {
          this.myStartDS.query();
        });
    });
  };

  renderBtn = ({ record }) => {
    if (!record.get('endTime') && record.get('suspended') === false && record.get('recall') === true) {
      return ([
        <Tooltip title={$l('hap.view')}>
          <Button
            funcType="flat"
            color="blue"
            icon="branding_watermark"
            onClick={() => this.openTaskEditModal(record)}
          />
        </Tooltip>,
        <Tooltip title="撤回">
          <Button
            funcType="flat"
            color="blue"
            icon="reply"
            onClick={this.revokeAccess}
          />
        </Tooltip>,
      ]);
    } else {
      return (
        <Tooltip title={$l('hap.view')}>
          <Button
            funcType="flat"
            color="blue"
            icon="branding_watermark"
            onClick={() => this.openTaskEditModal(record)
            }
          />
        </Tooltip>
      );
    }
  };

  render() {
    return (
      <Content>
        <Table
          dataSet={this.myStartDS}
          queryFieldsLimit={2}
        >
          <Column name="id" width={120} />
          <Column name="status" renderer={statusRender} width={100} />
          <Column
            name="edit"
            header={$l('hap.action')}
            width={100}
            lock="right"
            renderer={this.renderBtn}
          />
          <Column name="processName" />
          <Column name="description" />
          <Column name="taskName" />
          <Column name="startUserName" renderer={startRenderer} />
          <Column name="currentApprover" renderer={assigneeRenderer} />
          <Column name="startTime" sortable />
          <Column name="endTime" sortable />
        </Table>
      </Content>
    );
  }
}
