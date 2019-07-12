import React, { PureComponent } from 'react';
import { $l, axiosPro as axios, ContentPro as Content } from '@choerodon/boot';
import { Button, DataSet, Modal, Table, Tooltip } from 'choerodon-ui/pro';
import CarbonDataSet from './stores/CarbonDataSet';
import CurrentTaskDataSet from './stores/CurrentTaskDataSet';
import TaskHistoricDS from './stores/TaskHistoricDS';
import TaskResponseExtDS from './stores/TaskResponseExtDS';
import CarbonModal from './view/CarbonModal';

const { Column } = Table;
const modalKey = Modal.key();

function startRenderer({ record }) {
  if (record.get('readFlag') === 'N') {
    return <span style={{ fontWeight: 'bold' }}>{record.get('startUserName')}({record.get('startUserId')})</span>;
  } else {
    return `${record.get('startUserName')}(${record.get('startUserId')})`;
  }
}

function statusRender({ record }) {
  const fontWeight = record.get('readFlag') === 'N' ? 'bold' : 'normal';
  if (record.get('suspended') === true) {
    return <span style={{ color: 'goldenrod', fontWeight }}>挂起中</span>;
  } else if (!record.get('endTime')) {
    return <span style={{ color: '#5d6d7c', fontWeight }}>已结束</span>;
  } else {
    return <span style={{ color: 'green', fontWeight }}>运行中</span>;
  }
}

function weightRender({ record, text }) {
  if (record.get('readFlag') === 'N') {
    return <span style={{ fontWeight: 'bold' }}>{text}</span>;
  } else {
    return <span>{text}</span>;
  }
}

function assigneeRenderer({ record }) {
  if (!record.get('currentApprover')) {
    return '';
  } else if (record.get('readFlag') === 'N') {
    return [
      <span style={{ fontWeight: 'bold' }}>{record.get('currentApprover')}</span>,
    ];
  } else {
    return [
      `${record.get('currentApprover')}`,
    ];
  }
}

export default class Index extends PureComponent {
  openTaskEditModal = async () => {
    const id = this.carbonDS.current.get('id');
    this.taskDS.queryUrl = `/wfl/instance/${id}`;
    await this.taskDS.query();

    Modal.open({
      key: modalKey,
      title: $l('hap.details'),
      drawer: true,
      destroyOnClose: true,
      children: (
        <CarbonModal taskDS={this.taskDS} carbonDS={this.carbonDS} taskHistoricDS={this.taskHistoricDS} />
      ),
      style: {
        width: 800,
      },
      okCancel: false,
      okText: $l('hap.close'),
    });
    if (this.carbonDS.current.get('readFlag') === 'N') {
      await axios.post('/wfl/instance/carbon-copy-read', this.carbonDS.current.data).then(
        this.carbonDS.query(),
      );
    }
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

    this.carbonDS = new DataSet({
      ...CarbonDataSet,
      children: {
        currentTasks: this.currentTasks,
      },
    });
    this.carbonDS.queryDataSet.addEventListener('update', ({ record, value, name }) => {
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

  renderBtn = ({ record }) => {
    return (
      <Tooltip title={$l('hap.view')}>
        <Button
          funcType="flat"
          color="blue"
          icon="branding_watermark"
          onClick={() => this.openTaskEditModal(record)}
        />
      </Tooltip>
    );
  }

  render() {
    return (
      <Content>
        <Table
          dataSet={this.carbonDS}
          queryFieldsLimit={2}
        >
          <Column name="id" width={120} renderer={weightRender} />
          <Column name="status" renderer={statusRender} width={100} />
          <Column name="processName" renderer={weightRender} />
          <Column name="description" renderer={weightRender} />
          <Column name="taskName" renderer={weightRender} />
          <Column name="startUserName" renderer={startRenderer} />
          <Column name="currentApprover" renderer={assigneeRenderer} />
          <Column name="startTime" sortable renderer={weightRender} />
          <Column name="endTime" sortable renderer={weightRender} />
          <Column
            name="edit"
            header={$l('hap.action')}
            lock="right"
            width={80}
            renderer={this.renderBtn}
          />
        </Table>
      </Content>
    );
  }
}
