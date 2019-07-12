import React, { Component } from 'react';
import { Col, Form, Icon, Output, Row, Select, SelectBox, Switch, TextField } from 'choerodon-ui/pro';
import EditorSelect from '../../editors';
import { getFieldConfigByName } from '../../utils';
import { getEditor, getEditorByField, getEditorType } from '../../editors/editorMapping';
import getProperties from '../../editors/propertiesMapping';

export default function (record, ownerDataSet) {
  const { Option } = Select;
  const name = record.get('props.name');
  const field = getFieldConfigByName(ownerDataSet, name);

  function handleEditableChange(value) {
    if (value) {
      record.set('editor.type', getEditorByField(field));
    } else {
      record.set('editor', undefined);
    }
  }

  function handleLockChange(value) {
    if (value) {
      record.set('props.lock', 'left');
    } else {
      record.set('props.lock', false);
    }
  }

  const editor = !!record.get('editor');
  const lock = !!record.get('props.lock');
  const type = getEditorType({ record, field });

  const properties = [
    <Form key="base" header="基本属性">
      <Output key="datasetName" name="datasetName" record={ownerDataSet} label="数据源" />
      <TextField key="props.header" name="props.header" record={record} label="列头" />
      <Row key="props.lock" gutter={10} label="固定">
        <Col span={11}>
          <Switch checked={lock} onChange={handleLockChange} />
        </Col>
        <Col span={13}>
          {
            lock && (
              <SelectBox mode="button" name="props.lock" record={record}>
                <Option value="left"><Icon type="format_align_left" /></Option>
                <Option value="right"><Icon type="format_align_right" /></Option>
              </SelectBox>
            )
          }
        </Col>
      </Row>
      <SelectBox key="props.align" mode="button" name="props.align" record={record} label="对齐方式">
        <Option value="left"><Icon type="format_align_left" /></Option>
        <Option value="center"><Icon type="format_align_center" /></Option>
        <Option value="right"><Icon type="format_align_right" /></Option>
      </SelectBox>
      <Switch key="props.resizable" name="props.resizable" record={record} label="可调整列宽" />
      <Switch key="props.sortable" name="props.sortable" record={record} label="可排序" />
      <Switch key="editable" checked={editor} onChange={handleEditableChange} label="可编辑" />
      {editor && EditorSelect(record)}
    </Form>,
  ];
  if (editor) {
    properties.push(
      <Form key="editors" header="编辑器属性">
        {getProperties(getEditor(type).properties(), { record, field, type })}
      </Form>,
    );
  }
  return properties;
}
