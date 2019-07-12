import React, { Component } from 'react';
import { inject, observer, Provider } from 'mobx-react';
import { ContentPro as Content } from '@choerodon/boot';
import uuid from 'uuid/v4';
import { Button, Col, DataSet, Form, Modal, Row, Switch, Tabs, TextField } from 'choerodon-ui/pro';
import DraggableMenu from './view/DraggableMenu';
import Layout from './view/Layout';
import DndStore from './stores/DndStore';
import { PREFIX } from './constants';
import './style';
import Propertites from './view/Properties';
import AddLayoutDataSet from './stores/AddLayoutDataSet';
import LayoutSelect from './view/LayoutSelect';

const { TabPane } = Tabs;

@inject('layoutsDS')
@observer
export default class Index extends Component {
  static defaultProps = {
    prefixCls: PREFIX,
  };

  dndStore = new DndStore();

  addLayoutDS = new DataSet(AddLayoutDataSet);

  handleChange = (value) => {
    const { layoutsDS } = this.props;
    const index = layoutsDS.findIndex(r => r.get('layoutId') === value);
    if (index !== -1) {
      layoutsDS.locate(index);
    }
  };

  handleNewLayoutModalOk = async () => {
    const { addLayoutDS, addLayoutDS: { current }, props: { layoutsDS } } = this;
    if (await current.validate()) {
      layoutsDS.create({
        layoutName: current.get('name'),
        main: current.get('main'),
        layoutId: uuid(),
      });
      return true;
    } else {
      return false;
    }
  };

  handleNewLayoutModalClose = () => {
    const { addLayoutDS } = this;
    addLayoutDS.remove(addLayoutDS.current);
  };

  handleClickCreate = () => {
    const { addLayoutDS, props: { layoutsDS } } = this;
    const hasMain = layoutsDS.some(record => record.get('main') === true);
    addLayoutDS.create({ main: !hasMain });
    Modal.open({
      title: '新增Layout',
      drawer: true,
      style: {
        width: 600,
      },
      children: (
        <Form dataSet={addLayoutDS}>
          <TextField name="name" />
          <Switch name="main" disabled={hasMain} />
        </Form>
      ),
      okText: '新增',
      onOk: this.handleNewLayoutModalOk,
      afterClose: this.handleNewLayoutModalClose,
    });
  };

  handleClickDelete = async () => {
    if (await Modal.confirm('确认删除当前布局？') === 'ok') {
      const { layoutsDS } = this.props;
      layoutsDS.remove(layoutsDS.current);
    }
  };

  renderLayoutSelect() {
    const { layoutsDS } = this.props;
    if (layoutsDS.current) {
      return (
        <LayoutSelect
          layoutsDS={layoutsDS}
          value={layoutsDS.current.get('layoutId')}
          onChange={this.handleChange}
          clearButton={false}
        />
      );
    }
  }

  render() {
    const { prefixCls, layoutsDS } = this.props;
    return (
      <Provider dndStore={this.dndStore}>
        <Content className={prefixCls}>
          <table>
            <tbody>
              <tr>
                <td className={`${prefixCls}-menus`}>
                  <Row
                    gutter={4}
                  >
                    <Col span={10}>
                      <Button style={{ width: '100%' }} onClick={this.handleClickCreate} color="blue" icon="playlist_add">新建</Button>
                    </Col>
                    <Col span={10}>
                      {this.renderLayoutSelect()}
                    </Col>
                    <Col span={4}>
                      {layoutsDS.current && <Button funcType="flat" onClick={this.handleClickDelete} color="red" icon="cancel" />}
                    </Col>
                  </Row>
                  <Tabs className={`${prefixCls}-menus-tab`}>
                    <TabPane tab="数据源">
                      <DraggableMenu />
                    </TabPane>
                    <TabPane tab="组件" />
                  </Tabs>
                </td>
                <td className={`${prefixCls}-container`}>
                  <Layout />
                </td>
                <Propertites />
              </tr>
            </tbody>
          </table>
        </Content>
      </Provider>
    );
  }
}
