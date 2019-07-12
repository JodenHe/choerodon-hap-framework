import React, { PureComponent } from 'react';
import { DataSet } from 'choerodon-ui/pro';
import TaskDetailFooter from '../../activiti/wfl-task/view/TaskDetailFooter';
import TaskDetailForm from '../../activiti/wfl-task/view/TaskDetailForm';
import TaskDetailIframe from '../../activiti/wfl-task/view/TaskDetailIframe';
import TaskResponseExtDetailDS from '../stores/TaskResponseExtDetailDS';
import TaskHistoricDS from '../stores/TaskHistoricDS';
import EnumValuesDS from '../stores/EnumValuesDS';
import FormPropertiesDS from '../stores/FormPropertiesDS';

export default class TaskDetailModal extends PureComponent {
  constructor(props) {
    super(props);
    this.id = props.id;
    this.taskResponseExtDS = props.taskResponseExtDS;

    this.enumValuesDS = new DataSet(EnumValuesDS);

    this.formPropertiesDS = new DataSet({
      ...FormPropertiesDS,
      children: {
        enumValues: this.enumValuesDS,
      },
    });

    this.taskHistoricDS = new DataSet(TaskHistoricDS);

    this.taskResponseExtDetailDS = new DataSet({
      ...TaskResponseExtDetailDS,
      children: {
        historicTaskList: this.taskHistoricDS,
        'formData.formProperties': this.formPropertiesDS,
      },
    });
  }

  componentDidMount() {
    this.taskResponseExtDetailDS.queryUrl = `/wfl/runtime/tasks/${this.id}/details`;
    this.taskResponseExtDetailDS.query();
  }

  ds = new DataSet({
    fields: [
      { name: 'carbonCopyUsers', type: 'object', textField: 'name', lovCode: 'LOV_EMPLOYEE', multiple: true },
      { name: 'employeecode', type: 'string', bind: 'carbonCopyUsers.employeeCode', multiple: ',' },
    ],
  });

  render() {
    return (
      <div>
        <TaskDetailForm dataset={this.taskResponseExtDetailDS} />
        <TaskDetailIframe dataset={this.taskResponseExtDetailDS} />
        <TaskDetailFooter
          modal={this.props.modal}
          taskHistoricDS={this.taskHistoricDS}
          taskResponseExtDetailDS={this.taskResponseExtDetailDS}
          formProperties={this.formPropertiesDS}
          taskResponseExtDS={this.taskResponseExtDS}
          ds={this.ds}
          isAdmin={false}
        />
      </div>

    );
  }
}
