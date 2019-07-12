import React, { Component } from 'react';
import { Form, IconPicker, Modal, SelectBox, Switch, TextField } from 'choerodon-ui/pro';
import Constants from 'choerodon-ui/pro/lib/util/Constants';
import { PREFIX } from '../../constants';
import Behaviour from './behaviour/index';
import BehaviourButton from './behaviour/BehaviourButton';

export default function (record) {
  function handleClick(layoutsDS) {
    Modal.open({
      title: '定义按钮行为',
      children: (
        <Behaviour record={record} layoutsDS={layoutsDS} />
      ),
    });
  }

  const { Option } = SelectBox;
  const method = record.get('method');
  const C7N_PREFIX = getPrefixCls('hap-btn');
  const colorClass = `${PREFIX}-properties-color ${C7N_PREFIX}-raised`;
  const properties = [
    <Form key="base">
      <TextField key="props.children" name="props.children" record={record} label="文案" />
      <SelectBox key="props.funcType" name="props.funcType" record={record} label="展现模式">
        <Option value="raised">提升</Option>
        <Option value="flat">平面</Option>
      </SelectBox>
      <IconPicker key="props.icon" name="props.icon" record={record} label="图标" />
      <Switch
        disabled={!record.get('props.icon') && record.get('method') === 'custom'}
        key="iconOnly"
        name="iconOnly"
        record={record}
        label="只显示图标"
      />
      <SelectBox className={`${PREFIX}-properties-color-select`} key="props.color" mode="button" name="props.color" record={record} label="颜色">
        <Option value="default">
          <div className={colorClass} />
        </Option>
        <Option value="blue">
          <div className={`${colorClass} ${C7N_PREFIX}-blue`} />
        </Option>
        <Option value="red">
          <div className={`${colorClass} ${C7N_PREFIX}-red`} />
        </Option>
        <Option value="yellow">
          <div className={`${colorClass} ${C7N_PREFIX}-yellow`} />
        </Option>
        <Option value="green">
          <div className={`${colorClass} ${C7N_PREFIX}-green`} />
        </Option>
        <Option value="dark">
          <div className={`${colorClass} ${C7N_PREFIX}-dark`} />
        </Option>
        <Option value="gray">
          <div className={`${colorClass} ${C7N_PREFIX}-gray`} />
        </Option>
      </SelectBox>
    </Form>,
  ];
  if (method === 'custom') {
    properties.push(<BehaviourButton key="custom" color="blue" style={{ width: '100%' }} onClick={handleClick}>自定义按钮行为</BehaviourButton>);
  }
  return properties;
}
