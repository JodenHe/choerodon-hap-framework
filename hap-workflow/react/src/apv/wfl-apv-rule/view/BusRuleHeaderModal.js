import React from 'react';
import moment from 'moment';
import { DatePicker, Table } from 'choerodon-ui/pro';

function endTimeMin(record) {
  const time = record.get('startActiveDate');
  if (time) {
    return moment(time);
  }
}

const { Column } = Table;

export default ({ dataset }) => (
  <Table
    buttons={['add', 'save', 'delete']}
    dataSet={dataset}
    queryFieldsLimit={2}
  >
    <Column name="code" editor />
    <Column name="description" editor />
    <Column name="startActiveDate" editor />
    <Column
      name="endActiveDate"
      editor={record => (
        <DatePicker min={endTimeMin(record)} />
      )}
    />
    <Column name="enableFlag" editor width={140} />
  </Table>
);
