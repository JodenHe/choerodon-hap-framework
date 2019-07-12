import React, { PureComponent } from 'react';
import { ContentPro as Content } from '@choerodon/boot';
import { DataSet } from 'choerodon-ui/pro';
import BusinessRuleHeader from './stores/BusinessRuleHeader';
import BusinessRuleLineDS from './stores/BusinessRuleLineDS';
import BusRuleHeaderModal from './view/BusRuleHeaderModal';
import BusRuleLineModal from './view/BusRuleLineModal';

export default class Index extends PureComponent {
  line = new DataSet(BusinessRuleLineDS);

  header = new DataSet({
    ...BusinessRuleHeader,
    children: {
      lines: this.line,
    },
  });

  render() {
    return (
      <Content>
        <BusRuleHeaderModal dataset={this.header} />
        <BusRuleLineModal dataset={this.line} />
      </Content>
    );
  }
}
