import React, { Component } from 'react';
import { observer } from 'mobx-react';
import { Form, SelectBox } from 'choerodon-ui/pro';
import { getDefaultProps } from '../../mapping';
import LayoutBehaviour from './LayoutBehaviour';
import PageBehaviour from './PageBehaviour';
import FunctionBehaviour from './FunctionBehaviour';

const mapping = {
  openLayout: LayoutBehaviour,
  openPage: PageBehaviour,
  evalFunction: FunctionBehaviour,
};

@observer
export default class Behaviour extends Component {
  handleChange = (value) => {
    const { record } = this.props;
    const props = { name: value };
    if (value === 'evalFunction') {
      props.args = getDefaultProps(record.get('type')).behaviour.args;
    }
    record.set('behaviour', props);
  };

  render() {
    const { record, layoutsDS } = this.props;
    const { Option } = SelectBox;
    return (
      <Form>
        <SelectBox key="behaviour.name" value={record.get('behaviour.name')} label="点击行为" onChange={this.handleChange}>
          <Option value="openLayout">弹出布局</Option>
          <Option value="openPage">打开页面</Option>
          <Option value="evalFunction">执行脚本</Option>
        </SelectBox>
        {
          mapping[record.get('behaviour.name') || 'openLayout'](record, layoutsDS)
        }
      </Form>
    );
  }
}
