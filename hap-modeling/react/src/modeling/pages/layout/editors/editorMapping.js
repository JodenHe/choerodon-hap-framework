import React from 'react';
import isString from 'lodash/isString';
import CheckBox from './check-box';
import ColorPicker from './color-picker';
import Currency from './currency';
import DatePicker from './date-picker';
import DateTimePicker from './date-time-picker';
import EmailField from './email-field';
import IntlField from './intl-field';
import Lov from './lov';
import MonthPicker from './month-picker';
import NumberField from './number-field';
import Output from './output';
import Password from './password';
import Radio from './radio';
import Select from './select';
import SelectBox from './select-box';
import Switch from './switch';
import TextArea from './text-area';
import TextField from './text-field';
import TimePicker from './time-picker';
import UrlField from './url-field';
import WeekPicker from './week-picker';
import YearPicker from './year-picker';

const editors = {
  Output,
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
};

export function getEditorByField(field) {
  const type = field.get('type');
  const bind = field.get('bind');
  const lookupCode = field.get('lookupCode');
  const lookupUrl = field.get('lookupUrl');
  const lovCode = field.get('lovCode');
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

export function getEditorType({ record, field }) {
  const editor = record && record.get('editor.type');
  return editor || getEditorByField(field);
}

export function getEditor(key) {
  return editors[key];
}

export function getEditorByRecordAndField(props) {
  return getEditor(getEditorType(props));
}

export function getOptions() {
  const { Option } = Select.editor;
  return Object.keys(editors).map(key => (
    <Option key={key} value={key}>{editors[key].description}</Option>
  ));
}
