import React, { Component } from 'react';
import _ from 'lodash';
import { observer } from 'mobx-react';
import { withRouter } from 'react-router-dom';
import { Modal, DataSet } from 'choerodon-ui/pro';
import { axiosPro as axios, ContentPro as Content } from '@choerodon/boot';
import ComponentsDataSet from '../pages/stores/ComponentsDataSet';
import LayoutsDataSet from '../pages/stores/LayoutsDataSet';
import getComponent, { getCmpByField, getBtnByField } from './getComponent';
import ModalIndex from './ModalIndex';

@observer
@withRouter
export default class Relation extends Component {
  constructor(props) {
    super(props);
    this.metadataItemCode = _.get(this, 'props.match.params.code');
    this.datasets = {};
    this.datasetsConfig = {};
    this.metadataItem = { datasets: [], layouts: [] };

    this.componentsDS = new DataSet(ComponentsDataSet);

    this.layoutsDS = new DataSet({
      ...LayoutsDataSet,
      children: {
        components: this.componentsDS,
      },
    });
  }

  componentDidMount() {
    this.loadMetadataItem();
  }

  handleCallbackOfBtn = (type, obj, drawer) => {
    if (type === 'openPage') {
      this.props.history.push({
        pathname: obj,
      });
      return;
    }
    if (type === 'openLayout') {
      Modal.open({
        key: obj,
        title: 'Multilayer',
        drawer,
        children: (
          <ModalIndex
            metadataItem={this.metadataItem}
            layoutId={obj}
            datasets={this.datasets}
            datasetsConfig={this.datasetsConfig}
          />
        ),
      });
    }
  }

  getDataSetByKey(key) {
    if (!this.datasets[key]) {
      const dsConfigsArr = _.values(this.datasetsConfig);
      const children = dsConfigsArr.filter(d => d.parentDS === key);
      const obj = {};
      children.forEach((d) => {
        obj[d.datasetId] = this.getDataSetByKey(d.datasetId);
      });
      this.datasets[key] = new DataSet({
        ...this.datasetsConfig[key],
        children: obj,
      }); 
    }
    return this.datasets[key];
  }

  async loadMetadataItem() {
    const { metadataItemCode } = this;

    if (metadataItemCode) {
      try {
        const result = await this.loadMetadataItemById(metadataItemCode);

        if (result.success === false) {
          Modal.info('请确保先创建页面。');
          return;
        }

        const metadataItem = result || { datasets: [], layouts: [] };
        this.metadataItem = metadataItem;
        
        const o = {};
        metadataItem.datasets.forEach((d) => {
          o[d.datasetId] = d;
        });
        this.datasetsConfig = o;
        window.console.log('datasets', this.datasets);
        this.layoutsDS.loadData(metadataItem.layouts);
        this.layoutsDS.locate(metadataItem.layouts.findIndex(l => l.main));
        window.console.log('componentDS', this.componentsDS.treeData);
        this.forceUpdate();
      } catch (error) {
        Modal.info('读取页面失败，请稍后再试。');
      }
    }
  }

  loadMetadataItemById = metadataItemCode => axios.get(`/PageDefine/${metadataItemCode}`);

  renderContent(record) {
    const { type, dataSet, props, editor } = record.data;
    if (dataSet) {
      props.dataSet = this.getDataSetByKey(dataSet);
    }
    if (type === 'Row' || type === 'Form') {
      return getComponent(
        type,
        props,
        record.children ? record.children.map(r => this.renderContent(r)) : null,
      );
    }
    if (type === 'TableButton') {
      return getComponent(
        type,
        _.omit(props, 'children'),
        props.children,
      );
    }
    if (type === 'Table') {
      const columnsArr = this.componentsDS.filter(r => r.get('parentId') === record.get('id') && r.get('type') === 'TableItem');
      const btnsArr = this.componentsDS.filter(r => r.get('parentId') === record.get('id') && r.get('type') === 'TableButton');
      const buttons = btnsArr.map(r => (r.get('method') !== 'custom' ? r.get('method') : getBtnByField(r, props.dataSet, this.handleCallbackOfBtn)));
      const btnCommands = this.componentsDS.filter(r => r.get('parentId') === record.get('id') && r.get('type') === 'TableCommand');
      const queryFieldArr = this.componentsDS.filter(r => r.get('parentId') === record.get('id') && r.get('type') === 'TableQueryField' && r.get('editor'));
      const queryFields = {};
      queryFieldArr.forEach((r) => {
        queryFields[r.get('props').name] = getComponent(r.get('editor').type);
      });

      const columns = columnsArr.map(r => r.toData()).map((o) => {
        if (o.editor) {
          o.props.editor = getComponent(o.editor.type, {});
        }
        return o;
      }).map(obj => obj.props);
      if (btnCommands && btnCommands.length) {
        const commandsColumn = {
          align: 'center',
          lock: 'right',
          renderer: () => <div>{btnCommands.map(btn => getBtnByField(btn, props.dataSet, this.handleCallbackOfBtn))}</div>,
        };
        columns.push(commandsColumn);
      }
      const prop = {
        ...props,
        buttons,
        columns,
        queryFields,
      };
      return getComponent(
        type,
        prop,
      );
    }
    if (type === 'FormItem') {
      const parent = this.componentsDS.find(r => r.get('id') === record.data.parentId);
      const ds = parent ? parent.get('dataSet') : undefined;
      const mirror = this.datasetsConfig[ds].fields.find(o => o.name === record.data.props.name);
      return getComponent(getCmpByField(mirror), props);
    }
  }

  render() {
    const { treeData } = this.componentsDS;

    return (
      <Content>
        {treeData.map(r => this.renderContent(r))}
      </Content>
    );
  }
}
