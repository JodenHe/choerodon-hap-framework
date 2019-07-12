import { $l } from '@choerodon/boot';

export default {
  name: 'Employee',
  primaryKey: 'employeeId',
  autoQuery: true,
  pageSize: 20,
  fields: [
    { name: 'employeeId', type: 'number' },
    { name: 'employeeCode', type: 'string', label: $l('employee.employeecode'), required: true, unique: true },
    { name: 'name', type: 'string', label: $l('employee.name'), required: true },
    { name: 'bornDate', type: 'date', label: $l('employee.borndate') },
    { name: 'email', type: 'email', label: $l('user.email') },
    { name: 'mobil', type: 'string', label: $l('employee.mobile') },
    { name: 'joinDate', type: 'date', label: $l('employee.joindate') },
    { name: 'gender', type: 'string', label: $l('employee.gender'), lookupCode: 'HR.EMPLOYEE_GENDER', required: true },
    { name: 'certificateId', type: 'string', label: $l('employee.certificateid'), required: true },
    { name: 'certificateType', type: 'string', label: $l('employee.certificatetype'), lookupCode: 'HR.CERTIFICATE_TYPE', required: true },
    { name: 'status', type: 'string', label: $l('employee.status'), lookupCode: 'HR.EMPLOYEE_STATUS', required: true },
    { name: 'enabledFlag', type: 'string', label: $l('employee.enabledflag'), lookupCode: 'SYS.YES_NO', defaultValue: 'Y' },
    { name: 'effectiveStartDate', type: 'date', label: $l('employee.effectivestartdate') },
    { name: 'effectiveEndDate', type: 'date', label: $l('employee.effectiveenddate') },
  ],
  queryFields: [
    { name: 'employeeCode', type: 'string', label: $l('employee.employeecode') },
    { name: 'name', type: 'string', label: $l('employee.name') },
  ],
};
