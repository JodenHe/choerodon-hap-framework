import React, { PureComponent } from 'react';
import { observer } from 'mobx-react';
import { axiosPro as axios } from '@choerodon/boot';
import { Tabs, Table, DataSet, Icon, Button, Modal } from 'choerodon-ui/pro';
import { ApproveChainLineDataSet } from '../stores';

const { TabPane } = Tabs;
const { Column } = Table;

async function createNewDataSet(key, name) {
  const dataset = new DataSet(ApproveChainLineDataSet);
  const response = await axios.post(`/wfl/approve/chain/header/query?processKey=${key}&usertaskId=${name}`);
  if (response.rows && response.rows.length) {
    dataset.getField('approveChainId').set('defaultValue', response.rows[0].approveChainId);
  }
  // dataset.submitUrl = `/dataset/ApproveChainLine/mutations?processKey=${key}&usertaskId=${name}`;
  return dataset;
}

@observer
export default class Index extends PureComponent {
  constructor(props) {
    super(props);
    props.modal.handleOk(this.handleOkInModal.bind(this));
    this.state = {
      tabNodes: [],
      loading: false,
      currentKey: undefined,
    };
    this.datasets = {};
  }
  
  componentDidMount() {
    this.loadTabNodes();
  }

  async loadTabNodes() {
    const { modelId, name } = this.props;
    try {
      const res = await axios.get(`/wfl/repository/model/node?latest=true&size=99999&modelId=${modelId}`);
      if (res.success) {
        const { rows } = res;
        this.setState({ tabNodes: rows });
        if (rows.length) {
          this.setState({ currentKey: rows[0].nodeId });
          this.datasets[rows[0].nodeId] = await createNewDataSet(name, rows[0].nodeId);
          this.datasets[rows[0].nodeId].queryParameter.usertaskId = rows[0].nodeId;
          this.datasets[rows[0].nodeId].queryParameter.processKey = name;
          await this.datasets[rows[0].nodeId].query();
          this.forceUpdate();
        }
      } else {
        // show error
      }
    } catch (error) {
      // show error
    }
  }

  handleOkInModal() {
    const { datasets } = this;
    const keys = Object.keys(datasets);
    if (keys.some(key => datasets[key] && datasets[key].isModified())) {
      Modal.warning('关闭弹出框会丢失已修改但未保存的内容，请保存后再操作！');
      return false;
    } else {
      return true;
    }
  }

  handleChangeTab = async (key) => {
    const { datasets, props: { name }, state: { currentKey } } = this;
    if (datasets[currentKey] && datasets[currentKey].isModified()) {
      Modal.warning('离开当前标签页会丢失已修改但未保存的内容，请保存后再操作！');
      return;
    }
    if (key && !datasets[key]) {
      this.setState({ currentKey: key });
      datasets[key] = await createNewDataSet(name, key);
      datasets[key].queryParameter.usertaskId = key;
      datasets[key].queryParameter.processKey = name;
      await datasets[key].query();
      this.forceUpdate();
    } else if (key && datasets[key]) {
      this.setState({ currentKey: key });
    }
  };

  handleSave = async () => {
    const { datasets, state: { currentKey }, props: { name } } = this;
    const currentDs = datasets[currentKey];
    if (currentDs) {
      currentDs.forEach((record) => {
        record.set('usertaskId', currentKey);
        record.set('processKey', name);
      });
      this.setState({ loading: true });
      await currentDs.submit();
      this.setState({ loading: false });
    }
  }

  render() {
    const { tabNodes, loading, currentKey } = this.state;
    const { datasets } = this;
    const saveBtn = (
      <Button
        funcType="flat"
        color="blue"
        icon="save"
        onClick={this.handleSave}
        loading={loading}
      >
        保存
      </Button>
    );

    return (
      <div className="wf-model-editApproveChain-modal">
        {
          tabNodes.length > 0 ? (
            <Tabs onChange={this.handleChangeTab} activeKey={currentKey}>
              {
                tabNodes.map(({ name, nodeId }) => (
                  <TabPane tab={<span><Icon type="manage_person" />{name}</span>} key={nodeId}>
                    {
                      datasets[nodeId] && (
                        <Table
                          dataSet={datasets[nodeId]}
                          buttons={['add', saveBtn, 'delete']}
                        >
                          <Column name="name" editor />
                          <Column name="assignee" editor />
                          <Column name="assignGroup" editor />
                          <Column name="formKey" editor />
                          <Column name="sequence" editor />
                          <Column name="skipExpression" editor />
                          <Column name="breakOnSkip" editor />
                          <Column name="enableFlag" editor />
                          <Column name="description" editor />
                        </Table>
                      )
                    }
                  </TabPane>
                ))
              }
            </Tabs>
          ) : (
            <div>没有可用的人工任务节点!</div>
          )
        }
      </div>
    );
  }
}
