const defaultDeleteProps = [
  '__dirty',
  '__id',
  '__status',
  '_token',
  'id',
];

const unique = a => [...new Set(a)];

export function cleanRecordObj(obj, props = [], combine = true) {
  const reslutDeleteProps = combine
    ? unique(defaultDeleteProps.concat(props))
    : props;
  reslutDeleteProps.forEach((prop) => {
    if (obj[prop]) {
      delete obj[prop];
    }
  });
  return obj;
}

export function pickRecordObj(obj, props = [], combine = true) {
  const res = {};
  props.forEach((prop) => {
    if (obj[prop]) {
      res[prop] = obj[prop];
    }
  });
  return res;
}

function handleGetOtherParams(dataset) {
  const otherParamObj = dataset.toJSONData();
  otherParamObj.selection = otherParamObj.selection ? otherParamObj.selectionType : false;
  const res = cleanRecordObj(otherParamObj, ['datasetId', 'datasetName', 'tables', 'selectionType', '__dirty', '__id', '__status', '_token', 'fields', 'queryFields'], false);
  return res;
}

function handleGetFields(dataset) {
  // confirm
  const { fields } = dataset.toJSONData();
  const fieldsObj = fields.map((r) => {
    if (r.lovCode) {
      return cleanRecordObj(
        r,
        ['columnSize', 'description', 'displayType', 'multiLanguage', 'nullable', 'primaryKey', 'typeName', 'primaryKey', 'field'],
      );
    } else {
      return cleanRecordObj(
        r,
        ['columnSize', 'description', 'displayType', 'multiLanguage', 'nullable', 'primaryKey', 'typeName', 'primaryKey', 'tableKey', 'columnName', 'displayType', 'multiLanguage', 'description', 'nullable'],
      );
    }
  });
  return fieldsObj;
}

function handleGetQueryFields(dataset) {
  const { fields, queryFields } = dataset.toJSONData();
  const queryFieldsObj = queryFields.map(r => cleanRecordObj(r, ['columnSize', 'description', 'displayType', 'multiLanguage', 'nullable', 'primaryKey', 'typeName']));
  const lovAndLookup = fields
    .filter(r => r.lovCode)
    .map((record) => {
      if (record.lovCode) {
        return cleanRecordObj(
          record,
          ['columnSize', 'description', 'displayType', 'multiLanguage', 'nullable', 'primaryKey', 'typeName', 'primaryKey', 'field'],
        );
      } else {
        return cleanRecordObj(
          record,
          ['columnSize', 'description', 'displayType', 'multiLanguage', 'nullable', 'primaryKey', 'typeName', 'primaryKey', 'field'],
        );
      }
    });
  return queryFieldsObj.concat(lovAndLookup);
}


/**
 * 根据dataset返回可使用的dataset config
 * 清理部分字段
 * @param {} dataset 
 */
function transformDataSetToConfig(dataset) {
  // return dataset.toJSONData();
  return {
    ...handleGetOtherParams(dataset),
    fields: [
      ...handleGetFields(dataset),
    ],
    queryFields: [
      ...handleGetQueryFields(dataset),
    ],
  };
}

function handleGetTableAndAssociation(datasetObj) {
  const originObj = datasetObj.tables;
  originObj.forEach((table, i) => {
    table.filter = table.filter.map(r => cleanRecordObj(r));
    originObj[i] = pickRecordObj(table, ['filter', 'tableName', 'tableKey', 'parentKey', 'masterColumnName', 'relationColumnName', 'join', 'logic']);
  });
  return originObj;
}

function handlePickFields(datasetObj) {
  const onlyFields = datasetObj.fields.filter(r => !r.lovCode);
  if (!onlyFields.length) {
    throw new Error(`请确保  ${datasetObj.datasetName}  中添加了至少一个字段！`);
  }
  const fieldsObj = onlyFields
    .map(r => pickRecordObj(
      r,
      ['name', 'columnName', 'tableKey', 'type'],
    ));
  return fieldsObj;
}


function handlePickQueryFields(datasetObj) {
  const queryFieldsObj = datasetObj.queryFields.map(r => pickRecordObj(r, ['name', 'columnName', 'tableKey', 'type', 'operation']));
  return queryFieldsObj;
}

function getBackend(datasetObj) {
  return {
    talbeAndAssociation: [
      ...handleGetTableAndAssociation(datasetObj),
    ],
    fields: [
      ...handlePickFields(datasetObj),
    ],
    queryFields: [
      ...handlePickQueryFields(datasetObj),
    ],
  }; 
}

function clearDataSet(dataset) {
  const datasetObj = dataset;
  if (datasetObj.selection) {
    datasetObj.selection = datasetObj.selectionType;
  }
  return {
    ...datasetObj,
    backend: getBackend(datasetObj),
  };
}

/**
 * 根据一个datasetDS的数组，常用的是datasetsDS，遍历获得dataset configs
 * @param {*} datasetsDS 
 */
export function getDataSetConfigs(datasetsDS) {
  return datasetsDS.map(dataset => transformDataSetToConfig(dataset));
}

/**
 * 根据一个datasetDS获得一个dataset config
 * @param {*} datasetDS 
 */
export function getDataSetConfig(datasetDS) {
  return transformDataSetToConfig(datasetDS);
}

export function getDataSetConfigByName(datasetsDS, name) {
  if (name) {
    const dataset = datasetsDS.find(record => record.get('datasetName') === name);
    if (dataset) {
      return transformDataSetToConfig(dataset);
    }
  }
  return undefined;
}

export function getDataSetConfigById(datasetsDS, id) {
  if (id) {
    const dataset = datasetsDS.find(record => record.get('datasetId') === id);
    if (dataset) {
      return transformDataSetToConfig(dataset);
    }
  }
  return undefined;
}

/**
 * 点保存，准备提交时，处理数据
 * 1. datasets部分
 *    - 为每个dataset生成backend对象用于后端处理
 * @param {*} metadataItemDS 
 */
export function getDataToSave(metadataItemDS) {
  const metadataItem = metadataItemDS.current;
  const { datasets, layouts } = metadataItem.toData();
  return {
    layouts,
    datasets: datasets.map(dataset => clearDataSet(dataset)),
  };
}
