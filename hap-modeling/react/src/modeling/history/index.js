import React from 'react';
import { Button, DataSet, Modal, Table, Tabs } from 'choerodon-ui/pro';
import { axiosPro as axios, ContentPro as Content } from '@choerodon/boot';
import MetadataDataSetConfig from './stores/MetadataDataSet';
import MetadataItemDataSetConfig from './stores/MetadataItemDataSet';

const { TabPane } = Tabs;
const metadataItemModalKey = Modal.key();
const changesModalKey = Modal.key();

export default () => {
  const self = {};
  const MetadataDataSet = new DataSet(MetadataDataSetConfig);
  const MetadataItemDataSet = new DataSet(MetadataItemDataSetConfig);

  function openChangesModal(record) {
    const lis = [];
    JSON.parse(record.get('data')).forEach((change) => {
      switch (change.type) {
        case 'CREATE_TABLE':
          lis.push(<li key={Math.random()}>创建表: {change.table.tableName}</li>);
          break;
        case 'DELETE_TABLE':
          lis.push(<li key={Math.random()}>删除表: {change.table.tableName}</li>);
          break;
        case 'DELETE_COLUMN':
          lis.push(<li key={Math.random()}>删除列: {change.column.columnName}</li>);
          break;
        case 'CREATE_COLUMN':
          lis.push(<li key={Math.random()}>创建列: {change.column.columnName}, {change.column.displayType}</li>);
          break;
        case 'CREATE_RELATION':
          lis.push(<li key={Math.random()}>创建关系: {change.relation.uniqueName}</li>);
          break;
        case 'DELETE_RELATION':
          lis.push(<li key={Math.random()}>删除关系: {change.relation.uniqueName}</li>);
          break;
        default:
          lis.push(<li key={Math.random()}>未知操作</li>);
          break;
      }
    });
    Modal.open({
      drawer: true,
      key: changesModalKey,
      title: '修改记录',
      children: (
        <ol>
          {lis}
        </ol>
      ),
    });
  }

  function openMetadataItemModal(r) {
    MetadataItemDataSet.queryParameter = {
      metadataId: r.get('id'),
    };
    MetadataItemDataSet.query();
    Modal.open({
      drawer: true,
      key: metadataItemModalKey,
      title: '历史详情',
      children: (
        <Table dataSet={MetadataItemDataSet} selectionMode="false">
          <Table.Column name="dataVersion" width={80} />
          <Table.Column name="lastUpdateDate" />
          <Table.Column
            name="detail"
            align="center"
            width={100}
            lock="right"
            renderer={({ record }) => (
              <Button funcType="flat" icon="search" color="blue" onClick={() => openChangesModal(record)} />
            )}
          />
        </Table>
      ),
    });
  }

  function apply(commit) {
    const ids = [];
    MetadataDataSet.selected.forEach((record) => {
      ids.push(record.get('id'));
    });
    axios.post(`/metadata/history/apply?commit=${commit}`, ids).then(r => MetadataDataSet.query());
  }

  function handleTabChange(key) {
    if (key === '1') {
      MetadataDataSet.queryParameter.status = 'CHECKOUT';
    } else if (key === '2') {
      MetadataDataSet.queryParameter.status = 'COMMITTED';
    }
    MetadataDataSet.query();
  }

  function exportHistory() {
    const ids = [];
    MetadataDataSet.selected.forEach((record) => {
      ids.push(record.get('id'));
    });
    if (ids.length === 0) {
      return;
    }
    window.location = `/metadata/history/export/history.json?ids=${ids.join(',')}`;
  }

  function handleImportClick() {
    self.input.click();
  }

  function handleImportFileChange() {
    self.uploading = true;
    const file = self.input.files[0];
    const formData = new FormData();
    formData.append('file', file, file.name);
    axios.post('/metadata/history/import', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    }).then((r) => {
      MetadataDataSet.query();
    });
    self.input.type = '';
    self.input.type = 'file';
  }

  MetadataDataSet.queryParameter.status = 'CHECKOUT';
  MetadataDataSet.query();

  return (
    <Content>
      <input
        ref={(node) => {
          self.input = node;
        }}
        accept=".json"
        onChange={handleImportFileChange}
        type="file"
        style={{ display: 'none' }}
      />
      <Tabs onTabClick={handleTabChange}>
        <TabPane tab="已检出" key="1">
          <Table
            dataSet={MetadataDataSet}
            buttons={[
              <Button key="commit" funcType="flat" icon="check_circle" color="blue" onClick={() => apply(true)}>提交</Button>,
              <Button key="rollback" funcType="flat" icon="remove_circle" color="blue" onClick={() => apply(false)}>撤销</Button>,
              <Button key="export" funcType="flat" icon="export" color="blue" onClick={exportHistory}>导出</Button>,
              <Button key="import" funcType="flat" icon="inbox_content" color="blue" onClick={handleImportClick}>导入</Button>,
            ]}
          >
            <Table.Column name="name" />
            <Table.Column name="dataType" />
            <Table.Column name="lockedBy" />
            <Table.Column
              name="detail"
              align="center"
              width={100}
              lock="right"
              renderer={({ record }) => (
                <Button funcType="flat" icon="search" color="blue" onClick={() => openMetadataItemModal(record)} />
              )}
            />
          </Table>
        </TabPane>
        <TabPane tab="已提交" key="2">
          <Table dataSet={MetadataDataSet}>
            <Table.Column name="name" />
            <Table.Column name="dataType" />
            <Table.Column
              name="detail"
              align="center"
              width={100}
              lock="right"
              renderer={({ record }) => (
                <Button funcType="flat" icon="search" color="blue" onClick={() => openMetadataItemModal(record)} />
              )}
            />
          </Table>
        </TabPane>
      </Tabs>
    </Content>
  );
};
