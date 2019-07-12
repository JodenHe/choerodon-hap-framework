import React, { PureComponent } from 'react';
import { inject } from 'mobx-react';
import omit from 'lodash/omit';
import { Button } from 'choerodon-ui/pro';

@inject('layoutsDS')
export default class BehaviourButton extends PureComponent {
  handleClick = () => {
    const { onClick, layoutsDS } = this.props;
    onClick(layoutsDS);
  };

  render() {
    return <Button {...omit(this.props, ['layoutsDS'])} onClick={this.handleClick} />;
  }
}
