import AssociatedStore from './Associated';

function getPathByRecord(record, dataSet) {
  if (!record.get('parentKey')) {
    // 说明是主表
    return dataSet.data.map(r => r.get('tableKey'));
  }
  let current = record;
  const res = [];
  while (current && current.get('parentKey')) {
    res.push(current.get('tableKey'));
    // eslint-disable-next-line no-loop-func
    current = dataSet.find(r => r.get('tableKey') === current.get('parentKey'));
  }
  res.push(current.get('tableKey'));
  return res;
}

export default {
  // autoCreate: true,
  parentField: 'parentKey',
  idField: 'tableKey',
  expandField: 'expandFlag',
  fields: [
    { name: 'tableName', type: 'string', label: '表名' },
    { name: 'tableKey', type: 'string', label: 'key' },
    { name: 'parentKey', type: 'string', label: '父级key' },
    { name: 'associationType', type: 'string', label: '' },
    { name: 'association', type: 'string', label: '' },
    { name: 'masterColumnName', type: 'string' },
    { name: 'relationColumnName', type: 'string' },
    { name: 'expandFlag', type: 'boolean', label: true },
    { name: 'join', type: 'string', defaultValue: 'inner' },
    { name: 'filterLogic', type: 'string' },

    { name: 'otherParam', type: 'object' },
    { name: 'id', type: 'string', label: '唯一标识' },
    { name: 'name', type: 'string', label: 'name' },
    { name: 'primaryKey', type: 'string', label: 'primaryKey' },
    { name: 'autoQuery', type: 'boolean', label: '初始化后自动查询', defaultValue: false },
    { name: 'selection', type: 'boolean', label: '是否可选择', defaultValue: true },
    // change to lookup and set default value
    { name: 'selectionType', type: 'string', label: '选择模式', lookupCode: 'DATASET.SELECTION', defaultValue: 'multiple' },
    // { name: 'selectionType', type: 'string', label: 'selectionType', defaultValue: 'multiple' },
    { name: 'modifiedCheck', type: 'boolean', label: '更改警告提示', defaultValue: false },
    { name: 'autoCreate', type: 'boolean', label: '初始化空时自动创建', defaultValue: false },
    { name: 'pageSize', type: 'number', label: '分页大小', defaultValue: 10, step: 1, min: 5 },
    { name: 'paging', type: 'boolean', label: '是否分页', defaultValue: true },

    { name: 'queryUrl', type: 'string', label: '查询url' },
    { name: 'submitUrl', type: 'string', label: '记录提交url' },
    { name: 'tlsUrl', type: 'string', label: '多语言查询url' },

    { name: 'idField', type: 'string', label: '树形当前节点id字段名' },
    { name: 'parentField', type: 'string', label: '树形当前父节点id字段名' },
    { name: 'expandField', type: 'string', label: '树形标记结点是否展开字段名' },
    { name: 'checkField', type: 'string', label: '树形标记结点是否选中字段名' },

    { name: 'cacheSelection', type: 'boolean', label: '缓存选中记录', defaultValue: true },
  ],
  events: {
    indexchange: ({ record, dataSet }) => {
      if (record) {
        AssociatedStore.loadFieldAndSet(record.get('tableKey'), getPathByRecord(record, dataSet));
      }
    },
  },
};
