import React from 'react';
import isString from 'lodash/isString';
import omit from 'lodash/omit';
import Loadable from 'react-loadable';
import { Row, Tooltip, Button, Output, TextField, TextArea, NumberField, Currency, IntlField, Lov, Select, SelectBox, Password, CheckBox, Radio, Switch, ColorPicker, DatePicker, DateTimePicker, TimePicker, WeekPicker, MonthPicker, YearPicker, EmailField, UrlField } from 'choerodon-ui/pro';
import { openTabR } from '@choerodon/boot';

const FormComponent = Loadable({
  loader: () => import('choerodon-ui/pro/lib/form'),
  loading: () => <div>loading</div>,
});

const TableComponent = Loadable({
  loader: () => import('choerodon-ui/pro/lib/table'),
  loading: () => <div>loading</div>,
});

export default function asyncCmp(componentType, extProps, children) {
  if (componentType === 'Form') {
    Promise.resolve(import('choerodon-ui/pro/lib/form/style'));
  }
  if (componentType === 'Table') {
    Promise.resolve(import('choerodon-ui/pro/lib/table/style'));
  }
  
  const map = {
    Output,
    Button,
    Tooltip,
    TextField,
    TextArea,
    NumberField,
    Currency,
    IntlField,
    Lov,
    Select,
    SelectBox,
    Password,
    CheckBox,
    Radio,
    Switch,
    ColorPicker,
    DatePicker,
    DateTimePicker,
    TimePicker,
    WeekPicker,
    MonthPicker,
    YearPicker,
    EmailField,
    UrlField,
    Row,
    Form: FormComponent,
    Table: TableComponent,
    FormString: TextField,
    FormNumber: NumberField,
    FormDate: DatePicker,
    FormSelect: Select,
    TableButton: Button,
  };
  const cmp = map[componentType];
  return React.createElement(cmp, extProps, children);
}

export function getCmpByField(field) {
  const { type, bind, lookupCode, lookupUrl, lovCode } = field;
  if (lookupCode || isString(lookupUrl) || (lovCode && type !== 'object')) {
    return 'Select';
  }
  if (typeof lovCode !== 'undefined') {
    return 'Lov';
  }
  if (bind) {
    return 'Output';
  }
  switch (type) {
    case 'boolean':
      return 'CheckBox';
    case 'number':
      return 'NumberField';
    case 'date':
      return 'DatePicker';
    case 'dateTime':
      return 'DateTimePicker';
    case 'week':
      return 'WeekPicker';
    case 'month':
      return 'MonthPicker';
    case 'year':
      return 'YearPicker';
    case 'intl':
      return 'IntlField';
    case 'email':
      return 'EmailField';
    case 'url':
      return 'UrlField';
    case 'string':
      return 'TextField';
    default:
      return 'TextField';
  }
}

export function getFucFromBehaviour(behaviour, dataSet, cb) {
  const { name, page, drawer, layoutId, script } = behaviour;
  if (name === 'openPage') {
    const LINK_MAP = {
      REACT: `/${page.url}`,
      HTML: `/iframe/${page.functionCode}`,
      PAGE: `/hap-modeling/metadata/${page.url}`,
    };

    const link = LINK_MAP[page.type] || '/';
    return () => cb('openPage', link);
  } else if (name === 'openLayout') {
    return () => cb('openLayout', layoutId, drawer);
  } else if (name === 'evalFunction') {
    // eslint-disable-next-line no-new-func
    const temp = new Function('dataSet', script);
    return () => temp(dataSet);
  }
}

export function getBtnByField(field, dataSet, cb) {
  const iconOnly = field.get('iconOnly');
  const method = field.get('method');
  const props = field.get('props');
  const behaviour = field.get('behaviour');
  if (method === 'row_delete') {
    return asyncCmp('Tooltip', { title: '删除' }, asyncCmp('Button', { funcType: 'flat', icon: 'delete', onClick: () => { dataSet.delete(dataSet.current); } }));
  }
  if (iconOnly) {
    return asyncCmp(
      'Tooltip',
      { title: props.children },
      asyncCmp(
        'Button',
        { ...omit(props, 'children'), onClick: getFucFromBehaviour(behaviour, dataSet, cb) },
      ),
    );
  } else {
    return asyncCmp(
      'Button',
      { ...props, onClick: getFucFromBehaviour(behaviour, dataSet, cb) },
      props.children,
    );
  }
}
