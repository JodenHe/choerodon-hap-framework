import React from 'react';
import { Select } from 'choerodon-ui/pro';
import { getOptions } from './editorMapping';

export default record => (
  <Select key="editor-select" name="editor.type" record={record} label="编辑器" clearButton={false}>
    {getOptions()}
  </Select>
);
