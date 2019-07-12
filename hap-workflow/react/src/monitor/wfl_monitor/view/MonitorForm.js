import React from 'react';
import { Form, Output } from 'choerodon-ui/pro';
import { observer } from 'mobx-react';

export default observer(({ dataset, id }) => {
  if (dataset.current) {
    const startUserId = dataset.current.get('startUserId');
    dataset.current.set('startUserId', `${dataset.current.get('startUserName')}(${startUserId})`);
    dataset.current.set('id', id);
  }
  return (
    <div className="approve-block">
      <h3>审批事项</h3>
      <Form dataSet={dataset} style={{ width: '8rem' }} columns={2}>
        <Output name="processName" disabled />
        <Output name="startTime" disabled />
        <Output name="id" disabled />
        <Output name="startUserId" disabled />
        <Output name="description" disabled colSpan={2} />
      </Form>
    </div>
  );
});
