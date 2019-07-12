import React from 'react';
import { Button, Modal, NumberField, Select, Table, TextField, Tooltip } from 'choerodon-ui/pro';
import { $l } from '@choerodon/boot';
import ParameterConfigModal from './ParameterConfigModal.js';

const { Column } = Table;
const modalKey = Modal.key();
const numberField = <NumberField step={1} min={1} />;
const textField = <TextField />;

function dataLengthRenderer(record) {
  if (record.get('display') === 'textBox') {
    return numberField;
  }
}

export default ({ parameterDS, targetId, code, reportData }) => {
  function openParameterConfigModal(parameter) {
    Modal.open({
      modalKey,
      title: $l('hap.edit'),
      drawer: true,
      okText: $l('hap.ok'),
      children: (
        <ParameterConfigModal parameterDS={parameterDS} parameterData={parameter.toJSONData()} reportData={reportData} />
      ),
    });
  }

  async function handleSaveParameter() {
    if (await parameterDS.validate()) {
      if (parameterDS.data.length) {
        parameterDS.data.map((record) => {
          record.set('code', code);
          record.set('targetId', targetId);
          return record;
        });
      }
      await parameterDS.submit();
      return true;
    }
    return false;
  }

  const saveBtn = (
    <Button
      funcType="flat"
      color="blue"
      type="primary"
      icon="save"
      onClick={() => handleSaveParameter()}
    >
      {$l('hap.save')}
    </Button>
  );

  return (
    <Table
      buttons={['add', saveBtn, 'delete']}
      dataSet={parameterDS}
    >
      <Column name="display" editor />
      <Column name="tableFieldName" editor={textField} />
      <Column name="title" editor />
      <Column name="lineNumber" editor={numberField} width={80} />
      <Column name="columnNumber" editor={numberField} width={80} />
      <Column name="displayLength" editor={numberField} width={80} />
      <Column name="labelWidth" editor={numberField} width={100} />
      <Column name="dataLength" editor={record => dataLengthRenderer(record)} width={80} />
      <Column name="required" editor width={80} />
      <Column name="readOnly" editor width={80} />
      <Column name="enabled" editor width={80} />
      <Column
        header={$l('parameterconfig.config')}
        align="center"
        lock="right"
        renderer={({ record, text, name }) => {
          if (record.get('parameterId')) {
            return (
              <Tooltip title={$l('parameterconfig.config')}>
                <Button
                  funcType="flat"
                  icon="mode_edit"
                  onClick={() => openParameterConfigModal(record)}
                />
              </Tooltip>
            );
          }
          return null;
        }}
      />
    </Table>
  );
};
