import React, { Component } from 'react';
import { Table } from 'choerodon-ui/pro';
import { axiosPro as axios } from '@choerodon/boot';

const { Column } = Table;

export default class LovModal extends Component {
  constructor(props) {
    super(props);
    props.modal.handleOk(this.handleQuery.bind(this));
  }

  async handleQuery() {
    const { lovDS, fieldDS } = this.props;
    const selectedLovs = lovDS.selected;

    try {
      const res = await axios.post('/modeling/dataset/lovitem/queries',
        selectedLovs.map(r => r.get('code')).map(lovCode => ({ code: lovCode })));
      res.forEach((lov) => {
        const fieldArr = lov.lovItems.map(o => o.gridFieldName);
        const record = fieldDS.find(r => r.get('lovCode') === lov.code);
        if (record) {
          record.set('field', fieldArr.join(', '));
        } else {
          fieldDS.create({
            lovCode: lov.code,
            description: lov.description,
            type: 'object',
            field: fieldArr.join(', '),
            label: lov.description,
          });
        }
      });
      fieldDS.validate();
      return true;
    } catch (error) {
      return false;
    }
  }

  render() {
    const { lovDS } = this.props;

    return (
      <div className="msg-wrap">
        <Table dataSet={lovDS} queryFieldsLimit={2}>
          <Column name="code" minWidth={150} />
          <Column name="description" minWidth={150} />
        </Table>
      </div>
    );
  }
}
