import React, { Component } from 'react';
import _ from 'lodash';
import { Provider, inject, observer } from 'mobx-react';
import { withRouter } from 'react-router-dom';
import { axiosPro as axios, ContentPro as Content } from '@choerodon/boot';
import { Button, DataSet, Tabs, Modal, message } from 'choerodon-ui/pro';
import exception from 'choerodon-ui/pro/lib/util/exception';
import DataSetsDataSet from './stores/DataSetsDataSet';
import LayoutsDataSet from './stores/LayoutsDataSet';
import AssociatedDataSet from './dataset/stores/AssociatedDataSet';
import TableDataSet from './dataset/stores/TableDataSet';
import FieldDataSet from './dataset/stores/FieldDataSet';
import DataSetTab from './dataset';
import Layout from './layout';
import MetadataItemDataSet from './stores/MetadataItemDataSet';
import { getDataToSave } from './utils';
import './index.less';
import ComponentsDataSet from './stores/ComponentsDataSet';
import DataSetSelect from './dataset/DataSetSelect';
import TabStore from './stores/TabStore';

const { TabPane } = Tabs;

@inject('AppState')
@withRouter
@observer
export default class MetadataItem extends Component {
  constructor(props) {
    super(props);
    this.metadataItemCode = _.get(this, 'props.match.params.code');
    // this.metadataItemCode = `/hap-modeling/metadata/${_.get(this, 'props.match.params.code')}`;
    this.state = {
      isEdit: false,
      checkLoading: false,
    };

    this.associatedDS = new DataSet(AssociatedDataSet); // dataset配置中associated Tab页，与table级联
    this.tableDS = new DataSet({
      ...TableDataSet,
      children: {
        filter: this.associatedDS,
      },
    });
    this.fieldDS = new DataSet(FieldDataSet); // dataset配置中field Tab页，与dataset级联
    this.queryFieldDS = new DataSet(FieldDataSet); // dataset配置中queryFields Tab页， 与dataset级联

    this.datasetsDS = new DataSet({
      ...DataSetsDataSet,
      children: {
        tables: this.tableDS,
        fields: this.fieldDS,
        queryFields: this.queryFieldDS,
      },
    }); // datasets表，与tables，fields，queryFields级联

    this.componentsDS = new DataSet(ComponentsDataSet);

    this.layoutsDS = new DataSet({
      ...LayoutsDataSet,
      children: {
        components: this.componentsDS,
      },
    });

    this.metadataItemDS = new DataSet({
      ...MetadataItemDataSet,
      children: {
        layouts: this.layoutsDS,
        datasets: this.datasetsDS,
      },
    });
  }

  componentDidMount() {
    this.initMetadataItem();
  }

  async initMetadataItem() {
    // 获取url中的MetadataItem的id
    const { metadataItemCode } = this;

    // 根据id去获取后端数据，得到一个大的obj
    if (metadataItemCode) {
      try {
        const result = await this.loadMetadataItemById(metadataItemCode);

        if (result.success === false) {
          Modal.info('请确保先创建页面。');
          return;
        }
        // JSON.parse(obj)得到对象  { datasets: [], layouts: [] }

        const metadataItem = result || { datasets: [], layouts: [] };

        // 处理dataset
        const { datasets } = metadataItem;
        this.recoverDatasets(datasets);

        // 把 [{ datasets: [], layouts: [] }] loadData 到MetadataItemDS里，由于头行自动配到dataSetsDS和layoutsDS中
        this.metadataItemDS.loadData([metadataItem]);

        window.console.log(this.componentsDS.treeData);

        const res = await axios.post('/dataset/MetadataPage/queries?page=1&pagesize=10', {});
        if (res.success === false) {
          message.warn('查询当前锁定情况失败，您不能提交您的保存，请稍后再试。');
          return;
        }
        const currentMetadataItem = res.rows.find(r => r.url === metadataItemCode);
        let isEdit;
        if (!currentMetadataItem.lockedBy) {
          isEdit = 'none';
          message.info('当前无人锁定，您可以锁定并编辑页面。');
        } else if (currentMetadataItem.lockedBy === this.props.AppState.getUserInfo.userName) {
          isEdit = 'self';
          message.info('当前页面由您锁定，您可以直接编辑并保存页面。');
        } else {
          isEdit = 'other';
          message.warn(`当前页面被${currentMetadataItem.lockedBy}锁定，您不能保存页面。`);
        }
        this.setState({ isEdit });
      } catch (error) {
        Modal.info('读取页面失败，请稍后再试。');
      }
    }
  }

  /**
   * 遍历datasets数组中的每个dataset对象，遍历其中的每一个field对象，
   * 根据lovCode和lookupCode分别去发请求，得到lov数组和lookup数组
   * 把每个lov和lookup的description和field写回到dataset中
   * @param {*} datasets [ { dataset }, { dataset } ]
   */
  async recoverDatasets(datasets) {
    const lovCodesArr = [];
    const lookupCodesArr = [];
    datasets.forEach((dataset) => {
      dataset.fields.forEach((field) => {
        const { lovCode, lookupCode } = field;
        if (lovCode) {
          lovCodesArr.push(lovCode);
        }
        if (lookupCode) {
          lookupCodesArr.push(lookupCode);
        }
      });
    });

    const lovRes = await axios.post('/modeling/dataset/lovitem/queries',
      lovCodesArr.map(lovCode => ({ code: lovCode })));
    const lookupRes = await axios.post('/modeling/dataset/codevalue/queries',
      lookupCodesArr.map(lookupCode => ({ code: lookupCode })));

    // 请求回来后把description和field填充回每个datasets中的dataset中的field里
    datasets.forEach((dataset) => {
      dataset.fields.forEach((field) => {
        const { lovCode, lookupCode } = field;
        if (lovCode) {
          const lov = lovRes.find(l => l.code === lovCode);
          if (lov) {
            field.description = lov.description;
            field.field = lov.lovItems.map(o => o.gridFieldName).join(', ');
          }
        }
        if (lookupCode) {
          const lookup = lookupRes.find(l => l.code === lookupCode);
          if (lookup) {
            field.description = lookup.description;
            field.field = lookup.codeValues.map(o => o.meaning).join(', ');
          }
        }
      });
    });
  }

  loadMetadataItemById = metadataItemCode => axios.get(`/PageDefine/${metadataItemCode}`);

  handleClickCheck = async () => {
    const { metadataItemCode } = this;
    this.setState({ checkLoading: true });
    // 发请求
    const res = await axios.post(`/PageDefine/${metadataItemCode}`, { datasets: [], layouts: [] });

    // const res = { success: true };
    if (res.success) {
      this.setState({
        checkLoading: false,
        isEdit: 'self',
      });
      Modal.info('锁定成功，可以开始编辑');
    } else {
      Modal.info('锁定失败，请稍后再试');
    }
  };

  handleClickSave = async () => {
    const { metadataItemCode } = this;
    window.console.log(this.metadataItemDS);
    try {
      const data = getDataToSave(this.metadataItemDS);
      const res = await axios.post(`/PageDefine/${metadataItemCode}`, data);
      if (res.success) {
        message.success('保存成功!', undefined, undefined, 'top');
      }
    } catch (error) {
      Modal.error(exception(error));
    }
  };

  handleClickTabs = (key) => {
    TabStore.setCurrentTab(key);
  }

  renderSelectAndAdd() {
    if (TabStore.currentTab === 'dataset') {
      return <DataSetSelect />;
    }
    return null;
  }

  renderCheckOrSaveBtn() {
    const { checkLoading, isEdit } = this.state;
    if (isEdit === 'none') {
      const check = (
        <Button
          color="blue"
          style={{ marginRight: 20, display: 'inline-block', verticalAlign: 'middle' }}
          onClick={this.handleClickCheck}
          loading={checkLoading}
        >
          锁定
        </Button>
      );
      return check;
    } else if (isEdit === 'self') {
      const save = (
        <Button
          color="blue"
          style={{ marginRight: 20, display: 'inline-block', verticalAlign: 'middle' }}
          onClick={this.handleClickSave}
        >
          保存
        </Button>
      );
      return save;
    } else {
      return null;
    }
  }

  render() {
    return (
      <Provider
        associatedDS={this.associatedDS}
        tableDS={this.tableDS}
        fieldDS={this.fieldDS}
        queryFieldDS={this.queryFieldDS}
        datasetsDS={this.datasetsDS}
        layoutsDS={this.layoutsDS}
        componentsDS={this.componentsDS}
      >
        <Content style={{ padding: 0, overflow: 'hidden' }} className="core-modeling-pages-metadaItem">
          <div className="header-title">建模平台</div>
          <Tabs
            className="header-tabs"
            activeKey={TabStore.currentTab}
            // defaultActiveKey="layout"
            animated={false}
            onChange={this.handleClickTabs}
            tabBarExtraContent={this.renderSelectAndAdd()}
          >
            <TabPane tab="数据建模" key="dataset" style={{ height: 'calc(100vh - 112px)', overflow: 'hidden' }}>
              <DataSetTab />
            </TabPane>
            <TabPane tab="页面建模" key="layout" style={{ height: 'calc(100vh - 112px)', overflow: 'hidden' }}>
              <Layout />
            </TabPane>
          </Tabs>
          <div className="footer">
            {this.renderCheckOrSaveBtn()}
          </div>
        </Content>
      </Provider>
    );
  }
}
