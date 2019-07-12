import React, { Component } from 'react';
import { observer } from 'mobx-react';
import { CheckBox, TextField, Select, Form, NumberField, DatePicker } from 'choerodon-ui/pro';

@observer
export default class EditField extends Component {
  renderRequiredMsg() {
    const { fieldDS } = this.props;
    if (fieldDS.current && fieldDS.current.get('required')) {
      return <TextField name="requiredMessage" />;
    }
    return null;
  }

  renderFormat() {
    const { fieldDS } = this.props;
    if (fieldDS.current && ['date', 'dateTime', 'time', 'week', 'month', 'year'].includes(fieldDS.current.get('type'))) {
      return (
        <TextField name="format" />
      );
    }
    return null;
  }

  renderMaxAndMinAndStep() {
    const { fieldDS } = this.props;
    if (fieldDS.current && ['date', 'dateTime', 'time', 'week', 'month', 'year'].includes(fieldDS.current.get('type'))) {
      return [
        <DatePicker key="date_picker_max" name="max" />,
        <DatePicker key="date_picker_min" name="min" />,
      ];
    } else if (fieldDS.current && fieldDS.current.get('type') === 'number') {
      return [
        <NumberField key="number_field_max" name="max" />,
        <NumberField key="number_field_min" name="min" />,
        <NumberField key="number_field_step" name="step" />,
      ];
    }
    return null;
  }

  renderMaxAndMinLength() {
    const { fieldDS } = this.props;
    if (fieldDS.current && fieldDS.current.get('type') === 'string') {
      return [
        <NumberField key="number_field_max_length" name="maxLength" />,
        <NumberField key="number_field_min_length" name="minLength" />,
      ];
    }
    return null;
  }

  renderUnique() {
    const { fieldDS } = this.props;
    if (fieldDS.current && fieldDS.current.get('unique')) {
      return [
        <CheckBox key="check_box_unique" name="unique" />,
        <TextField key="text_field_unique_string" name="uniqueString" />,
      ];
    } else if (fieldDS.current && !fieldDS.current.get('unique')) {
      return (
        <CheckBox name="unique" />
      );
    }
    return null;
  }

  render() {
    const { fieldDS } = this.props;
    return (
      <Form labelWidth={95} dataSet={fieldDS} style={{ marginRight: 34 }}>
        <Select name="order" />
        {this.renderFormat()}
        {this.renderRequiredMsg()}
        <CheckBox name="readOnly" />
        <TextField name="pattern" />
        <NumberField name="group" />
        <TextField name="defaultValue" />
        <CheckBox name="multiple" />
        {this.renderMaxAndMinLength()}
        {this.renderMaxAndMinAndStep()}
        {this.renderUnique()}
      </Form>
    );
  }
}
