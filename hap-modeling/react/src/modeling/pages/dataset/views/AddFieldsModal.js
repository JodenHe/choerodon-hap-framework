import React, { Component } from 'react';
import { observer } from 'mobx-react';
import { Table, Tabs, DataSet } from 'choerodon-ui/pro';
import AddFieldsDataSet from '../stores/ModalDataSet';

const { TabPane } = Tabs;
const { Column } = Table;

function createNewDataSet() {
  return new DataSet(AddFieldsDataSet);
}

function toHump(name) {
  return name.toLowerCase().replace(/_(\w)/g, (all, letter) => letter.toUpperCase());
}

@observer
export default class AddFieldsModal extends Component {
  constructor(props) {
    super(props);
    props.modal.handleOk(this.handleAddFields.bind(this));
    this.dataSets = {};
  }

  componentDidMount() {
    this.dataSets[this.props.tables[0]] = createNewDataSet();
    // eslint-disable-next-line prefer-destructuring
    this.dataSets[this.props.tables[0]].queryParameter.tableName = this.props.tables[0];
    this.dataSets[this.props.tables[0]].query();
    this.forceUpdate();
  }

  handleChangeTab = (key) => {
    const { dataSets } = this;
    if (key && !dataSets[key]) {
      dataSets[key] = createNewDataSet();
      dataSets[key].queryParameter.tableName = key;
      dataSets[key].query();
      this.forceUpdate();
    }
  };

  handleAddFields() {
    const TYPE_MAP = {
      VARCHAR: 'string',
      BIGINT: 'number',
      DATETIME: 'date',
      CHAR: 'string',
      LONGTEXT: 'string',
    };
    const dataSetsNameArr = Object.keys(this.dataSets);
    const { fieldDS, queryField } = this.props;
    let total = [];
    dataSetsNameArr.forEach((dsName) => {
      const { selected } = this.dataSets[dsName];
      const newSelected = selected
        .filter(r => fieldDS.find(fr => fr.get('id') === r.get('id')) === undefined)
        .map(re => re.set('tableKey', dsName));
      total = total.concat(newSelected);
    });
    total.map(r => r.set('name', toHump(r.get('columnName')))
      .set('label', r.get('description'))
      .set('required', queryField ? false : !r.get('nullable'))
      .set('type', TYPE_MAP[r.get('typeName')] || 'string')
      .set('operation', 'equals'));
    fieldDS.push(...total);
  }

  render() {
    const { tables } = this.props;
    const { dataSets } = this;

    return (
      <div className="msg-wrap">
        <Tabs onChange={this.handleChangeTab}>
          {
            tables.map(tableName => (
              <TabPane tab={tableName} key={tableName}>
                {
                  dataSets[tableName] && (
                    <Table
                      dataSet={dataSets[tableName]}
                    >
                      <Column name="columnName" />
                      <Column name="displayType" />
                      <Column name="description" />
                      <Column name="nullable" align="center" />
                      <Column name="multiLanguage" align="center" />
                    </Table>
                  )
                }
              </TabPane>
            ))
          }
        </Tabs>
      </div>
    );
  }
}
