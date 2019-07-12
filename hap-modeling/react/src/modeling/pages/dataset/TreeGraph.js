import React, { Component } from 'react';
import _ from 'lodash';
import uuid from 'uuid/v4';
import classNames from 'classnames';
import { axiosPro as axios } from '@choerodon/boot';
import { observer, inject } from 'mobx-react';
import { Button, Modal, Form, TextField, Select, DataSet } from 'choerodon-ui/pro';
import Node from './Node';
import AddDataSetDataSet from './stores/AddDataSetDataSet';

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
        primaryKey: res.rows.find(r => r.primaryKey).columnName,
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

  handleClickDelteDataset = () => {
    const { datasetsDS } = this.props;
    Modal.confirm({
      title: '确认',
      children: '您确认删除当前DataSet',
    }).then(() => {
      datasetsDS.remove(datasetsDS.current);
    });
  };

  handleClickNode = (node) => {
    const { tableDS } = this.props;
    const index = tableDS.findIndex(r => r.get('tableKey') === node.get('tableKey'));
    if (index !== -1) {
      tableDS.locate(index);
    }
  };

  handleChange = (value) => {
    const { datasetsDS } = this.props;
    const index = datasetsDS.findIndex(r => r.get('datasetId') === value);
    if (index !== -1) {
      datasetsDS.locate(index);
    }
  };

  renderTree() {
    const { tableDS } = this.props;
    const { treeData } = tableDS;
    if (!treeData.length) {
      return null;
    }
    return (
      <div className="tree-wrap">
        <div className="tree-center">
          <Node
            level={0}
            node={treeData[0]}
            parentNode={null}
            isMain
            isLast
            isActive={tableDS.current.get('tableKey') === treeData[0].get('tableKey')}
            onClick={this.handleClickNode}
            tableDS={tableDS}
          />
          <div className="tree-node-children">
            {treeData[0].children && treeData[0].children.map((n, i) => this.renderTreeNode(n, 1, i === treeData[0].children.length - 1, treeData[0]))}
          </div>
        </div>
      </div>
    );
  }

  renderTreeNode(treeNode, level, isLast, parentNode) {
    const { tableDS } = this.props;
    const className = classNames('tree-node', {
      'tree-node-not-last': !isLast,
    });
    if (treeNode.children && treeNode.children.length) {
      return (
        <div key={treeNode.id} className={className}>
          <Node
            level={level}
            node={treeNode}
            parentNode={parentNode}
            isMain={false}
            isLast={isLast}
            isActive={tableDS.current.get('tableKey') === treeNode.get('tableKey')}
            onClick={this.handleClickNode}
            tableDS={this.props.tableDS}
          />
          <div className="tree-node-children">
            {treeNode.children.map((tn, i) => this.renderTreeNode(tn, level + 1, i === treeNode.children.length - 1, treeNode))}
          </div>
        </div>
      );
    }
    return (
      <div key={treeNode.id} className={className}>
        <Node
          level={level}
          node={treeNode}
          parentNode={parentNode}
          isMain={false}
          isLast={isLast}
          isActive={tableDS.current.get('tableKey') === treeNode.get('tableKey')}
          onClick={this.handleClickNode}
          tableDS={tableDS}
        />
      </div>
    );
  }

  renderDataSetSelect() {
    const { datasetsDS } = this.props;
    if (datasetsDS.current) {
      return [
        <Select
          key="btn_area_datasets_select"
          clearButton={false}
          value={datasetsDS.current.get('datasetId')}
          style={{ width: 200, marginLeft: 20 }}
          onChange={this.handleChange}
        >
          {datasetsDS.map(r => (
            <Select.Option
              key={r.get('datasetId')}
              value={r.get('datasetId')}
            >
              {r.get('datasetName')}
            </Select.Option>
          ))}
        </Select>,
        <Button
          key="btn_area_datasets_delete_btn"
          color="blue"
          icon="delete"
          style={{ marginLeft: 20 }}
          onClick={this.handleClickDelteDataset}
        >
          删除
        </Button>,
      ];
    }
    return null;
  }

  render() {
    return (
      <div className="graph-wrap">
        {/* <div className="btn-area">
          <Button
            color="blue"
            icon="playlist_add"
            onClick={this.handleClickCreate}
          >
            新建
          </Button>
          {this.renderDataSetSelect()}
        </div> */}
        <div
          style={{
            // marginTop: 30,
            textAlign: 'center',
            // height: 'calc(100% - 30px)',
            width: '100%',
            height: '100%',
            display: 'table',
          }}
        >
          {this.renderTree()}
          <div className="tree-tip">
            <div className="inner">Inner Join</div>
            <div className="left">Left Join</div>
          </div>
        </div>
      </div>
    );
  }
}
