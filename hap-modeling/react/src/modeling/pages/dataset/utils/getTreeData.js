import _ from 'lodash';

function setTreeData(data) {
  const cloneData = _.cloneDeep(data);
  const tree = cloneData.filter((father) => {
    const branchArr = cloneData.filter(child => father.tableKey === child.parentKey);
    father.children = branchArr.length > 0 ? branchArr : [];
    return father.parentKey === undefined;
  });
  window.console.log(tree);
  return tree;
}

export default setTreeData;
