package io.choerodon.hap.modeling;

import io.choerodon.hap.modeling.model.DocumentDatasetExector;
import io.choerodon.hap.modeling.service.IMetadataTableService;
import io.choerodon.dataset.exception.DatasetException;
import io.choerodon.dataset.service.IDatasetRepositoryService;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Component
public class DocumentDatasetResolver implements ApplicationListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentDatasetResolver.class);
    private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    @Autowired
    private SqlSessionTemplate template;
    @Autowired
    private IDatasetRepositoryService repositoryService;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private IMetadataTableService metadataTableService;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            ApplicationContext context = ((ContextRefreshedEvent) event).getApplicationContext();
            try {
                // 获取资源目录下 dataset 里的 xml 文件
                Resource[] resources = context.getResources("classpath*:/dataset/**/*.xml");
                for (Resource resource : resources) {
                    try {
                        Document document = factory.newDocumentBuilder().parse(resource.getInputStream());
                        for (int nodeIndex = 0; nodeIndex < document.getChildNodes().getLength(); ++nodeIndex) {
                            if (document.getChildNodes().item(nodeIndex) instanceof Element) {
                                NodeList list = document.getChildNodes().item(nodeIndex).getChildNodes();
                                for (int i = 0; i < list.getLength(); ++i) {
                                    Node node = list.item(i);
                                    if (node instanceof Element) {
                                        this.updateDataset((Element) node);
                                    }
                                }
                            }
                        }

                    } catch (ParserConfigurationException | SAXException | IOException e) {
                        LOGGER.warn("Dataset document parse exception:", e);
                    }
                }
            } catch (IOException e) {
                throw new DatasetException("dataset.error", e);
            }
        }
    }

    public void updateDataset(Element node) {
        String id = node.getAttribute("id");
        repositoryService.putExecutor(id, new DocumentDatasetExector(node, template.getConfiguration(), metadataTableService, repositoryService, applicationContext));
    }

}
