import React from 'react';

function getFieldConfig(dataSet, fieldName, childrenName) {
  if (dataSet && fieldName) {
    const fields = dataSet.getCascadeRecords(childrenName);
    if (fields) {
      return fields.find(record => record.get('name') === fieldName);
    }
  }
}

export function getDataSetConfigById(datasetsDS, id) {
  if (id) {
    return datasetsDS.find(record => record.get('datasetId') === id);
  }
}

export function getFieldConfigByName(dataSet, fieldName) {
  return getFieldConfig(dataSet, fieldName, 'fields');
}

export function getQueryFieldConfigByName(dataSet, fieldName) {
  return getFieldConfig(dataSet, fieldName, 'queryFields');
}

export function isSupportTransition() {
  return typeof window !== 'undefined' && 'TransitionEvent' in window;
}

export function recursiveRemove(record) {
  if (record) {
    const { children, dataSet } = record;
    if (children) {
      children.forEach(recursiveRemove);
    }
    dataSet.remove(record);
  }
}

export function getComputedStyle(node) {
  return document.defaultView.getComputedStyle(node);
}

export function getMargin(node) {
  const { marginTop, marginRight, marginBottom, marginLeft } = getComputedStyle(node);
  return {
    top: parseFloat(marginTop),
    right: parseFloat(marginRight),
    bottom: parseFloat(marginBottom),
    left: parseFloat(marginLeft),
  };
}

export function getOverflowParent(node) {
  const { offsetParent } = node;
  if (offsetParent) {
    if (getComputedStyle(offsetParent).overflow !== 'visible') {
      return offsetParent;
    } else {
      return getOverflowParent(offsetParent);
    }
  }
}

export function isTree(dataSet) {
  const parentField = dataSet.get('parentField');
  const idField = dataSet.get('idField');
  return parentField !== undefined && idField !== undefined;
}
