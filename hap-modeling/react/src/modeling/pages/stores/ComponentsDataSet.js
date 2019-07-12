export default {
  paging: false,
  idField: 'id',
  parentField: 'parentId',
  fields: [
    { name: 'id', type: 'string' },
    { name: 'parentId', type: 'string' },
    { name: 'type', type: 'string' },
    { name: 'span', type: 'number' },
    { name: 'width', type: 'number' },
    { name: 'behaviour.page', type: 'object', lovCode: 'LOV_RESOURCE' },
    { name: 'behaviour.args', type: 'string', multiple: true },
  ],
};
