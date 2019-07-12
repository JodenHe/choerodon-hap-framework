import React from 'react';
import { Lov } from 'choerodon-ui/pro';

export default record => (
  [
    <Lov key="behaviour.page" name="behaviour.page" label="选择页面" record={record} />,
  ]
);
