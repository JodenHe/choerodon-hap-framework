import React, { Component } from 'react';
import { Table } from 'choerodon-ui/pro';

const { Column } = Table;

export default class LookupModal extends Component {
  constructor(props) {
    super(props);
    props.modal.handleOk(this.handleQuery.bind(this));
  }

  async handleQuery() {
    const { lookupDS, fieldDS } = this.props;
    const selectedLovs = lookupDS.selected;

    if (selectedLovs && selectedLovs.length) {
      fieldDS.current.set('lookupCode', selectedLovs[0].get('code'));
    }
  }

  render() {
    const { lookupDS } = this.props;

    return (
      <div className="msg-wrap">
        <Table dataSet={lookupDS} queryFieldsLimit={2}>
          <Column name="code" />
          <Column name="description" />
        </Table>
      </div>
    );
  }
}
