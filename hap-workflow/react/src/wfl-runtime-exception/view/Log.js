import { Icon, Modal, Table } from 'choerodon-ui/pro';
import { $l } from '@choerodon/boot';
import { observer } from 'mobx-react';
import React from 'react';

const { Column } = Table;
const modalKey = Modal.key();

function openModal(text) {
  Modal.open({
    key: modalKey,
    title: $l('wfl.runtime.exception.message'),
    style: {
      width: 800,
    },
    drawer: true,
    children: <p>{text}</p>,
  });
}

function renderLogMessage({ text }) {
  return (
    <Icon style={{ cursor: 'pointer' }} type="visibility" onClick={() => openModal(text)} />
  );
}

const Log = (props) => {
  const { ds } = props;
  return (
    <Table dataSet={ds} selectionMode="click">
      <Column name="procId" />
      <Column name="procDefName" />
      <Column name="messStr" renderer={renderLogMessage} />
      <Column name="duedate" />
    </Table>
  );
};

export default observer(Log);
