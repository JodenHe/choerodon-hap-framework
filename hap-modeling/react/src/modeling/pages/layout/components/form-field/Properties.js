import React, { Component } from 'react';
import { Form, Output, TextField } from 'choerodon-ui/pro';
import { getFieldConfigByName } from '../../utils';
import EditorSelect from '../../editors';
import { getEditor, getEditorType } from '../../editors/editorMapping';
import getProperties from '../../editors/propertiesMapping';

export function fieldCommonProperties(record, ownerDataSet, field) {
  const editor = EditorSelect(record);
  const type = getEditorType({ record, field });
  return (
    <Form key="base">
      <Output key="datasetName" name="datasetName" record={ownerDataSet} label="数据源" />
      <TextField key="props.label" name="props.label" record={record} label="标签" placeholder={field.get('label')} />
      {editor}
      {getProperties(getEditor(type).properties(), { record, field, type })}
    </Form>
  );
}

export default function (record, ownerDataSet) {
  const name = record.get('props.name');
  const field = getFieldConfigByName(ownerDataSet, name);
  return fieldCommonProperties(record, ownerDataSet, field);
}
