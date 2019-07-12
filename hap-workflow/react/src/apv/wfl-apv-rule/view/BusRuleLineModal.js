import React from 'react';
import { Table } from 'choerodon-ui/pro';

const { Column } = Table;

export default ({ dataset }) => (
  <Table
    buttons={['add', 'delete']}
    dataSet={dataset}
  >
    <Column name="description" editor />
    <Column name="conditions" editor />
    <Column name="enableFlag" editor width={140}/>
  </Table>
);
