import React from 'react';
import { observer } from 'mobx-react';
import '../index.scss';

export default observer(({ dataset: { current } }) => {
  let formKey;
  if (current) {
    formKey = current.get('formKey');
    if (formKey) {
      if (formKey.indexOf('?') > 0) {
        formKey = `${formKey}&businessKey=${current.get('businessKey')}`;
      } else {
        formKey = `${formKey}?businessKey=${current.get('businessKey')}`;
      }
    }
  }

  function onIncludeFrameLoad({ target }) {
    const ifm = target;
    const subWeb = ifm.contentWindow.document;
    if (subWeb) {
      ifm.height = subWeb.body.offsetHeight;
      ifm.width = '100%';
    }
  }

  return (
    <div className="approve-block">
      <h3>审批表单</h3>
      <iframe
        title="iframe"
        id="includeFrame"
        name="includeFrame"
        src={formKey ? `/${formKey}` : undefined}
        onLoad={onIncludeFrameLoad}
      />
    </div>
  );
});
