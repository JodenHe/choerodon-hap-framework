import React from 'react';
import { Button, Lov, Modal, Spin, Table, Tabs, TextArea } from 'choerodon-ui/pro';
import { axiosPro as axios } from '@choerodon/boot';
import { observable } from 'mobx';
import { observer } from 'mobx-react';
import EmployeeModal from './EmployeeModal';
import '../index.scss';

const { Column } = Table;
const modalKey = Modal.key();

const loading = observable.box(false);

export default observer(({ modal, taskHistoricDS, taskResponseExtDetailDS: { current }, formProperties, taskResponseExtDS, ds, isAdmin }) => {
  let approvedText = '同意';
  let rejectedText = '拒绝';
  let carbonCopyUsers = '';
  let processInstanceId;
  if (current) {
    processInstanceId = current.get('processInstanceId');
  }
  let textarea;

  /**
   * 点击事件
   * @param p
   */
  async function actionCallBack(p) {
    p = p || {};
    p.action = p.action || 'complete';
    if (current.get('delegationState') === 'pending' && p.action !== 'delegate') {
      p.action = 'resolve';
    }
    const variables = [];
    if (p.action !== 'delegate') {
      variables.push({ name: 'approveResult', value: p.approveResult });
    }
    const comment = textarea.getValue();
    if (comment) {
      variables.push({ name: 'comment', value: comment });
    }
    if (ds.current) {
      carbonCopyUsers = ds.current.toJSONData().employeecode;
    }
    const param = {
      assignee: p.targetUser || null,
      action: p.action,
      comment: comment || '',
      variables,
      jumpTarget: p.jumpTarget || null,
      carbonCopyUsers,
    };
    let args;
    if (isAdmin) {
      args = await axios.post(`/wfl/runtime/admin/tasks/${current.get('taskId')}`, param);
    } else {
      args = await axios.post(`/wfl/runtime/tasks/${current.get('taskId')}`, param);
    }

    if (args === '') {
      taskResponseExtDS.query();
      modal.close();
      Modal.success('操作完成!');
    } else {
      Modal.error(args.message);
    }
  }

  /**
   *
   * 触发点击事件
   * @param p
   */
  async function taskAction(p) {
    try {
      /* eslint-disable */
      if (window.includeFrame && includeFrame.executeWorkFlowTask) {
        await includeFrame.executeWorkFlowTask(actionCallBack, p);
      } else {
        await actionCallBack(p);
      }
      /* eslint-enable */
    } finally {
      loading.set(false);
    }
  }

  function ApproverOnClick() {
    Modal.confirm(`确认${approvedText}`).then((button) => {
      if (button === 'ok') {
        loading.set(true);
        taskAction({ approveResult: 'APPROVED' });
      }
    });
  }

  function RejectedOnClick() {
    Modal.confirm(`确认${rejectedText}`).then((button) => {
      if (button === 'ok') {
        taskAction({ approveResult: 'REJECTED' });
      }
    });
  }

  function customApproveOnClick(actionId, text) {
    Modal.confirm(`确认${text}`).then((button) => {
      if (button === 'ok') {
        taskAction({ approveResult: actionId });
      }
    });
  }

  function DeleAndAddOnClick(isAddSign) {
    Modal.open({
      key: modalKey,
      title: '选择员工',
      drawer: true,
      children: (
        <EmployeeModal func={taskAction} isAddSign={isAddSign} />
      ),
      style: { width: 600 },
      okText: '确定',
    });
  }

  function CloseOnClick() {
    modal.close();
  }

  function renderBtnGroup() {
    const btns = [];
    let hasSetApproveResult = false;
    let findApproved = false;
    let findReject = false;
    let findDelegate = false;
    let findDddApprover = false;
    let disable = false;

    formProperties.data.forEach((record) => {
      if (record.get('id') === 'approveResult' && record.get('type') === 'enum') {
        hasSetApproveResult = true;
        record.get('enumValues').forEach((data) => {
          if (data.id === 'APPROVED' && data.name) {
            findApproved = true;
            approvedText = data.name;
          } else if (data.id === 'REJECTED' && data.name) {
            findReject = true;
            rejectedText = data.name;
          } else {
            const btn = (
              <Button
                funcType="flat"
                color="blue"
                icon="content_copy"
                onClick={() => customApproveOnClick(data.id, data.name)}
              >
                {data.name}
              </Button>
            );
            btns.push(btn);
          }
        });
      }
      if (record.get('id') === 'APPROVAL_ACTION' && record.get('type') === 'enum') {
        record.get('enumValues').forEach((data) => {
          if (data.id === 'DELEGATE_FLAG' && data.name === 'Y') {
            findDelegate = true;
          } else if (data.id === 'ADD_SIGN_FLAG' && data.name === 'Y') {
            findDddApprover = true;
          }
        });
      }
    });

    if (current) {
      if (current.get('delegationState') === 'pending' || current.get('delegationState') === 'resolved') {
        disable = true;
      }
    }

    const btnApproved = (
      <Button
        name="btn-approved"
        color="blue"
        icon="check_circle"
        onClick={ApproverOnClick}
        loading={loading.get()}
      >
        {approvedText}
      </Button>

    );
    const btnRejected = (
      <Button
        name="btn-rejected"
        funcType="flat"
        color="blue"
        icon="block"
        onClick={RejectedOnClick}
      >
        {rejectedText}
      </Button>

    );
    const btnDelegate = (
      <Button
        name="btn-rejected"
        funcType="flat"
        color="blue"
        icon="forward"
        onClick={() => DeleAndAddOnClick(false)}
      >
        转交
      </Button>
    );
    const btnAddApprover = (
      <Button
        name="btn-rejected"
        funcType="flat"
        color="blue"
        icon="person_add"
        disabled={disable}
        onClick={() => DeleAndAddOnClick(true)}
      >
        添加审批人
      </Button>

    );

    const btnClose = (
      <Button
        name="btn-approved"
        funcType="flat"
        color="blue"
        icon="close"
        onClick={CloseOnClick}
      >
        关闭
      </Button>

    );
    if (!hasSetApproveResult) {
      btns.push(btnApproved);
      btns.push(btnRejected);
    } else {
      if (findApproved) {
        btns.push(btnApproved);
      }
      if (findReject) {
        btns.push(btnRejected);
      }
    }
    if (findDelegate) {
      btns.push(btnDelegate);
    }
    if (findDddApprover) {
      btns.push(btnAddApprover);
    }
    btns.push(btnClose);
    return btns;
  }

  function setAction({ record, text }) {
    const action = record.get('action');
    // let text;
    if (action === 'APPROVED') {
      return <span style={{ color: '#5fb760' }}>{text}</span>;
    } else {
      return <span style={{ color: '#eeac5f' }}>{text}</span>;
    }
  }

  return (
    <Spin tip="Loading..." spinning={loading.get()}>
      <div>
        <Tabs className="approve-block">
          <Tabs.TabPane tab="审批历史" key="1">
            <Table
              dataSet={taskHistoricDS}
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
                alt="流程图"
                id="svg"
                type="image/svg+xml"
                src={`/wfl/runtime/process-instances/${processInstanceId}/diagram`}
              />
            </div>
          </Tabs.TabPane>
        </Tabs>
        <div className="approve-block">
          <h3>审批意见</h3>
          <TextArea
            ref={(node) => {
              if (node) {
                textarea = node;
              }
            }}
            style={{ width: '100%' }}
          />
        </div>
        <div className="approve-block">
          <h3>抄送</h3>
          <Lov dataSet={ds} name="carbonCopyUsers" style={{ width: '100%' }} />
        </div>
        {renderBtnGroup()}
      </div>
    </Spin>
  );
});
