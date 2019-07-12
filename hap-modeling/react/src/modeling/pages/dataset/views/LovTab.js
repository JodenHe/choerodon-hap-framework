import React, { Component } from 'react';
import { inject, observer } from 'mobx-react';
import { Button, DataSet, Modal, Table, Tooltip } from 'choerodon-ui/pro';
import LovModal from './LovModal';
import EditFieldModal from './EditFieldModal';
import LovDataSet from '../stores/LovDataSet';

const { Column } = Table;

function filterLovCode(record) {
  return record.get('lovCode');
}

@inject('fieldDS')
@observer
export default class LovTab extends Component {
  lovDS = new DataSet(LovDataSet);

  lovModalKey = Modal.key();

  editFieldModalKey = Modal.key();

  handleEditMore = () => {
    const { fieldDS } = this.props;
    Modal.open({
      key: this.editFieldModalKey,
      title: '编辑更多属性',
      drawer: true,
      destoryOnClose: true,
      style: { width: '4.3rem' },
      children: <EditFieldModal fieldDS={fieldDS} />,
      okText: '保存',
    });
  };

  handleOpenLovModal = () => {
    const { lovDS, props: { fieldDS } } = this;
    Modal.open({
      key: this.lovModalKey,
      title: '查询LOV_CODE',
      drawer: true,
      destoryOnClose: true,
      children: <LovModal lovDS={lovDS} fieldDS={fieldDS} />,
    });
  };

  render() {
    const { fieldDS } = this.props;
    const addLovBtn = (
      <Button
        key="add"
        funcType="flat"
        color="blue"
        icon="playlist_add"
        onClick={this.handleOpenLovModal}
      >
        增加LOV
      </Button>
    );

    return (
      <div className="field" style={{ height: '100%', padding: '0 28px 28px', overflow: 'auto' }}>
        <Table
          dataSet={fieldDS}
          buttons={[addLovBtn, 'delete']}
          filter={filterLovCode}
        >
          <Column name="name" editor />
          <Column name="description" width={200} />
          <Column name="label" editor />
          <Column name="lovCode" width={200} />
          <Column name="field" width={800} />
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
