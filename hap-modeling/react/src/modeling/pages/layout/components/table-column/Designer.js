import React from 'react';
import { inject, observer } from 'mobx-react';
import defaultTo from 'lodash/defaultTo';
import { pxToRem } from 'choerodon-ui/pro/lib/_util/UnitConvertor';
import BaseDesigner from '../index';
import DragZone from '../../dnd/DragZone';
import { PREFIX } from '../../constants';
import { getFieldConfigByName } from '../../utils';

@inject('dndStore', 'datasetsDS')
@observer
export default class TableColumnDesigner extends BaseDesigner {
  static defaultProps = {
    prefixCls: `${PREFIX}-table-column`,
  };

  render() {
    const { record, dataSet, prefixCls } = this.props;
    const name = record.get('props.name');
    const field = getFieldConfigByName(dataSet, name);
    const label = defaultTo(record.get('props.header'), field.get('label'));
    const width = record.get('props.width');
    const align = record.get('props.align');
    const minWidth = record.get('props.minWidth');
    const { parent } = record;
    const height = parent && parent.get('rowHeight');
    const itemProps = {
      className: `${prefixCls}-item`,
    };
    if (height !== 'auto') {
      itemProps.style = { height: pxToRem(height), lineHeight: pxToRem(height) };
    }
    return (
      <DragZone
        type={record.get('type')}
        record={record}
        className={prefixCls}
        style={{ width: pxToRem(width), minWidth: pxToRem(minWidth), textAlign: align }}
        buttons={this.renderRemoveButton()}
      >
        <div {...itemProps}>
          {label || name}
        </div>
      </DragZone>
    );
  }
}
