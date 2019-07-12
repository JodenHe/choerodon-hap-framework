import React from 'react';
import { $l, axiosPro as axios } from '@choerodon/boot';
import { Button, Modal, Table, Form, TextField, DatePicker, NumberField } from 'choerodon-ui/pro';

const { Column } = Table;
const key = Modal.key();

export default ({ vacation }) => {
  let created;

  async function handleOnOkNewVacation() {
    if (await vacation.current.validate()) {
      const res = await axios.post('/wfl/runtime/process-instances/vacation', vacation.current.toJSONData());
      if (res.success) {
        Modal.info($l('hap.tip.success'));
      } else {
        Modal.error(res.message);
        return false;
      }
    } else {
      return false;
    }
    await vacation.query();
  }

  function handleOnCloseNewVacation() {
    if (created) {
      vacation.remove(created);
      created = null;
    }
  }

  function openNewVacationModel() {
    created = vacation.create();
    Modal.open({
      key,
      title: $l('hap.add'),
      drawer: true,
      destroyOnClose: true,
      children: (
        <Form dataSet={vacation}>
          <DatePicker name="startDate" />
          <NumberField name="needDays" />
          <TextField name="leaveReason" />
        </Form>
      ),
      okText: '提交申请',
      onOk: handleOnOkNewVacation,
      onClose: handleOnCloseNewVacation,
    });
  }

  const btn = (<Button funcType="flat" color="blue" onClick={openNewVacationModel} icon="operation_new_feature">新建请假申请</Button>);
  return (
    <Table dataSet={vacation} buttons={[btn]}>
      <Column name="startDate" align="center" />
      <Column name="needDays" align="center" />
      <Column name="leaveReason" align="center" />
    </Table>
  );
};
