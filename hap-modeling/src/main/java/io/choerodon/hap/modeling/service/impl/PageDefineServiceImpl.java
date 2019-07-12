package io.choerodon.hap.modeling.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.hap.core.AppContextInitListener;
import io.choerodon.hap.function.dto.Resource;
import io.choerodon.hap.function.mapper.ResourceMapper;
import io.choerodon.hap.modeling.DocumentDatasetResolver;
import io.choerodon.hap.modeling.dto.Metadata;
import io.choerodon.hap.modeling.dto.MetadataChange;
import io.choerodon.hap.modeling.dto.MetadataItem;
import io.choerodon.hap.modeling.mapper.MetadataItemMapper;
import io.choerodon.hap.modeling.mapper.MetadataMapper;
import io.choerodon.hap.modeling.service.IPageDefineService;
import io.choerodon.dataset.exception.DatasetException;
import io.choerodon.message.IMessagePublisher;
import io.choerodon.message.ITopicMessageListener;
import io.choerodon.message.annotation.TopicMonitor;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.dom4j.dom.DOMElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


@Service
@TopicMonitor(channel = PageDefineServiceImpl.UPDATE_DATASET_CHANNEL)
public class PageDefineServiceImpl implements IPageDefineService, ITopicMessageListener<JSONArray>, AppContextInitListener {
    final static Logger LOGGER = LoggerFactory.getLogger(PageDefineServiceImpl.class);
    final static String UPDATE_DATASET_CHANNEL = "topic:dataset:update";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MetadataMapper metadataMapper;
    @Autowired
    private MetadataItemMapper metadataItemMapper;
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private ModelingPageService modelingPageService;
    @Autowired
    private DocumentDatasetResolver datasetResolver;
    @Autowired
    private IMessagePublisher messagePublisher;

    @Override
    public String query(String name){
        Metadata metadata = new Metadata();
        metadata.setDataType(Metadata.DATA_TYPE_PAGE);
        metadata.setName(name);
        metadata = metadataMapper.selectOne(metadata);
        if (metadata == null){
            throw new JSONException("page not found " + name);
        }
        String id = metadata.getChangeId();
        if (id == null){
            id = metadata.getDataId();
        }
        MetadataItem metadataItem = new MetadataItem();
        metadataItem.setId(id);
        metadataItem = metadataItemMapper.selectByPrimaryKey(metadataItem);
        if (metadataItem == null){
            return null;
        }
        try {
            List<MetadataChange> changes = objectMapper.readValue(metadataItem.getData(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, MetadataChange.class));
            for (MetadataChange change : changes){
                if(change.getType().equals(MetadataChange.Type.UPDATE_PAGE)){
                    return change.getData();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(String name, String data){
        Resource page = new Resource();
        page.setUrl(name);
        page.setType(Metadata.DATA_TYPE_PAGE);
        page = resourceMapper.selectOne(page);
        if(page == null){
            throw new DatasetException("dataset define not found.");
        }
        modelingPageService.update(page, data);
    }

    public void processPage(String data) throws IOException {
        JSONObject page = JSONObject.fromObject(data);
        if(page.isNullObject()){
            return;
        }
        Object datasets = page.get("datasets");
        if(datasets instanceof JSONArray){
            for(Object dataset: ((JSONArray) datasets).toArray()){
                if(dataset instanceof JSONObject){
                    processDataset((JSONObject) dataset);
                }
            }
            messagePublisher.publish(UPDATE_DATASET_CHANNEL, datasets);
        }
    }

    public void processDataset(JSONObject json) throws IOException {
        Set<String> updateColumns = new TreeSet<>();
        String masterTable = null;
        DOMElement datasetXml = new DOMElement("dataset");
        datasetXml.addAttribute("id", json.getString("datasetId"));
        JSONObject backend = json.getJSONObject("backend");
        DOMElement selectXml = new DOMElement("select");
        DOMElement tablesXml = new DOMElement("tables");
        for(Object table: backend.getJSONArray("talbeAndAssociation").toArray()){
            if(table instanceof JSONObject){
                String parentKey = transactSQLInjection((String) ((JSONObject)table).get("parentKey"));
                String filterLogic = transactSQLInjection((String) ((JSONObject)table).get("filterLogic"));

                if(parentKey == null){
                    masterTable = transactSQLInjection(((JSONObject) table).getString("tableKey"));
                    datasetXml.setAttribute("table", masterTable);
                    datasetXml.setAttribute("filterLogic", filterLogic);
                    for(Object filter : ((JSONObject) table).getJSONArray("filter")){
                        if(filter instanceof JSONObject){
                            DOMElement filterXml = new DOMElement("filter");
                            filterXml.setAttribute("columnName", transactSQLInjection(((JSONObject) filter).getString("field")));
                            filterXml.setAttribute("operation", ((JSONObject) filter).getString("operation"));
                            filterXml.setAttribute("type", ((JSONObject) filter).getString("type"));
                            filterXml.setAttribute("value", transactSQLInjection(((JSONObject) filter).getString("value")));
                            filterXml.setAttribute("valueTable", transactSQLInjection((String) ((JSONObject) filter).get("valueTable")));
                            selectXml.add(filterXml);
                        }
                    }
                } else {
                    DOMElement tableXml = new DOMElement("table");
                    tableXml.setAttribute("parentTable", parentKey);
                    tableXml.setAttribute("filterLogic", filterLogic);
                    tableXml.setAttribute("table", transactSQLInjection(((JSONObject) table).getString("tableKey")));
                    tableXml.setAttribute("join",  ((JSONObject) table).getString("join"));
                    tableXml.setAttribute("masterColumnName", transactSQLInjection(((JSONObject) table).getString("masterColumnName")));
                    tableXml.setAttribute("relationColumnName", transactSQLInjection(((JSONObject) table).getString("relationColumnName")));
                    for(Object filter : ((JSONObject) table).getJSONArray("filter")){
                        if(filter instanceof JSONObject){
                            DOMElement filterXml = new DOMElement("filter");
                            filterXml.setAttribute("columnName", transactSQLInjection(((JSONObject) filter).getString("field")));
                            filterXml.setAttribute("operation", ((JSONObject) filter).getString("operation"));
                            filterXml.setAttribute("type", ((JSONObject) filter).getString("type"));
                            filterXml.setAttribute("value", transactSQLInjection(((JSONObject) filter).getString("value")));
                            filterXml.setAttribute("valueTable", transactSQLInjection((String) ((JSONObject) filter).get("valueTable")));
                            filterXml.setAttribute("table", transactSQLInjection(((JSONObject) filter).getString("table")));
                            tableXml.add(filterXml);
                        }
                    }
                    tablesXml.add(tableXml);
                }
            }
        }
        DOMElement fieldsXml = new DOMElement("fields");
        for(Object field: backend.getJSONArray("fields").toArray()){
            if(field instanceof JSONObject){
                DOMElement fieldXml = new DOMElement("field");
                String name = transactSQLInjection(((JSONObject) field).getString("name"));
                String tableName = transactSQLInjection(((JSONObject) field).getString("tableKey"));
                String columnName = transactSQLInjection(((JSONObject) field).getString("columnName"));
                if (tableName.equals(masterTable)){
                    updateColumns.add(name);
                }
                fieldXml.setAttribute("name", name);
                fieldXml.setAttribute("columnName", columnName);
                fieldXml.setAttribute("table", tableName);

                fieldsXml.add(fieldXml);
            }
        }
        DOMElement queryFieldsXml = new DOMElement("queryFields");
        for(Object field: backend.getJSONArray("queryFields").toArray()){
            if(field instanceof JSONObject){
                DOMElement fieldXml = new DOMElement("field");
                fieldXml.setAttribute("name", transactSQLInjection(((JSONObject) field).getString("name")));
                fieldXml.setAttribute("columnName", transactSQLInjection(((JSONObject) field).getString("columnName")));
                fieldXml.setAttribute("table", transactSQLInjection(((JSONObject) field).getString("tableKey")));
                fieldXml.setAttribute("operation", transactSQLInjection(((JSONObject) field).getString("operation")));
                queryFieldsXml.add(fieldXml);
            }
        }
        selectXml.add(tablesXml);
        selectXml.add(fieldsXml);
        selectXml.add(queryFieldsXml);
        DOMElement updateXml = new DOMElement("update");
        updateXml.setAttribute("columns", StringUtils.join(updateColumns, ','));
        DOMElement insertXml = new DOMElement("insert");
        insertXml.setAttribute("columns", StringUtils.join(updateColumns, ','));
        DOMElement deleteXml = new DOMElement("delete");
        datasetXml.add(selectXml);
        datasetXml.add(updateXml);
        datasetXml.add(insertXml);
        datasetXml.add(deleteXml);
        datasetResolver.updateDataset(datasetXml);
    }

    @Override
    public String[] getTopic() {
        return null;
    }

    @Override
    public RedisSerializer<JSONArray> getRedisSerializer() {
        return null;
    }

    @Override
    public void onTopicMessage(JSONArray message, String pattern) {
        if (UPDATE_DATASET_CHANNEL.equals(pattern)){
            LOGGER.info("Dataset Update: {}.", message.size());
            for(Object dataset: message.toArray()){
                if(dataset instanceof JSONObject){
                    try {
                        processDataset((JSONObject) dataset);
                    } catch (IOException e) {
                        LOGGER.warn("Dataset update exception: ", e);
                    }
                }
            }
        }
    }

    @Override
    public void contextInitialized(ApplicationContext applicationContext) {
        //获取Resource表中的Page并初始化其中的Dataset
        Resource pageResource = new Resource();
        pageResource.setType(Metadata.DATA_TYPE_PAGE);
        pageResource.setLoginRequire("Y");
        for (Resource resource : resourceMapper.select(pageResource)){
            try {
                processPage(query(resource.getUrl()));
            } catch (JSONException e) {
                LOGGER.warn("page dataset init error: ",e);
            } catch (IOException e) {
                throw new DatasetException("dataset.error", e);
            }
        }
    }

    private static String transactSQLInjection(String str) {
        if (str == null){
            return null;
        }
        return str.replaceAll("([';])+|(--)+","");
    }
}