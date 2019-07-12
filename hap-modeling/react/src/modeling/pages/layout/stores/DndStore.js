import { action, get, observable, reaction } from 'mobx';

export default class DndStore {
  @observable dragItem;

  @observable width;

  @observable height;

  @observable waiter;

  @action
  setDragItem(item) {
    this.dragItem = item;
  }

  @action
  setSize(width, height) {
    this.width = width;
    this.height = height;
  }

  @action
  setWaiter(waiter) {
    this.waiter = waiter;
  }

  @action
  clear(from) {
    const { dragItem } = this;
    if (dragItem && dragItem.from === from) {
      this.dragItem = null;
      this.waiter = null;
    }
  }

  canDrop(types) {
    const { dragItem, waiter } = this;
    if (dragItem && !waiter) {
      const type = get(dragItem, 'type');
      return types.includes(type);
    }
    return false;
  }
}
