import React, { Component } from 'react';
import { Form, Icon, Output, Select, SelectBox, TextField } from 'choerodon-ui/pro';

export default function (record, ownerDataSet) {
  const { Option } = Select;

  return (
    <Form key="base">
      <Output key="datasetName" name="datasetName" record={ownerDataSet} label="数据源" />
      <TextField key="props.header" name="props.header" record={record} label="标题" />
      <Select key="props.labelLayout" name="props.labelLayout" record={record} label="标签位置" clearButton={false}>
        <Option value="horizontal">水平</Option>
        <Option value="vertical">垂直</Option>
        <Option value="placeholder">占位符</Option>
        <Option value="none">不显示</Option>
      </Select>
      {
        record.get('props.labelLayout') === 'horizontal' && (
          <SelectBox key="props.labelAlign" mode="button" name="props.labelAlign" record={record} label="标签对齐方式">
            <Option value="left"><Icon type="format_align_left" /></Option>
            <Option value="center"><Icon type="format_align_center" /></Option>
            <Option value="right"><Icon type="format_align_right" /></Option>
          </SelectBox>
        )
      }
    </Form>
  );
}
