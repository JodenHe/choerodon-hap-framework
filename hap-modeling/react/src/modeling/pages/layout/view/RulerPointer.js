import React, { Component } from 'react';
import { observer } from 'mobx-react';
import noop from 'lodash/noop';
import { Col } from 'choerodon-ui/pro';
import { pxToRem } from 'choerodon-ui/pro/lib/util/UnitConvertor';
import EventManager from 'choerodon-ui/pro/lib/util/EventManager';
import { PREFIX } from '../constants';

@observer
export default class Ruler extends Component {
  static defaultProps = {
    prefixCls: `${PREFIX}-ruler-pointer`,
  };

  eventManager = new EventManager(typeof window !== 'undefined' && document);

  handleMouseDown = ({ clientX, currentTarget }) => {
    const { eventManager, handleMouseMove, handleMouseUp } = this;
    this.startWidth = this.getWidth();
    this.startX = clientX;
    this.pointer = currentTarget;
    eventManager.addEventListener('mousemove', handleMouseMove);
    eventManager.addEventListener('mouseup', handleMouseUp);
  };

  handleMouseMove = ({ clientX }) => {
    const { pointer: { parentNode, firstElementChild }, startWidth, startX, props: { maxWidth = Number.MAX_SAFE_INTEGER } } = this;
    const width = Math.min(Math.max(startWidth + clientX - startX, this.getMinWidth()), maxWidth);
    this.width = width;
    parentNode.style.width = pxToRem(width);
    firstElementChild.innerHTML = width;
  };

  handleMouseUp = () => {
    const { eventManager, width, props: { record, onChange = noop, index } } = this;
    eventManager.clear();
    if (record) {
      record.set('props.width', width);
    }
    onChange(width, index);
  };

  getWidth() {
    const { record, value } = this.props;
    return record ? record.get('props.width') : value;
  }

  getMinWidth() {
    const { record, minWidth } = this.props;
    return record ? record.get('props.minWidth') : minWidth;
  }

  componentWillUnmount() {
    this.eventManager.clear();
  }

  render() {
    const { prefixCls, maxWidth, span } = this.props;
    const width = this.getWidth();
    const minWidth = this.getMinWidth();
    const pointer = (
      <span className={`${PREFIX}-drop-zone-item`}>
        <span
          className={`${prefixCls}-wrapper`}
          style={{ width: pxToRem(width), minWidth: pxToRem(minWidth), maxWidth: maxWidth ? pxToRem(maxWidth - 2) : undefined }}
        >
          <div className={prefixCls} onMouseDown={this.handleMouseDown}>
            <span>{width}</span>
          </div>
        </span>
      </span>
    );
    return span ? (
      <Col span={span}>
        {pointer}
      </Col>
    ) : pointer;
  }
}
