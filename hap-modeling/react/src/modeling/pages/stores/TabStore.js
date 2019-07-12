import { action, observable } from 'mobx';

class TabStore {
  @observable currentTab = 'layout';

  @action
  setCurrentTab(data) {
    this.currentTab = data;
  }
}

const tabStore = new TabStore();
export default tabStore;
