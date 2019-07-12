import React, { Component } from 'react';
import { Form, Output, TextField } from 'choerodon-ui/pro';
import { getQueryFieldConfigByName } from '../../utils';
import { fieldCommonProperties } from '../form-field/Properties';

export default function (record, ownerDataSet) {
  const name = record.get('props.name');
  const field = getQueryFieldConfigByName(ownerDataSet, name);
  return fieldCommonProperties(record, ownerDataSet, field);
}
