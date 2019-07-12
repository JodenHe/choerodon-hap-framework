import React from 'react';
import { SelectBox, TextField } from 'choerodon-ui/pro';
import LayoutSelect from '../../../view/LayoutSelect';

const { Option } = SelectBox;

function optionsFilter(record) {
  return !record.get('main') && !record.isCurrent;
}

function getLayoutName(record, layoutsDS) {
  const layoutId = record.get('behaviour.layoutId');
  if (layoutId) {
    const layout = layoutsDS.find(r => r.get('layoutId') === layoutId);
    if (layout) {
      return layout.get('layoutName');
    }
  }
}

export default (record, layoutsDS) => (
  [
    <LayoutSelect
      key="behaviour.layoutId"
      name="behaviour.layoutId"
      record={record}
      layoutsDS={layoutsDS}
      label="选择布局"
      optionsFilter={optionsFilter}
    />,
    <TextField key="behaviour.title" name="behaviour.title" label="标题" record={record} placeHolder={getLayoutName(record, layoutsDS)} />,
    <SelectBox key="behaviour.drawer" name="behaviour.drawer" label="打开方式" record={record}>
      <Option value>划出</Option>
      <Option value={false}>弹出</Option>
    </SelectBox>,
  ]
);
