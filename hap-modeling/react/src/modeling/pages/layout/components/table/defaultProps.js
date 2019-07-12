import { isTree } from '../../utils';

export default dataSet => ({
  props: {
    mode: dataSet && isTree(dataSet) ? 'tree' : 'list',
    selectionMode: 'rowbox',
    indentSize: 15,
    rowHeight: 30,
    showQueryBar: true,
    queryFieldsLimit: 1,
  },
});
