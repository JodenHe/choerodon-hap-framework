import React, { Component } from 'react';
import { observer } from 'mobx-react';
import { Table, DataSet } from 'choerodon-ui/pro';
import pic from '../assets/no_lov.svg';
import AddedLovDataSet from '../stores/AddedLovDataSet';

@observer
export default class AddFields extends Component {
  constructor(props) {
    super(props);
    props.modal.handleOk(this.handleAddFields.bind(this));
    this.addedLovDS = new DataSet(AddedLovDataSet);
  }

  componentDidMount() {
    const { fieldDS } = this.props;
    const { addedLovDS } = this;
    addedLovDS.splice(0, addedLovDS.length);

    fieldDS.filter(record => record.get('lovCode') && record.get('name')).forEach((r) => {
      const fields = r.get('field').split(', ');
      fields.forEach((field) => {
        addedLovDS.create({
          field,
          code: r.get('lovCode'),
          name: r.get('name'),
        });
      });
    });
  }

  handleAddFields() {
    const { addedLovDS: { selected }, props: { fieldDS, queryFieldDS } } = this;
    const target = queryFieldDS ? queryFieldDS.current : fieldDS.current;
    selected.forEach((r) => {
      target.set('bind', `${r.get('name')}.${r.get('field')}`);
    });
  }

  render() {
    const { addedLovDS } = this;

    if (!addedLovDS.data.length) {
      return (
        <div
          className="c7n-wrap"
          style={{ width: 200, margin: '0 auto' }}
        >
          <div className="c7n-imgWrap">
            <img src={pic} alt="当前无可绑定LOV" className="c7n-img" />
          </div>
          <div className="c7n-textWrap">
            <h1 className="c7n-title" style={{ fontSize: '24px' }}>
              当前无可绑定LOV
            </h1>
            <div className="c7n-des">
              请去【LOV】页关联LOV
            </div>
          </div>
        </div>
      );
    }

    return (
      <div className="msg-wrap">
        <Table dataSet={addedLovDS}>
          <Table.Column name="code" />
          <Table.Column name="name" />
          <Table.Column name="field" />
        </Table>
      </div>
    );
  }
}
