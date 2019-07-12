import { action, computed, observable } from 'mobx';
import _ from 'lodash';
import { axiosPro as axios } from '@choerodon/boot';

const map = {};

class DataSourceStore {
  @observable shouldLoadingRelationTables = true;

  @observable relationTablesLoading = true;

  @observable originTables = [
    // {
    //   tableName: '房间',
    //   tableKey: 'act_demo_vacation',
    //   parentKey: null,
    // },
    // {
    //   tableName: '房间查询视图',
    //   tableKey: 'act_evt_log',
    //   parentKey: 'act_demo_vacation',
    // },
    // {
    //   tableName: '项目视图',
    //   tableKey: 'act_exception',
    //   parentKey: 'act_demo_vacation',
    // },
  ];

  @observable tables = [
    // {
    //   tableName: '房间',
    //   tableKey: 'act_demo_vacation',
    //   children: [
    //     {
    //       tableName: '房间查询视图',
    //       tableKey: 'act_evt_log',
    //       children: [],
    //     },
    //     {
    //       tableName: '项目视图',
    //       tableKey: 'act_exception',
    //       children: [],
    //     },
    //   ],
    // },
  ];

  @observable currentTable = {};

  @observable relationTables = [];

  @computed
  get currentTableCode() {
    return this.currentTable.tableKey || (this.tables.length && this.tables[0].tableKey);
  }

  @computed
  get currentTablePath() {
    const res = [];
    let table = this.originTables.find(t => t.tableKey === this.currentTableCode);
    res.push(this.currentTableCode);
    while (table) {
      // eslint-disable-next-line no-loop-func
      const target = this.originTables.find(t => t.tableKey === table.parentKey);
      if (target) {
        res.push(target.tableKey);
      }
      table = target;
    }
    return res;
  }

  @action
  setCurrentTable(data) {
    this.currentTable = data;
  }

  @action
  setRelationTables(data) {
    this.relationTables = data;
  }

  @action
  setTables(data) {
    this.tables = data;
  }

  @action
  addNode(parentNode, { tableName, tableKey }) {
    parentNode.children.push({
      tableName: tableName || tableKey,
      tableKey,
      children: [],
    });
  }

  @action
  deleteNode(node, parentNode) {
    if (!parentNode) {
      this.tables = [];
    } else {
      const index = parentNode.children.indexOf(node);
      parentNode.children.splice(index, 1);
    }
  }

  @action
  loadRelationTables(tableKey) {
    if (map[tableKey]) {
      this.relationTables = map[tableKey];
      this.relationTablesLoading = false;
      return;
    }
    this.relationTablesLoading = true;
    axios.get(`/metadata/selectRelationTable?table=${tableKey}`)
      .then(action((res) => {
        this.relationTablesLoading = false;
        map[tableKey] = res;
        this.relationTables = res;
      }))
      .catch(() => {
        this.relationTablesLoading = false;
        map[tableKey] = [];
        this.relationTables = [];
      });
  }

  // loadRelationTables = tableName => axios.get(`/metadata/selectRelationTable?table=${tableName}`);
}

const dataSourceStore = new DataSourceStore();
export default dataSourceStore;
