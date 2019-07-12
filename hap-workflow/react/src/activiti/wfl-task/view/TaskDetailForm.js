import React from 'react';
import { Form, Output } from 'choerodon-ui/pro';
import { observer } from 'mobx-react';

export default observer(({ dataset }) => {
  if (dataset.current) {
    const startUserId = dataset.current.get('startUserId');
    dataset.current.set('startUserId', `${dataset.current.get('startUserName')}(${startUserId})`);
  }

  return (
    <div className="approve-block">
      <h3>审批事项</h3>
      <Form dataSet={dataset} style={{ width: '8rem' }} columns={2}>
        <Output name="processDefinitionName" colSpan={2} />
        <Output name="processInstanceId" />
        <Output name="startUserId" />
        <Output name="createTime" />
        <Output name="priority" />
        <Output name="description" colSpan={2} />
      </Form>
    </div>
  );
});
