import React, { Component } from 'react';
import { Row } from 'choerodon-ui/pro';
import ComponentItem from './ComponentItem';

export default class ComponentsModal extends Component {
  state = {
    selectType: null,
  };

  constructor(props) {
    super(props);
    props.modal.handleOk(this.handleOk);
  }

  handleOk = () => {
    const { onSelect, dragItem } = this.props;
    const { selectType } = this.state;
    if (selectType) {
      onSelect(selectType, dragItem);
    } else {
      return false;
    }
  };

  handleSelect = (selectType) => {
    this.setState({
      selectType,
    });
  };

  renderComponent(type, icon, description) {
    return (
      <ComponentItem
        type={type}
        selected={type === this.state.selectType}
        onSelect={this.handleSelect}
        icon={icon}
        description={description}
      />
    );
  }

  render() {
    return (
      <Row gutter={10}>
        {this.renderComponent('Table', 'grid_on', '列表')}
        {this.renderComponent('Form', 'inbox_content', '表单')}
      </Row>
    );
  }
}
