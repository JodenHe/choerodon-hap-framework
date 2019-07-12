import React, { PureComponent } from 'react';
import { observer } from 'mobx-react';
import { ContentPro as Content, $l, axiosPro as axios } from '@choerodon/boot';
import { Button, Modal, DataSet, Form, DatePicker, Lov } from 'choerodon-ui/pro';
import PreferencesDataSet from './stores/PreferencesDataSet';

const PREFERENCES_MAP = {
  deliver_start_date: 'deliverStartDate',
  deliver_end_date: 'deliverEndDate',
  auto_deliver: 'autoDelegate',
};

@observer
export default class Index extends PureComponent {
  preferencesDataSet;

  constructor(props) {
    super(props);
    this.preferencesDataSet = new DataSet(PreferencesDataSet);
  }

  findDataIndex(name) {
    return this.preferencesDataSet.findIndex(r => r.get('preferences') === name);
  }

  handleSave() {
    const obj = this.preferencesDataSet.data.slice().map(r => r.data);
    const deliverStartDateRecord = obj.find(r => r.preferences === PREFERENCES_MAP.deliver_start_date);
    const deliverEndDateRecord = obj.find(r => r.preferences === PREFERENCES_MAP.deliver_end_date);
    deliverStartDateRecord.preferencesValue = deliverStartDateRecord.preferencesValue ? deliverStartDateRecord.preferencesValue.format('YYYY-MM-DD HH:mm:ss') : null;
    deliverEndDateRecord.preferencesValue = deliverEndDateRecord.preferencesValue ? deliverEndDateRecord.preferencesValue.format('YYYY-MM-DD HH:mm:ss') : null;
    axios.post('/sys/preferences/savePreferences', obj)
      .then((res) => {
        if (res.success) {
          Modal.info($l('hap.tip.success'));
          // this.preferencesDataSet.splice(0, this.preferencesDataSet.totalCount);
          // this.preferencesDataSet.loadData(res.rows);
          this.preferencesDataSet.query();
        } else {
          Modal.warning(res.message);
        }
      });
  }

  render() {
    return (
      <Content>
        <Form labelWidth={125} style={{ width: 450, margin: 'auto' }}>
          <DatePicker
            label={$l('preference.deliverstartdate')}
            dataSet={this.preferencesDataSet}
            name="preferencesValue"
            mode="dateTime"
            dataIndex={this.findDataIndex(PREFERENCES_MAP.deliver_start_date)}
            help={$l('preference.deliverstartdate.description')}
          />

          <DatePicker
            label={$l('preference.deliverenddate')}
            dataSet={this.preferencesDataSet}
            name="preferencesValue"
            mode="dateTime"
            dataIndex={this.findDataIndex(PREFERENCES_MAP.deliver_end_date)}
            help={$l('preference.deliverenddate.description')}
          />

          <Lov
            label={$l('preference.autodeliver')}
            dataSet={this.preferencesDataSet}
            name="preferencesValue"
            dataIndex={this.findDataIndex(PREFERENCES_MAP.auto_deliver)}
            help={$l('preference.autodeliver.description')}
          />

          <Button color="blue" onClick={() => this.handleSave()}>{$l('hap.save')}</Button>
        </Form>
      </Content>
    );
  }
}
