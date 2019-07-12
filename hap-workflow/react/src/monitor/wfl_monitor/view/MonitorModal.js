import React, { PureComponent } from 'react';
import { DataSet } from 'choerodon-ui/pro';
import MonitorFooter from './MonitorFooter';
import MonitorForm from './MonitorForm';
import MonitorIframe from './MonitorIframe';
import ProcessInstanceDS from '../stores/ProcessInstanceDS';
import TaskHistoricDS from '../stores/TaskHistoricDS';

export default class MonitorModal extends PureComponent {
  constructor(props) {
    super(props);
    this.id = props.id;
    this.taskHistoricDS = new DataSet(TaskHistoricDS);

    this.processInstanceDS = new DataSet({
      ...ProcessInstanceDS,
      children: {
        historicTaskList: this.taskHistoricDS,
      },
    });
  }

  componentDidMount() {
    this.processInstanceDS.queryUrl = `/wfl/instanceUP/${this.id}`;
    this.processInstanceDS.query();
  }


  render() {
    return (
      <div>
        <MonitorForm dataset={this.processInstanceDS} id={this.id} />
        <MonitorIframe dataset={this.processInstanceDS} />
        <MonitorFooter
          taskHistoricDS={this.taskHistoricDS}
          id={this.id}
        />
      </div>
    );
  }
}
