import React, { Component } from 'react';
import { Tabs } from 'choerodon-ui/pro';
import { observer, inject } from 'mobx-react';
import LovTab from './views/LovTab';
import FieldTab from './views/FieldTab';
import AssociatedTab from './views/AssociatedTab';
import QueryFieldTab from './views/QueryFieldTab';
import OtherParamsTab from './views/OtherParamsTab';

const { TabPane } = Tabs;

@inject('tableDS')
@observer
export default class Msg extends Component {
  render() {
    const { tableDS } = this.props;
    
    if (!(tableDS.current && tableDS.current.get('tableKey'))) {
      return null;
    }

    return (
      <div className="msg-wrap" style={{ overflow: 'hidden' }}>
        <Tabs>
          <TabPane tab="关联" key="associated">
            <div className="tab-wrapper">
              <AssociatedTab />
            </div>
          </TabPane>
          <TabPane tab="字段" key="fields">
            <div className="tab-wrapper">
              <FieldTab />
            </div>
          </TabPane>
          <TabPane tab="关联Lov" key="lov">
            <div className="tab-wrapper">
              <LovTab />
            </div>
          </TabPane>
          <TabPane tab="查询参数" key="queryFields">
            <div className="tab-wrapper">
              <QueryFieldTab />
            </div>
          </TabPane>
          <TabPane tab="其他参数" key="otherParams">
            <div className="tab-wrapper">
              <OtherParamsTab />
            </div>
          </TabPane>
        </Tabs>
      </div>
    );
  }
}
