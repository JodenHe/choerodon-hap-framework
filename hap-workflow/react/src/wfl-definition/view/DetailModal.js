import React from 'react';
import { $l } from '@choerodon/boot';
import { Button, Modal, Table, DataSet, Tooltip, Form, TextField, Output } from 'choerodon-ui/pro';

export default ({ processDefinition, deployment }) => (
  [
    <Form header="流程信息" columns={4} dataSet={processDefinition} labelWidth={75}>
      <Output name="name" label="名称" />
      <Output name="category" label="类别" />
      <Output name="key" label="标识" />
      <Output name="version" />
      <Output name="description" />
    </Form>,
    <Form header="部署信息" dataSet={deployment} columns={4} labelWidth={75}>
      <Output name="name" />
      <Output name="category" />
      <Output name="deploymentTime" />
    </Form>,
    <Form header="预览图">
      <div>
        <img
          src={`/wfl/repository/process-definitions/${processDefinition.current.get('id')}/image`}
          alt="halo"
        />
      </div>
    </Form>,
  ]
);
