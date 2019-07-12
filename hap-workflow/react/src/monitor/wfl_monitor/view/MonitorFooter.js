import React from 'react';
import { Table, Tabs } from 'choerodon-ui/pro';
import { observer } from 'mobx-react';
import '../index.scss';

const { Column } = Table;

export default observer(({ taskHistoricDS, id }) => {
  function setAction({ record, text }) {
    const action = record.get('action');
    if (action === 'APPROVED') {
      return <span style={{ color: '#5fb760' }}>{text}</span>;
    } else {
      return <span style={{ color: '#eeac5f' }}>{text}</span>;
    }
  }

  return (
    <div>
      <Tabs>
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
              src={`/wfl/runtime/process-instances/${id}/diagram`}
            />
          </div>
        </Tabs.TabPane>
      </Tabs>
    </div>
  );
});
