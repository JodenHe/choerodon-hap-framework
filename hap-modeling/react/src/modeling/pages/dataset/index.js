import React, { Component } from 'react';
import Msg from './Msg';
import Graph from './TreeGraph';
import './index.less';

export default class Index extends Component {
  render() {
    return (
      <div style={{ height: '100%' }}>
        <div className="borad-wrap">
          <Graph />
          <Msg />
        </div>
      </div>
    );
  }
}
