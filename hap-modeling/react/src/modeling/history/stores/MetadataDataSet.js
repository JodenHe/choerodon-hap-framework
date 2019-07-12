import { DataSet } from 'choerodon-ui/pro';


export default {
  primaryKey: 'id',
  name: 'Metadata',
  pageSize: 10,
  fields: [
    { name: 'id', type: 'string' },
    { name: 'name', type: 'string', label: '名称' },
    { name: 'dataType', type: 'string', label: '类型', lookupCode: 'MODELING.DATA_TYPE' },
    { name: 'status', type: 'string', label: '状态', lookupCode: 'MODELING.HISTORY_STATUS' },
    { name: 'lockedBy', type: 'string', label: '被锁定' },
    { name: 'detail', label: '详情' },
  ],
  queryDataSet: new DataSet({
    fields: [
      { name: 'name', type: 'string', label: '名称' },
    ],
  }),
};
