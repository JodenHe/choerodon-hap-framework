import React, { Component } from 'react';
import { observer, inject } from 'mobx-react';
import { Table, TextField, Button, Modal, Tooltip, Select, DataSet } from 'choerodon-ui/pro';
import LookupModal from './LookupModal';
import AddFieldsModal from './AddFieldsModal';
import EditFieldModal from './EditFieldModal';
import AddLovFieldsModal from './AddLovFieldsModal';
import LookupDataSet from '../stores/LookupDataSet';

const { Column } = Table;

function renderColumn({ record }) {
  return <span>{`【${record.get('tableKey')}】 - ${record.get('columnName')}`}</span>;
}

@inject('fieldDS', 'queryFieldDS', 'tableDS')
@observer
export default class Field extends Component {
  addFieldModalKey = Modal.key();

  addLovModalKey = Modal.key();

  editFieldModalKey = Modal.key();

  lookupDS = new DataSet(LookupDataSet);

  handleOpenLookupModal = () => {
    const { lookupDS, props: { queryFieldDS } } = this;
    Modal.open({
      key: this.lookupModalKey,
      title: '查询Lookup',
      drawer: true,
      destoryOnClose: true,
      children: <LookupModal lookupDS={lookupDS} fieldDS={queryFieldDS} />,
    });
  };

  handleOpenAddFieldModal = () => {
    const { tableDS, queryFieldDS } = this.props;
    const tables = tableDS.data.map(r => r.get('tableKey'));
    Modal.open({
      key: this.addFieldModalKey,
      title: '添加字段',
      drawer: true,
      destoryOnClose: true,
      style: { width: 900 },
      children: <AddFieldsModal fieldDS={queryFieldDS} tables={tables} queryField />,
      okText: '添加',
    });
  };

  handleOpenLovModal = () => {
    const { queryFieldDS, fieldDS } = this.props;
    Modal.open({
      key: this.addLovModalKey,
      title: '绑定Lov字段',
      drawer: true,
      destoryOnClose: true,
      style: { width: 600 },
      children: (
        <AddLovFieldsModal
          fieldDS={fieldDS}
          queryFieldDS={queryFieldDS}
        />
      ),
      okText: '添加',
    });
  };

  handleClearBind = () => {
    const { queryFieldDS } = this.props;
    if (queryFieldDS.current) {
      queryFieldDS.current.set('bind', undefined);
    }
  };

  handleClearLookup = () => {
    const { queryFieldDS } = this.props;
    if (queryFieldDS.current) {
      queryFieldDS.current.set('lookupCode', undefined);
    }
  };

  handleEditMore = () => {
    const { queryFieldDS } = this.props;
    Modal.open({
      key: this.editFieldModalKey,
      title: '编辑更多属性',
      drawer: true,
      destoryOnClose: true,
      style: { width: '4.3rem' },
      children: <EditFieldModal fieldDS={queryFieldDS} />,
      okText: '保存',
    });
  };

  renderBind = ({ record }) => {
    const btn = (
      <Button key="bind_add_btn" funcType="flat" icon="link2" color="blue" onClick={this.handleOpenLovModal} />
    );
    const clearBtn = (
      <Button key="bind_clear_btn" funcType="flat" icon="baseline-link_off" color="blue" onClick={this.handleClearBind} />
    );
    if (!record.get('bind')) {
      return btn;
    }
    return [
      btn,
      <span key="bind_msg">{record.get('bind')}</span>,
      clearBtn,
    ];
  };

  renderLookup = ({ record }) => {
    const btn = (
      <Button key="lookup_add_btn" funcType="flat" icon="link2" color="blue" onClick={this.handleOpenLookupModal} />
    );
    const clearBtn = (
      <Button key="lookup_clear_btn" funcType="flat" icon="baseline-link_off" color="blue" onClick={this.handleClearLookup} />
    );
    if (!record.get('lookupCode')) {
      return btn;
    }
    return [
      btn,
      <span key="lookup_msg">{record.get('lookupCode')}</span>,
      clearBtn,
    ];
  };

  renderEditorName = (record) => {
    const { tableDS } = this.props;
    const tableRecord = tableDS.find(r => r.get('tableKey') === record.get('tableKey'));
    if (tableRecord && !tableRecord.get('parentKey')) {
      return null;
    }
    return <TextField />;
  };

  render() {
    const { queryFieldDS } = this.props;
    const addField = (
      <Button
        key="add"
        funcType="flat"
        color="blue"
        icon="playlist_add"
        onClick={this.handleOpenAddFieldModal}
      >
        增加属性
      </Button>
    );

    return (
      <div className="field" style={{ height: '100%', padding: '0 28px 28px', overflow: 'auto' }}>
        <Table
          buttons={[addField, 'delete']}
          dataSet={queryFieldDS}
        >
          <Column name="operation" editor={record => (record.get('type') === 'string' ? <Select /> : null)} />
          <Column name="name" editor={this.renderEditorName} />
          <Column name="columnName" renderer={renderColumn} width={200} />
          <Column name="type" editor />
          <Column name="label" editor />
          <Column name="bind" renderer={this.renderBind} width={200} />
          <Column name="lookupCode" renderer={this.renderLookup} width={200} />
          <Column
            header="操作"
            align="center"
            lock="right"
            renderer={() => (
              <Tooltip title="编辑更多属性">
                <Button
                  funcType="flat"
                  icon="mode_edit"
                  onClick={this.handleEditMore}
                />
              </Tooltip>
            )}
          />
        </Table>
      </div>
    );
  }
}
