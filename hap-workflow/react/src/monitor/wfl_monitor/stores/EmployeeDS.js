import { $l } from '@choerodon/boot';

export default {
  queryUrl: '/hr/employee/queryAllUP',
  autoQuery: true,
  selection: 'single',
  fields: [
    { name: 'employeeId', type: 'number' },
    { name: 'employeeCode', type: 'string', label: $l('employee.employeecode') },
    { name: 'name', type: 'string', label: $l('employee.name') },
    { name: 'unitName', type: 'string', label: $l('hrorgunit.name') },
    { name: 'positionName', type: 'string', label: $l('position.name') },
  ],
  queryFields: [
    { name: 'employeeCode', type: 'string', label: $l('employee.employeecode') },
    { name: 'name', type: 'string', label: $l('employee.name')},
    { name: 'unitId', type: 'number', bind: 'unit.unitId' },
    { name: 'unit', type: 'object', textField: 'name', lovCode: 'LOV_UNIT', label: $l('hrorgunit.name') },
    { name: 'positionId', type: 'number', bind: 'position.positionId' },
    { name: 'position', type: 'object', textField: 'name', lovCode: 'LOV_POSITION', label: $l('position.name') },
  ],
};
