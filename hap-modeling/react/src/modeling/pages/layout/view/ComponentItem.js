import React, { PureComponent } from 'react';
import classNames from 'classnames';
import { Col, Icon } from 'choerodon-ui/pro';
import { PREFIX } from '../constants';

export default class ComponentItem extends PureComponent {
  static defaultProps = {
    prefixCls: `${PREFIX}-component-item`,
  };

  handleClick = () => {
    const { type, onSelect } = this.props;
    onSelect(type);
  };

  render() {
    const { icon, description, prefixCls, selected } = this.props;
    const className = classNames(prefixCls, {
      [`${prefixCls}-selected`]: selected,
    });
    return (
      <Col span={12} className={`${prefixCls}-wrapper`}>
        <div className={className} onClick={this.handleClick}>
          <Icon type={icon} />
          <div>
            {description}
          </div>
        </div>
      </Col>
    );
  }
}
