export default {
  queryUrl: '/hr/employee/queryAllUP',
  autoQuery: true,
  selection: 'single',
  fields: [
    { name: 'employeeId', type: 'number' },
    { name: 'employeeCode', type: 'string', label: '员工编码' },
    { name: 'name', type: 'string', label: '员工姓名' },
    { name: 'unitName', type: 'string', label: '组织名称' },
    { name: 'positionName', type: 'string', label: '岗位名称' },
  ],
  queryFields: [
    { name: 'employeeCode', type: 'string', label: '员工编码' },
    { name: 'name', type: 'string', label: '员工姓名' },
    { name: 'unitId', type: 'number', bind: 'unit.unitId' },
    { name: 'unit', type: 'object', textField: 'name', lovCode: 'LOV_UNIT', label: '组织名称' },
    { name: 'positionId', type: 'number', bind: 'position.positionId' },
    { name: 'position', type: 'object', textField: 'name', lovCode: 'LOV_POSITION', label: '岗位名称' },
  ],
};
