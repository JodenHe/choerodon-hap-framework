import React from 'react';
import ScriptEditor from '../../../view/ScriptEditor';

export default record => (
  [
    <ScriptEditor name="behaviour" record={record} label="脚本" />,
  ]
);
