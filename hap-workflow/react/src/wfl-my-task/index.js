import React, { PureComponent } from 'react';
import { ContentPro as Content } from '@choerodon/boot';
import { Button, DataSet, Modal, Table, Tooltip } from 'choerodon-ui/pro';
import TaskResponseExtDS from './stores/TaskResponseExtDS';
import TaskDetailModal from './view/TaskDetailModal';

import './index.scss';

const { Column } = Table;
const modalKey = Modal.key();

function startRenderer({ record, text }) {
  return [
    `${record.get('startUserName')}(${text})`,
  ];
}

function getDurationTime(durationTime) {
  let value = '';
  const days = durationTime / 86400;
  const hours = (durationTime / 3600) % 24;
  const minutes = (durationTime / 60) % 60;
  if (days > 0) {
    value += `${days}天`;
  }
  if (hours > 0) {
    value += `${hours}小时`;
  }
  if (minutes > 0) {
    value += `${minutes}分钟`;
  }
  return value;
}

function priorityRenderer({ record, text }) {
  if (!record.get('dueTime')) {
    if (record.get('priority') < 33) {
      text = '低';
    } else if (record.get('priority') < 66) {
      text = '中';
    } else {
      text = '高';
    }
  } else {
    if (record.get('dueTime') < 0) {
      text = '已超时:';
    } else {
      text = '剩余时间:';
    }
    const dueTime = Math.abs(record.get('dueTime'));
    text += getDurationTime(dueTime);
  }
  if (record.get('priority') < 33) {
    return (
      <div className="priority-wrapper">
        <div className="priority-low">{text}</div>
      </div>
    );
  }
  if (record.get('priority') < 66) {
    return (
      <div className="priority-wrapper">
        <div className="priority-median">{text}</div>
      </div>
    );
  }
  return (
    <div className="priority-wrapper">
      <div className="priority-high">{text}</div>
    </div>
  );
}

export default class Index extends PureComponent {
  queryTaskResponseExtDSS({ params }) {
    const { priority: priority1 } = params;
    if (priority1 === 'high') {
      params.maximumPriority = 100;
      params.minimumPriority = 67;
    } else if (priority1 === 'middle') {
      params.maximumPriority = 66;
      params.minimumPriority = 34;
    } else if (priority1 === 'low') {
      params.maximumPriority = 33;
      params.minimumPriority = 0;
    }
    params.priority = null;
  }

  taskResponseExtDS = new DataSet({
    ...TaskResponseExtDS,
    events: {
      query: this.queryTaskResponseExtDSS,
    },
  });

  openTaskEditModal(record) {
    Modal.open({
      key: modalKey,
      title: record.get('processName'),
      drawer: true,
      children: (
        <TaskDetailModal id={record.get('id')} taskResponseExtDS={this.taskResponseExtDS} />
      ),
      // onOk: saveEmployee,
      style: { width: 900 },
      footer: null,
    });
  }

  render() {
    return (
      <Content>
        <Table
          dataSet={this.taskResponseExtDS}
          queryFieldsLimit={2}
        >
          <Column name="processInstanceId" width={120} sortable />
          <Column name="processName" />
          <Column name="description" />
          <Column name="name" />
          <Column name="startUserId" renderer={startRenderer} />
          <Column name="createTime" sortable />
          <Column name="priority" renderer={priorityRenderer} align="center" width={100} sortable />
          <Column
            name="edit"
            header="操作"
            lock="right"
            width={100}
            minWidth={100}
            renderer={({ record }) => (
              <Tooltip title="办理">
                <Button
                  funcType="flat"
                  icon="branding_watermark"
                  onClick={() => this.openTaskEditModal(record)}
                />
              </Tooltip>
            )}
          />
        </Table>
      </Content>
    );
  }
}
