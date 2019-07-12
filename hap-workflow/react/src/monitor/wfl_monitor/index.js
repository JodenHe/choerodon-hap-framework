import React, { PureComponent } from 'react';
import { $l, axiosPro as axios, ContentPro as Content } from '@choerodon/boot';
import { Button, DataSet, Modal, Table, Tooltip } from 'choerodon-ui/pro';
import ProcessInstanceResponseExtDS from './stores/HistoricProcessInstanceResponseExtDS';
import CurrentTasksDS from './stores/CurrentTasksDS';
import EmployeeModal from './view/EmployeeModal';
import CurrentTaskModal from './view/CurrentTaskModal';
import ActivitiNodeModal from './view/ActivitiNodeModal';
import MonitorModal from './view/MonitorModal';

const { Column } = Table;
const modalKey = Modal.key();
const modalKey02 = Modal.key();
const modalKey03 = Modal.key();
const modalKey04 = Modal.key();

function startRenderer({ record, text }) {
  if (record.get('startUserName')) {
    return [
      `${record.get('startUserName')}(${text})`,
    ];
  }
  return '';
}

function setState({ record }) {
  if (record.get('endTime')) {
    return <span style={{ color: '#5d6d7c' }}>已结束</span>;
  }
  if (record.get('suspended')) {
    return <span style={{ color: 'goldenrod' }}>挂起中</span>;
  } else {
    return <span style={{ color: 'green' }}>运行中</span>;
  }
}

export default class Index extends PureComponent {
  queryInstanceResponseExtDS({ params }) {
    const { taskState: state } = params;
    if (state === 'end' || state === 'running') {
      params.finished = (state === 'end');
    } else if (state === 'suspended') {
      params.finished = state === 'end';
      params.suspended = true;
    }
  }

  currentTasksDS = new DataSet(CurrentTasksDS);

  instanceResponseExtDS = new DataSet({
    ...ProcessInstanceResponseExtDS,
    children: {
      currentTasks: this.currentTasksDS,
    },
    events: {
      query: this.queryInstanceResponseExtDS,
    },
  });

  openTaskEditModal(record) {
    Modal.open({
      key: modalKey04,
      title: record.get('processName'),
      drawer: true,
      children: (
        <MonitorModal id={record.get('id')} />
      ),
      style: { width: 900 },
      okCancel: false,
      okText: $l('hap.close'),
    });
  }

  batchDelegate = () => {
    const param = [];
    this.currentTasksDS.data.forEach((record) => {
      if (record.get('delegateUserCode')) {
        const p = {};
        p.assignee = record.get('delegateUserCode');
        p.action = 'delegate';
        p.currentTaskId = record.get('taskId');
        param.push(p);
      }
    });
    if (param.length === 0) {
      Modal.info('请选择要转交的人员');
      return false;
    }
    axios.post('/wfl/runtime/admin/tasks/batch-delegate', param)
      .then((args) => {
        if (args.success === false) {
          Modal.error(args.message);
        } else {
          Modal.success('操作完成!');
          this.instanceResponseExtDS.query(this.instanceResponseExtDS.currentPage);
        }
      });
  };

  delegateTask(record) {
    if (record.get('currentTasks').length === 1) {
      Modal.open({
        key: modalKey,
        title: '选择员工',
        drawer: true,
        children: (
          <EmployeeModal
            dataSet={this.currentTasksDS}
            isSingle
            taskId={(record.get('currentTasks')[0].taskId)}
            instanceResponseExtDS={this.instanceResponseExtDS}
          />
        ),
        style: { width: 600 },
        okText: '确定',
      });
    } else {
      Modal.open({
        key: modalKey02,
        title: '批量转交',
        drawer: true,
        children: (
          <CurrentTaskModal dataSet={this.currentTasksDS} />
        ),
        onOk: this.batchDelegate,
        style: { width: 500 },
        okText: '确定',
      });
    }
  }

  jumpProc(record) {
    Modal.open({
      key: modalKey03,
      title: '选择节点',
      drawer: true,
      children: (
        <ActivitiNodeModal
          id={record.get('id')}
          taskId={this.currentTasksDS.current.get('taskId')}
          instanceResponseExtDS={this.instanceResponseExtDS}
        />
      ),
      style: { width: 500 },
      okText: '确定',
    });
  }

  /**
   * 终止流程.
   * @param record
   */
  endProc(record) {
    Modal.confirm('确认终止流程').then((button) => {
      if (button === 'ok') {
        axios.post(`/wfl/runtime/prc/end/${record.get('id')}`)
          .then(() => {
            this.instanceResponseExtDS.query(this.instanceResponseExtDS.currentPage);
          });
      }
    });
  }

  /**
   * 启动流程.
   * @param record
   */
  activeProc(record) {
    Modal.confirm('确认启动流程').then((button) => {
      if (button === 'ok') {
        axios.post(`/wfl/runtime/prc/active/${record.get('id')}`)
          .then(() => {
            this.instanceResponseExtDS.query(this.instanceResponseExtDS.currentPage);
          });
      }
    });
  }

  /**
   * 挂起流程.
   * @param record
   */
  suspendProc(record) {
    Modal.confirm('确认挂起流程').then((button) => {
      if (button === 'ok') {
        axios.post(`/wfl/runtime/prc/suspend/${record.get('id')}`)
          .then(() => {
            this.instanceResponseExtDS.query(this.instanceResponseExtDS.currentPage);
          });
      }
    });
  }


  setButton = ({ record }) => {
    if (record.get('endTime')) {
      return '';
    } else {
      let disable = false;
      const btns = [];
      if (record.get('suspended')) {
        disable = true;
        const btn = (
          <Tooltip title="启动流程">
            <Button funcType="flat" color="blue" icon="play_arrow" onClick={() => this.activeProc(record)} />
          </Tooltip>
        );
        btns.push(btn);
      } else {
        const btn = (
          <Tooltip title="挂起流程">
            <Button funcType="flat" color="blue" icon="pause" onClick={() => this.suspendProc(record)} />
          </Tooltip>
        );
        btns.push(btn);
      }

      const delegateBtn = (
        <Tooltip title="转交流程">
          <Button
            funcType="flat"
            color="blue"
            icon="forward"
            disabled={disable}
            onClick={() => this.delegateTask(record)}
          />
        </Tooltip>
      );

      const jumpBtn = (
        <Tooltip title="跳转流程">
          <Button funcType="flat" color="blue" icon="routeroutline" onClick={() => this.jumpProc(record)} disabled={disable} />
        </Tooltip>
      );
      const endBtn = (
        <Tooltip title="终止流程">
          <Button funcType="flat" color="blue" icon="power_settings_new" onClick={() => this.endProc(record)} />
        </Tooltip>
      );

      btns.push(delegateBtn);
      btns.push(jumpBtn);
      btns.push(endBtn);
      return btns;
    }
  };

  render() {
    return (
      <Content>
        <Table
          dataSet={this.instanceResponseExtDS}
          queryFieldsLimit={2}
        >
          <Column name="id" width={120} />
          <Column name="state" renderer={setState} width={100} />
          <Column name="processName" />
          <Column name="taskName" />
          <Column name="startUserId" renderer={startRenderer} />
          <Column name="currentApprover" />
          <Column name="startTime" sortable />
          <Column name="endTime" sortable />
          <Column
            name="edit"
            header="详细信息"
            lock="right"
            width={100}
            minWidth={50}
            renderer={({ record }) => (
              <Tooltip title="查看">
                <Button
                  funcType="flat"
                  icon="map"
                  onClick={() => this.openTaskEditModal(record)}
                />
              </Tooltip>
            )}
          />
          <Column
            lock="right"
            width={200}
            header="操作"
            renderer={this.setButton}
          />
        </Table>
      </Content>
    );
  }
}
