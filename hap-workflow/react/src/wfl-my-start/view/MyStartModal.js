import React, { Component } from 'react';
import { Form, Output, TextField, Tabs, Table } from 'choerodon-ui/pro';

const { Column } = Table;

export default (props) => {
  let processInstanceId;
  if (props.myStartDS.current) {
    processInstanceId = props.myStartDS.current.get('id');
  }

  function setAction({ record, text }) {
    const action = record.get('action');
    if (action === 'APPROVED') {
      return <span style={{ color: '#5fb760' }}>{text}</span>;
    } else {
      return <span style={{ color: '#eeac5f' }}>{text}</span>;
    }
  }

  function onIncludeFrameLoad({ target }) {
    const ifm = target;
    const subWeb = ifm.contentWindow.document;
    if (subWeb) {
      ifm.height = subWeb.body.offsetHeight;
      ifm.width = '100%';
    }
  }

  let formKey;
  if (props.taskDS) {
    formKey = props.taskDS.current.get('formKey');
    if (formKey) {
      if (formKey.indexOf('?') > 0) {
        formKey = `${formKey}&businessKey=${props.taskDS.current.get('businessKey')}`;
      } else {
        formKey = `${formKey}?businessKey=${props.taskDS.current.get('businessKey')}`;
      }
    }
  }


  return (
    <div>
      <h3>审批事项</h3>
      <Form columns={2} labelWidth={100} style={{ marginRight: 80 }}>
        <Output name="processName" dataSet={props.taskDS} label="流程" />
        <Output name="startTime" dataSet={props.taskDS} label="申请时间" />
        <Output name="id" dataSet={props.myStartDS} label="流程ID" />
        <Output name="startUserName" dataSet={props.taskDS} label="申请人" />
        <Output name="description" colSpan={2} dataSet={props.taskDS} label="流程描述" />
      </Form>
      <h3>表单</h3>
      <iframe
        title="表单"
        id="includeFrame"
        name="includeFrame"
        src={formKey ? `/${formKey}` : undefined}
        onLoad={onIncludeFrameLoad}
      />


      <div>
        <Tabs>
          <Tabs.TabPane tab="审批历史" key="1">
            <Table
              dataSet={props.taskHistoricDS}
            >
              <Column name="startTime" />
              <Column name="name" />
              <Column name="assigneeName" />
              <Column name="action" renderer={setAction} />
              <Column name="comment" />
            </Table>
          </Tabs.TabPane>
          <Tabs.TabPane tab="流程图" key="2">
            <div className="tab-pane fade" id="processImg">
              <img
                id="svg"
                type="image/svg+xml"
                src={`/wfl/runtime/process-instances/${processInstanceId}/diagram`}
                alt="流程图"
              />
            </div>
          </Tabs.TabPane>
        </Tabs>
      </div>
    </div>
  );
};
