import React from 'react';
import { Select } from 'choerodon-ui/pro';

function getOptions(layoutsDS, optionsFilter) {
  const { Option } = Select;
  const records = optionsFilter ? layoutsDS.filter(optionsFilter) : layoutsDS.slice();
  return records.map(r => (
    <Option key={r.get('layoutId')} value={r.get('layoutId')}>{r.get('layoutName')}</Option>
  ));
}

export default ({ layoutsDS, optionsFilter, ...props }) => (
  <Select {...props}>
    {getOptions(layoutsDS, optionsFilter)}
  </Select>
);
