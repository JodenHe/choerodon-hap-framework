import React, { Component } from 'react';
import _ from 'lodash';
import uuid from 'uuid/v4';
import { axiosPro as axios } from '@choerodon/boot';
import { observer, inject } from 'mobx-react';
import { Button, Modal, Form, TextField, Select, DataSet, Dropdown, Menu, Icon } from 'choerodon-ui/pro';
import AddDataSetDataSet from './stores/AddDataSetDataSet';

function toHump(name) {
  return name.toLowerCase().replace(/_(\w)/g, (all, letter) => letter.toUpperCase());
}

@inject('tableDS', 'datasetsDS')
@observer
export default class TreeGraph extends Component {
  addDataSetModalKey = Modal.key();

  addDataSetDS = new DataSet(AddDataSetDataSet);

  handleNewDatasetModalOk = async () => {
    const { addDataSetDS, props: { tableDS, datasetsDS } } = this;
    if (await addDataSetDS.current.validate()) {
      const res = await axios.post('/dataset/MetadataColumn/queries?page=1&pagesize=999', { tableName: addDataSetDS.current.get('tableName') });
      const datasetId = uuid();
      const datasetObj = {
        datasetId,
        name: datasetId,
        datasetName: addDataSetDS.current.get('name'),
        primaryKey: toHump(res.rows.find(r => r.primaryKey).columnName || ''),
      };
      datasetsDS.create(datasetObj);
      const tableObj = {
        tableName: addDataSetDS.current.get('tableName'),
        tableKey: addDataSetDS.current.get('tableName'),
      };
      tableDS.create(tableObj);
      return true;
    } else {
      return false;
    }
  };

  handleNewDatasetModalCancel = async () => {
    const { addDataSetDS } = this;
    addDataSetDS.remove(addDataSetDS.current);
  };

  handleClickCreate = () => {
    const { addDataSetDS } = this;
    addDataSetDS.create();
    Modal.open({
      key: this.addDataSetModalKey,
      title: '新增DataSet',
      drawer: true,
      destoryOnClose: true,
      children: (
        <Form dataSet={addDataSetDS}>
          <TextField name="name" />
          <Select name="tableName" placeholder="请选择" required searchable />
        </Form>
      ),
      okText: '新增',
      onOk: this.handleNewDatasetModalOk,
      onCancel: this.handleNewDatasetModalCancel,
    });
  };

  handleClickDelteDataset = (e) => {
    e.stopPropagation();
    const { datasetsDS } = this.props;
    Modal.confirm({
      title: '确认',
      children: '您确认删除当前DataSet',
    }).then((button) => {
      if (button === 'ok') {
        datasetsDS.remove(datasetsDS.current);
      }
    });
  };

  handleChange = ({ key }) => {
    const { datasetsDS } = this.props;
    const index = datasetsDS.findIndex(r => r.get('datasetId') === key);
    if (index !== -1) {
      datasetsDS.locate(index);
    }
  };

  renderDataSetSelect() {
    const { datasetsDS } = this.props;
    if (datasetsDS.current) {
      const menu = (
        <Menu onClick={this.handleChange}>
          {
            datasetsDS.map(r => (
              <Menu.Item key={r.get('datasetId')}>
                <div style={{ width: 100, display: 'flex', marginLeft: -10 }}>
                  <span style={{ overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{r.get('datasetName')}</span>
                  {r === datasetsDS.current 
                    && <Icon type="delete" style={{ position: 'absolute', right: 0, marginRight: 5 }} onClick={this.handleClickDelteDataset} />}
                </div>
              </Menu.Item>
            ))
          }
        </Menu>
      );
      return (
        <Dropdown overlay={menu} trigger={['click']}>
          <span style={{ userSelect: 'none', color: '#3f51b5' }}>{datasetsDS.current.get('datasetName')}<Icon type="baseline-arrow_drop_down" /></span>
        </Dropdown>
      );
    }
    return null;
  }

  render() {
    return (
      <div className="btn-area">
        {this.renderDataSetSelect()}
        <Button
          funcType="flat"
          color="blue"
          icon="playlist_add"
          style={{ marginLeft: 30 }}
          onClick={this.handleClickCreate}
        >
          新建
        </Button>
      </div>
    );
  }
}
