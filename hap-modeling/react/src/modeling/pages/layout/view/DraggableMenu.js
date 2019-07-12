import React, { Component } from 'react';
import { inject, observer } from 'mobx-react';
import { Icon, Menu } from 'choerodon-ui/pro';
import DragZone from '../dnd/DragZone';
import { PREFIX } from '../constants';

const { SubMenu, Item } = Menu;

@inject('datasetsDS')
@observer
export default class DraggableMenu extends Component {
  static defaultProps = {
    prefixCls: `${PREFIX}-menu`,
  };

  getMenuItems(fields) {
    if (fields) {
      const { prefixCls } = this.props;
      return fields.map(field => (
        <Item key={`key-${field.get('label') || ''}-name-${field.get('name')}`}>
          <DragZone record={field} type="Field">
            <span className={`${prefixCls}-item`}>
              <Icon type="label" />
              {field.get('label') || field.get('name')}
            </span>
          </DragZone>
        </Item>
      ));
    }
  }

  getSubMenus() {
    const { datasetsDS, prefixCls } = this.props;
    return datasetsDS.data.map((record) => {
      const title = (
        <DragZone record={record} type="DataSet">
          <span className={`${prefixCls}-item`}>
            <Icon type="database" />
            {record.get('datasetName')}
          </span>
        </DragZone>
      );
      return (
        <SubMenu key={record.get('name')} title={title}>
          {this.getMenuItems(record.getCascadeRecords('fields'))}
        </SubMenu>
      );
    });
  }

  render() {
    const { prefixCls, datasetsDS } = this.props;
    return datasetsDS.length ? (
      <Menu className={prefixCls}>
        {this.getSubMenus()}
      </Menu>
    ) : (
      <div className={`${prefixCls}-tip`}>请在数据建模中添加数据源</div>
    );
  }
}
