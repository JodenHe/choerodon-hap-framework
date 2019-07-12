import React, { PureComponent } from 'react';
import { inject } from 'mobx-react';
import noop from 'lodash/noop';
import omit from 'lodash/omit';
import { getOverflowParent } from '../utils';

@inject('dndStore')
export default class DropItem extends PureComponent {
  handleMouseEnter = ({ currentTarget }) => {
    const { onMouseEnter = noop, index, hidden } = this.props;
    const offsetParent = getOverflowParent(currentTarget);
    const { offsetLeft, offsetTop, offsetWidth, offsetHeight, firstElementChild } = currentTarget;
    if (!hidden) {
      onMouseEnter(index, firstElementChild.offsetLeft, firstElementChild.offsetTop);
    }
    const {
      scrollLeft, scrollTop, scrollWidth, scrollHeight,
      offsetWidth: parentWidth, offsetLeft: parentLeft,
      offsetHeight: parentHeight, offsetTop: parentTop,
    } = offsetParent;
    const currentLeft = offsetLeft - scrollLeft;
    const currentTop = offsetTop - scrollTop;
    const margin = 10;
    let left = scrollLeft;
    let top = scrollTop;
    if (parentWidth < scrollWidth) {
      const width = parentWidth - offsetWidth;
      if (currentLeft > width) {
        left = scrollLeft + currentLeft - width + margin;
      } else if (currentLeft < parentLeft) {
        left = scrollLeft + currentLeft - parentLeft - margin;
      }
    }
    if (parentHeight < scrollHeight) {
      const height = parentHeight - offsetHeight;
      if (currentTop > height) {
        top = scrollTop + currentTop - height + margin;
      } else if (currentTop < parentTop) {
        top = scrollTop + currentTop - parentTop - margin;
      }
    }

    offsetParent.scrollTo({
      top, left, behavior: 'smooth',
    });
  };

  handleMouseDown = (e) => {
    e.stopPropagation();
    const { dndStore } = this.props;
    const { onMouseDown = noop, index, currentTarget: { offsetWidth, offsetHeight } } = e;
    dndStore.setSize(offsetWidth, offsetHeight);
    onMouseDown(index);
  };

  render() {
    const { droppable, component: Cmp = 'span', ...restProps } = this.props;
    return (
      <Cmp
        onMouseEnter={droppable ? this.handleMouseEnter : undefined}
        onMouseDown={this.handleMouseDown}
        {...omit(restProps, ['index', 'onMouseEnter', 'onMouseDown', 'dndStore'])}
      />
    );
  }
}
