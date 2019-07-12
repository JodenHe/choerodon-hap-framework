import React, { Component } from 'react';
import { observer, inject } from 'mobx-react';
import { CheckBox, TextField, Form, NumberField, Select, Output } from 'choerodon-ui/pro';

@inject('datasetsDS')
@observer
export default class Msg extends Component {
  handleChange = (value) => {
    const { datasetsDS } = this.props;
    datasetsDS.current.set('parentDS', value);
  };

  renderPageSize() {
    const { datasetsDS } = this.props;
    if (datasetsDS.current && datasetsDS.current.get('paging')) {
      return <NumberField name="pageSize" min={0} step={1} />;
    }
    return null;
  }

  renderSelectionType() {
    const { datasetsDS } = this.props;
    if (datasetsDS.current && datasetsDS.current.get('selection')) {
      return <Select name="selectionType" clearButton={false} />;
    }
    return null;
  }

  renderParentDS() {
    const { datasetsDS } = this.props;
    const parentDsArr = datasetsDS
      .filter(r => r.get('datasetId') !== datasetsDS.current.get('datasetId'));
    if (!parentDsArr.length) {
      return null;
    }
    return (
      <Select
        value={datasetsDS.current.get('parentDS')}
        onChange={this.handleChange}
        label="父级DataSet"
      >
        {
          parentDsArr.map(r => (
            <Select.Option value={r.get('datasetId')}>{r.get('datasetName')}</Select.Option>
          ))
        }
      </Select>
    );
  }

  render() {
    const { datasetsDS } = this.props;
    return (
      <div style={{ height: '100%', overflow: 'auto' }}>
        <div style={{ padding: '4px 28px 20px', borderBottom: '1px solid #d3d3d3' }}>
          <h2 style={{ fontSize: '16px', lineHeight: '24px', color: '#000', fontWeight: '600' }}>常见属性</h2>
          <Form style={{ margin: '0 80px 0 0', width: 520 }} labelWidth={150} dataSet={datasetsDS} className="otherParam-form">
            <TextField name="id" clearButton />
            <Output name="name" />
            <TextField name="primaryKey" clearButton />
            <CheckBox name="autoQuery" />
            <CheckBox name="autoCreate" />
            <CheckBox name="paging" />
            {this.renderPageSize()}
            <CheckBox name="selection" />
            {this.renderSelectionType()}
            {this.renderParentDS()}
          </Form>
        </div>
        <div style={{ padding: '20px 28px', borderBottom: '1px solid #d3d3d3' }}>
          <h2 style={{ fontSize: '16px', lineHeight: '24px', color: '#000', fontWeight: '600' }}>自定义查询属性</h2>
          <Form style={{ margin: '0 80px 0 0', width: 520 }} labelWidth={150} dataSet={datasetsDS} className="otherParam-form">
            <TextField name="queryUrl" clearButton />
            <TextField name="submitUrl" clearButton />
            <TextField name="tlsUrl" clearButton />
          </Form>
        </div>
        <div style={{ padding: '20px 28px', borderBottom: '1px solid #d3d3d3' }}>
          <h2 style={{ fontSize: '16px', lineHeight: '24px', color: '#000', fontWeight: '600' }}>树形相关属性</h2>
          <Form style={{ margin: '0 80px 0 0', width: 520 }} labelWidth={150} dataSet={datasetsDS} className="otherParam-form">
            <TextField name="idField" clearButton />
            <TextField name="parentField" clearButton />
            <TextField name="expandField" clearButton />
            <TextField name="checkField" clearButton />
          </Form>
        </div>
        <div style={{ padding: '20px 28px' }}>
          <h2 style={{ fontSize: '16px', lineHeight: '24px', color: '#000', fontWeight: '600' }}>其他</h2>
          <Form style={{ margin: '0 80px 0 0', width: 520 }} labelWidth={150} dataSet={datasetsDS} className="otherParam-form">
            <CheckBox name="modifiedCheck" />
            <CheckBox name="cacheSelection" />
          </Form>
        </div>
      </div>
    );
  }
}
