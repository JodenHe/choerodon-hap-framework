import { DataSet } from 'choerodon-ui/pro';


export default {
  primaryKey: 'id',
  name: 'MetadataItem',
  pageSize: 10,
  fields: [
    { name: 'id', type: 'string' },
    { name: 'metadataId', type: 'string' },
    { name: 'data', type: 'string' },
    { name: 'dataVersion', label: '数据版本' },
    { name: 'lastUpdateDate', type: 'datetime', label: '更新时间' },
    { name: 'detail', label: '详情' },
  ],
};
