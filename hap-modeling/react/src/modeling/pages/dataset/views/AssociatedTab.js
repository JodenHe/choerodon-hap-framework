import React, { Component } from 'react';
import { inject, observer } from 'mobx-react';
import { Button, Form, Select, TextField, Radio } from 'choerodon-ui/pro';
import AssociatedStore from '../stores/Associated';

const { Option, OptGroup } = Select;
const OPERATIONS_MAP = [
  {
    code: 'equal',
    title: '等于',
  },
  {
    code: 'unEqual',
    title: '不等于',
  },
  {
    code: 'contains',
    title: '包含',
  },
  {
    code: 'exclusive',
    title: '不包含',
  },
  {
    code: 'startWith',
    title: '起始字符',
  },
  {
    code: 'empty',
    title: '为空',
  },
  {
    code: 'nonEmpty',
    title: '不为空',
  },
];
const TYPE_MAP = [
  {
    code: 'fixedValue',
    title: '固定值',
  },
  {
    code: 'urlParam',
    title: 'URL参数',
  },
  // {
  //   code: 'sysParam',
  //   title: '系统参数',
  // },
  // {
  //   code: 'businessParam',
  //   title: '业务参数',
  // },
  {
    code: 'field',
    title: '字段',
  },
];

@inject('associatedDS', 'tableDS')
@observer
export default class AssociatedTab extends Component {
  state = {
    showLogic: false,
  };

  getPathTableName() {
    const { tableDS } = this.props;
    let record = tableDS.current;
    const res = [];
    if (record) {
      res.push(record.get('tableKey'));
      while (record && record.get('parentKey')) {
        res.push(record.get('parentKey'));
        // eslint-disable-next-line no-loop-func
        record = tableDS.find(r => r.get('tableKey') === record.get('parentKey'));
      }
    }
    return res;
  }

  handleAddRow = () => {
    const created = this.props.associatedDS.create();
    created.set('operation', 'equal');
    created.set('type', 'fixedValue');
    created.set('value', null);
    created.set('field', null);
  };

  handleDeleteRow(record) {
    this.props.associatedDS.remove(record);
    // this.forceUpdate();
  }

  handleAddLogic = () => {
    this.setState({ showLogic: true });
  };

  handleDeleteLogin = () => {
    this.setState({ showLogic: false });
  };

  handlePrimaryTableFieldChange(record, index, value) {
    let tableName;
    let v;
    if (value) {
      const values = value.split('|');
      // eslint-disable-next-line prefer-destructuring
      tableName = values[0];
      // eslint-disable-next-line prefer-destructuring
      v = values[1];
    }
    record.set('field', v);
    record.set('table', tableName);
  }

  handleFieldChange(record, index, value) {
    record.set('field', value);
  }

  handleOperationChange(record, index, value) {
    record.set('operation', value);
  }

  handleTypeChange(record, index, value) {
    record.set('type', value);
    // if (value === 'field') {
    //   AssociatedStore.setFieldsAll(AssociatedStore.fields);
    // }
  }

  handleValueChange(record, index, value) {
    let tableName;
    let v;
    if (value) {
      const values = value.split('|');
      // eslint-disable-next-line prefer-destructuring
      tableName = values[0];
      // eslint-disable-next-line prefer-destructuring
      v = values[1];
    }
    record.set('value', v);
    record.set('valueTable', tableName);
  }

  renderNumberOrRelation(index, isNumber = this.state.showLogic) {
    if (!isNumber) {
      return index === 0 ? <span colSpan={1} /> : <span colSpan={1} style={{ lineHeight: '30px' }}>AND</span>;
    }
    return <span colSpan={1} style={{ lineHeight: '30px' }}>{index + 1}</span>;
  }

  renderField(record, index) {
    const { tableDS } = this.props;
    if (tableDS.current && !tableDS.current.get('parentKey')) {
      // 主表
      return (
        <Select
          key={`${record.id}-field-select`}
          colSpan={4}
          label="请选择"
          value={`${record.get('table')}|${record.get('field')}`}
          onChange={this.handlePrimaryTableFieldChange.bind(this, record, index)}
        >
          {
            AssociatedStore.fieldsAll.map(({ tableName, columns }) => (
              <OptGroup label={tableName}>
                {
                  columns.map(({ columnName, description }) => (
                    <Option key={columnName} value={`${tableName}|${columnName}`}>{`${description} | ${columnName}`}</Option>
                  ))
                }
              </OptGroup>
            ))
          }
        </Select>
      );
    }
    return (
      <Select
        key={`${record.id}-field-select`}
        colSpan={4}
        label="请选择"
        value={record.get('field')}
        onChange={this.handleFieldChange.bind(this, record, index)}
      >
        {
          AssociatedStore.fields.map(({ columnName, description }) => (
            <Option key={columnName} value={columnName}>{`${description} | ${columnName}`}</Option>
          ))
        }
      </Select>
    );
  }

  renderType(record, index) {
    const operation = record.get('operation');
    if (operation === 'nonEmpty' || operation === 'empty') {
      return <span colSpan={2} />;
    }
    return (
      <Select
        label=""
        key={`${record.id}-type-select`}
        colSpan={2}
        clearButton={false}
        value={record.get('type')}
        onChange={this.handleTypeChange.bind(this, record, index)}
      >
        {
          TYPE_MAP.map(({ code, title }) => (
            <Option key={code} value={code}>{title}</Option>
          ))
        }
      </Select>
    );
  }

  renderValue(record, index) {
    const operation = record.get('operation');
    if (operation === 'nonEmpty' || operation === 'empty') {
      return <span colSpan={4} />;
    }
    const type = record.get('type');
    if (type === 'fixedValue') {
      return (
        <TextField key={`${record.id}-value-text-textField`} colSpan={4} label="填写文本" value={record.get('value')} onChange={this.handleValueChange.bind(this, record, index)} />
      );
    }
    if (type === 'urlParam') {
      return (
        <TextField
          key={`${record.id}-value-url-textField`}
          colSpan={4}
          label="填写文本"
          value={record.get('value')}
          addonBefore="url"
          onChange={this.handleValueChange.bind(this, record, index)}
        />
      );
    }
    return (
      <Select key={`${record.id}-value-select`} colSpan={4} label="请选择" value={`${record.get('valueTable')}|${record.get('value')}`} onChange={this.handleValueChange.bind(this, record, index)}>
        {
          AssociatedStore.fieldsAll.map(({ tableName, columns }) => (
            <OptGroup label={tableName}>
              {
                columns.map(({ columnName, description }) => (
                  <Option key={columnName} value={`${tableName}|${columnName}`}>{`${description} | ${columnName}`}</Option>
                ))
              }
            </OptGroup>
          ))
        }
      </Select>
    );
  }

  renderAssociationRelation() {
    const { tableDS } = this.props;
    const currentTable = tableDS.current;
    return `${currentTable.get('parentKey')}.${currentTable.get('masterColumnName')} = ${currentTable.get('tableKey')}.${currentTable.get('relationColumnName')}`;
  }

  render() {
    const { associatedDS, tableDS } = this.props;
    const { showLogic } = this.state;

    return (
      <div className="associated" style={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
        {
          tableDS.current.get('parentKey') && (
            <div className="associated-props" style={{ borderBottom: '1px solid #ddd', padding: '4px 27px 27px 27px' }}>
              <h2 style={{ fontSize: '16px', lineHeight: '24px', color: '#000', fontWeight: '600' }}>关联属性</h2>
              <div style={{ marginTop: 16 }}>
                <span style={{ marginRight: 8 }}>关联方式：</span>
                <Radio dataSet={tableDS} name="join" value="inner">Inner Join</Radio>
                <Radio dataSet={tableDS} name="join" value="left">Left Join</Radio>
              </div>
              <div style={{ marginTop: 16 }}>
                <span style={{ marginRight: 8 }}>关联关系：</span>
                <span>{this.renderAssociationRelation()}</span>
              </div>
            </div>
          )
        }

        <div className="associated-conditions" style={{ flex: 1, padding: '22px 27px 27px 27px', overflow: 'auto' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between' }}>
            <h2 style={{ fontSize: '16px', lineHeight: '24px', color: '#000', fontWeight: '600' }}>过滤条件</h2>
            <Button funcType="flat" color="blue" icon="playlist_add" onClick={this.handleAddRow}>添加条件</Button>
          </div>
          <Form labelLayout="placeholder" columns={14} className="associated-form">
            {
              associatedDS.data.map((r, index) => ([
                this.renderNumberOrRelation(index),
                this.renderField(r, index),
                <Select
                  label=""
                  key={`${r.id}-operation-select`}
                  clearButton={false}
                  colSpan={2}
                  value={r.get('operation')}
                  onChange={this.handleOperationChange.bind(this, r, index)}
                >
                  {
                    OPERATIONS_MAP.map(({ code, title }) => (
                      <Option key={code} value={code}>{title}</Option>
                    ))
                  }
                </Select>,
                this.renderType(r, index),
                this.renderValue(r, index),
                <Button funcType="flat" icon="delete" colSpan={1} onClick={this.handleDeleteRow.bind(this, r)} />,
              ]))
            }
          </Form>
          {/* <Table dataSet={associatedDS}>
            <Table.Column name="value" editor />
          </Table> */}

          <div>
            <div>
              {
                !showLogic && (
                  <Button funcType="flat" color="blue" icon="playlist_add" onClick={this.handleAddLogic} style={{ marginTop: 20 }}>添加筛选逻辑</Button>
                )
              }
            </div>
            {
              showLogic && [
                <div style={{ marginTop: 20, color: 'rgba(0, 0, 0, 0.65)' }}>筛选逻辑</div>,
                <Form labelLayout="placeholder" columns={14} className="logic-show">
                  <TextField label="筛选逻辑" colSpan={13} dataSet={tableDS} name="filterLogic" />
                  <Button funcType="flat" icon="delete" colSpan={1} onClick={this.handleDeleteLogin} />
                </Form>,
                <div style={{ color: 'rgba(0, 0, 0, 0.65)', fontSize: '12px', lineHeight: '17px' }}>使用 AND 和 OR 合并筛选器条件行。示例： (1 AND 2) OR 3</div>,
              ]
            }
          </div>
        </div>
      </div>
    );
  }
}
