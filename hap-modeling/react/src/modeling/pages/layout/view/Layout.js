import React, { Component, createElement } from 'react';
import { inject, observer } from 'mobx-react';
import { action } from 'mobx';
import { pxToRem } from 'choerodon-ui/pro/lib/_util/UnitConvertor';
import DropZone from '../dnd/DropZone';
import BaseDesigner from '../components/index';
import { PREFIX } from '../constants';

@inject('dndStore', 'componentsDS', 'datasetsDS', 'layoutsDS')
@observer
export default class Layout extends BaseDesigner {
  static defaultProps = {
    prefixCls: `${PREFIX}-layout`,
  };

  renderDragItem = (dropItemProps, dropIndex, child, index, width, height) => {
    dropItemProps.children = this.renderComponent(child);
    if (dropIndex !== -1 && index >= dropIndex) {
      dropItemProps.style = { transform: `translateY(${pxToRem(height || 60)})` };
    }
  };

  renderMain() {
    const { dndStore, componentsDS } = this.props;
    return (
      <DropZone
        items={componentsDS.treeData}
        droppable={dndStore.canDrop(['DataSet', 'Component'])}
        layout="vertical"
        emptyText="请拖入左侧数据源或组件"
        itemRenderer={this.renderDragItem}
      />
    );
  }

  renderTip() {
    return (
      <div className={`${PREFIX}-layout-tip`}>请新建页面布局</div>
    );
  }

  render() {
    const { layoutsDS } = this.props;
    return (
      <div className={`${PREFIX}-content`}>
        <div className={`${PREFIX}-layout`}>
          {layoutsDS.current ? this.renderMain() : this.renderTip()}
        </div>
      </div>
    );
  }
}
