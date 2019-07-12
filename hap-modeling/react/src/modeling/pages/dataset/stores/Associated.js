import { action, observable } from 'mobx';
import _ from 'lodash';
import { axiosPro as axios } from '@choerodon/boot';

const map = {};

class AssociatedStore {
  @observable fields = [];

  @observable fieldsAll = [];

  @action
  setFields(data) {
    this.fields = data;
  }

  @action
  setFieldsAll(data) {
    this.fieldsAll = data;
  }

  @action
  async loadFieldAndSet(tableName, tablePath = [tableName]) {
    if (map[tableName]) {
      const res = map[tableName];
      this.fields = res.fields.rows;
      this.fieldsAll = res.fieldsAll;
      return;
    }
    const res = await this.loadField(tableName);
    const result = await this.loadFieldByTablePath(tablePath);
    map[tableName] = {
      fields: res,
      fieldsAll: result,
    };
    this.fields = res.rows;
    this.fieldsAll = result;
  }

  loadField = tableName => axios.post('/dataset/MetadataColumn/queries', { tableName });

  loadFieldByTablePath = tablePath => axios.post('modeling/dataset/metadatacolumn/queries', [...tablePath].map(o => ({ tableName: o })));
}

const associatedStore = new AssociatedStore();
export default associatedStore;
