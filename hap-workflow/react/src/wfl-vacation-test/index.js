import React, { PureComponent } from 'react';
import { ContentPro as Content, $l } from '@choerodon/boot';
import { Button, Modal, Table, DataSet, Tooltip, Form, TextField, NumberField, Select, DatePicker } from 'choerodon-ui/pro';
import VacationDataSet from './stores/DemoVacationDataSet';
import VacationTest from './view/VacationTest';

export default class Index extends PureComponent {
  vacationDataSet = new DataSet(VacationDataSet);

  render() {
    return (
      <Content>
        <VacationTest vacation={this.vacationDataSet} />
      </Content>
    );
  }
}
