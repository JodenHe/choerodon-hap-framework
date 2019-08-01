import { $l } from '@choerodon/boot';

export default {
  primaryKey: 'headerId',
  name: 'InterfaceHeader',
  autoQuery: true,
  pageSize: 20,
  fields: [
    { name: 'interfaceCode', type: 'string', label: $l('interface.systemcode'), required: true, unique: true },
    { name: 'description', type: 'string' },
    { name: 'name', type: 'string', label: $l('interface.systemname'), required: true },
    { name: 'interfaceType', type: 'string', label: $l('interface.interfacetype'), lookupCode: 'IF.IF_TYPE', required: true },
    { name: 'headerId', type: 'string', label: 'ID' },
    { name: 'domainUrl', type: 'string', label: $l('interface.systemurl'), required: true },
    { name: 'requestFormat', type: 'string', label: $l('interface.requestformat'), defaultValue: 'raw', lookupCode: 'IF.REQUEST_FORM' },
    { name: 'namespace', type: 'string', label: $l('interface.namespace') },
    { name: 'bodyHeader', type: 'string', label: $l('interface.soapheader') },
    { name: 'bodyTail', type: 'string', label: $l('interface.soaptail') },
    { name: 'soapAction', type: 'String', label: 'soapAction' },
    { name: 'enableFlag', type: 'boolean', label: $l('interface.enableflag'), defaultValue: 'Y', trueValue: 'Y', falseValue: 'N' },
    {
      name: 'authFlag',
      type: 'boolean',
      label: $l('interface.authflag'),
      lookupCode: 'SYS.YES_NO',
      defaultValue: 'N',
      trueValue: 'Y',
      falseValue: 'N',
    },
    { name: 'authType', type: 'String', label: $l('interface.authtype'), lookupCode: 'IF.AUTH_TYPE', defaultValue: 'BASIC_AUTH' },
    { name: 'authUsername', type: 'String', label: $l('interface.checkusername') },
    { name: 'authPassword', type: 'String', label: $l('interface.checkpassword') },
    { name: 'mapperClass', type: 'String', label: $l('interface.mapperclass') },
    { name: 'requestAccept', type: 'String', label: $l('interface.requestaccept') },
    { name: 'requestContentType', type: 'String', label: $l('interface.requestcontenttype') },
    { name: 'requestMethod', type: 'String', label: $l('interface.requestmethod'), lookupCode: 'IF.REQUEST_METHOD', defaultValue: 'POST' },
    { name: 'accessTokenUrl', type: 'String', label: $l('interface.accesstokenurl') },
    { name: 'clientId', type: 'String', label: $l('interface.clientid') },
    { name: 'clientSecret', type: 'String', label: $l('interface.clientsecret') },
    { name: 'scope', type: 'String', label: 'scope' },
    { name: 'grantType', type: 'String', label: $l('interface.granttype'), defaultValue: 'password', lookupCode: 'IF.GRANT_TYPE' },
    { name: 'accessTokenKey', type: 'String' },
  ],
  queryFields: [
    { name: 'interfaceCode', type: 'string', label: $l('interface.systemcode') },
    { name: 'name', type: 'string', label: $l('interface.systemname') },
  ],
};
