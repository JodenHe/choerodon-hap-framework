import React, { Component } from 'react';
import { action } from 'mobx';
import { inject, observer } from 'mobx-react';
import classNames from 'classnames';
import classes from 'component-classes';
import noop from 'lodash/noop';
import omit from 'lodash/omit';
import { pxToRem } from 'choerodon-ui/pro/lib/_util/UnitConvertor';
import EventManager, { stopPropagation } from 'choerodon-ui/pro/lib/_util/EventManager';
import { PREFIX } from '../constants';
import { isSupportTransition } from '../utils';

function position(target, x, y, width, height, otherStyle = '') {
  target.style.cssText = `left: ${pxToRem(x)};top: ${pxToRem(y)};width:${pxToRem(width)};height:${pxToRem(height)};${otherStyle}`;
}

@inject('dndStore')
@observer
export default class DragZone extends Component {
  static defaultProps = {
    prefixCls: `${PREFIX}-drag-zone`,
    draggable: true,
  };

  eventManager = new EventManager(typeof window !== 'undefined' && document);

  handleMouseDown = (e) => {
    if (e.button === 0) {
      // stopPropagation(e);
      const { eventManager } = this;
      const { clientX, clientY, currentTarget } = e;
      this.dragging = false;
      eventManager.addEventListener('mousemove', this.handleMouseMove, false);
      eventManager.addEventListener('mouseup', this.handleMouseUp, false);
      const { top, left, width, height } = currentTarget.getBoundingClientRect();
      this.originTarget = currentTarget;
      this.startX = left;
      this.startY = top;
      this.startWidth = width;
      this.startHeight = height;
      this.relativeX = clientX - left;
      this.relativeY = clientY - top;
    }
  };

  handleMouseMove = ({ clientX, clientY }) => {
    const { relativeX, relativeY, startX, startY, startWidth, startHeight } = this;
    const currentX = clientX - relativeX;
    const currentY = clientY - relativeY;
    if (!this.dragging) {
      if (currentX !== startX || currentY !== startY) {
        this.dragging = true;
        const { originTarget, props: { record, type, dndStore, onDrag = noop } } = this;
        const target = originTarget.cloneNode(true);
        this.target = target;
        const item = { record, type, from: this };
        const { parent } = record;
        if (parent && parent.get('type') === 'Row' && parent.children.length === 1) {
          item.record = parent;
        }
        dndStore.setDragItem(item);
        const classList = classes(document.body);
        const classString = `${PREFIX}-dragging`;
        classList.add(classString);
        onDrag(item);
        position(target, currentX, currentY, startWidth, startHeight);
        document.body.appendChild(target);
      }
    } else {
      this.currentX = currentX;
      this.currentY = currentY;
      position(this.target, currentX, currentY, startWidth, startHeight);
    }
  };

  handleMouseUp = async (e) => {
    if (e.button === 0) {
      const { target, startX, startY, needTransition, props: { dndStore: { waiter }, onDragEnd = noop } } = this;
      if (target) {
        if (needTransition && isSupportTransition()) {
          ['transitionend', 'oTransitionEnd', 'webkitTransitionEnd', 'mozTransitionEnd'].forEach(event => (
            target.addEventListener(event, this.handleTransitionEnd)
          ));
          position(target, startX, startY, this.startWidth, this.startHeight, 'transition: all .3s');
        } else {
          this.clearTarget();
        }
      }
      this.dragging = false;
      const classList = classes(document.body);
      const classString = `${PREFIX}-dragging`;
      classList.remove(classString);
      this.eventManager.clear();
      if (waiter) {
        await waiter;
      }
      this.props.dndStore.clear(this);
      onDragEnd(e);
    }
  };

  handleFocus = () => {
    const { record } = this.props;
    record.dataSet.current = record;
  };

  handleTransitionEnd = () => {
    this.clearTarget();
  };

  get needTransition() {
    const { props: { dndStore: { waiter } }, startX, startY, currentX, currentY } = this;
    return !waiter && startX !== currentX && startY !== currentY;
  }

  clearTarget() {
    const { target } = this;
    if (target) {
      target.parentNode.removeChild(target);
      this.target = null;
    }
  }

  componentWillUnmount() {
    this.clearTarget();
    this.props.dndStore.clear(this);
    this.eventManager.clear();
  }

  render() {
    const { prefixCls, record, className, children, draggable, buttons, hover, ...restProps } = this.props;
    const classString = classNames(prefixCls, className, {
      [`${prefixCls}-hover`]: hover,
    });
    const snapshotClass = classNames(`${prefixCls}-snapshot`, {
      [`${prefixCls}-snapshot-current`]: record.isCurrent,
    });
    return (
      <div
        {...omit(restProps, ['record', 'type', 'onDrag', 'onDragEnd', 'dndStore', 'onRemove'])}
        className={classString}
        onClick={stopPropagation}
      >
        {buttons && (<span className={`${prefixCls}-btn-group`}>{buttons}</span>)}
        <div
          onMouseDown={draggable ? this.handleMouseDown : noop}
          onFocus={this.handleFocus}
          className={snapshotClass}
          tabIndex={0}
        >
          {children}
        </div>
      </div>
    );
  }
}
