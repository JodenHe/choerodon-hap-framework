import React, { PureComponent } from 'react';
import { Provider } from 'mobx-react';
import { ContentPro as Content } from '@choerodon/boot';
import { DataSet } from 'choerodon-ui/pro';
import Layout from '.';
import MetaDataSet from './stores/MetaDataSet';
import LayoutsDataSet from '../stores/LayoutsDataSet';
import DataSetsDataSet from '../stores/DataSetsDataSet';
import DataSetFieldsDataSet from '../dataset/stores/FieldDataSet';
import ComponentsDataSet from '../stores/ComponentsDataSet';
import { PREFIX } from './constants';

export default class Mock extends PureComponent {
  static defaultProps = {
    prefixCls: PREFIX,
  };

  handleMetaDataSetLoad = ({ dataSet }) => {
    const { datasets, layouts } = JSON.parse(dataSet.current.get('data') || '{}');
    this.datasetsDS.loadData(datasets);
    this.layoutsDS.loadData(layouts);
  };

  dataSetFieldsDs = new DataSet(DataSetFieldsDataSet);

  datasetsDS = new DataSet({
    ...DataSetsDataSet,
    children: {
      fields: this.dataSetFieldsDs,
    },
  });

  componentsDS = new DataSet(ComponentsDataSet);

  layoutsDS = new DataSet({
    ...LayoutsDataSet,
    children: {
      components: this.componentsDS,
    },
  });

  metaDS = new DataSet({
    ...MetaDataSet,
    events: {
      load: this.handleMetaDataSetLoad,
    },
  });

  render() {
    const { prefixCls } = this.props;
    return (
      <Provider datasetsDS={this.datasetsDS} componentsDS={this.componentsDS} layoutsDS={this.layoutsDS}>
        <Content className={prefixCls}>
          <Layout />
        </Content>
      </Provider>
    );
  }
}
