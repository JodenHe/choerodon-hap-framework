import React from 'react';
import { Output, TextArea } from 'choerodon-ui/pro';
import { PREFIX } from '../constants';

const ScriptEditor = ({ name, prefixCls, record, ...props }) => (
  <div className={prefixCls}>
    <h4>
      <label>参数</label>
      <Output name={`${name}.args`} record={record} />
    </h4>
    <TextArea name={`${name}.script`} record={record} {...props} />
  </div>
);

ScriptEditor.displayName = 'ScriptEditor';
ScriptEditor.defaultProps = { prefixCls: `${PREFIX}-script-editor` };

export default ScriptEditor;
