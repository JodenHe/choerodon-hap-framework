package io.choerodon.hap.modeling.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.hap.function.dto.Resource;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetadataChange {
    public enum Type {
        CREATE_TABLE, DELETE_TABLE , CREATE_COLUMN, DELETE_COLUMN, CREATE_RELATION, DELETE_RELATION, CREATE_PAGE, DELETE_PAGE, UPDATE_PAGE
    }
    private Type type;
    private MetadataTable table;
    private MetadataColumn column;
    private Resource page;
    private MetadataRelation relation;
    private String data;

    public String getTableName(){
        if(table != null){
            return table.getTableName();
        }
        if(column != null){
            return column.getTableName();
        }
        return null;
    }

    public MetadataRelation getRelation() {
        return relation;
    }

    public void setRelation(MetadataRelation relation) {
        this.relation = relation;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public MetadataTable getTable() {
        return table;
    }

    public void setTable(MetadataTable table) {
        this.table = table;
    }

    public MetadataColumn getColumn() {
        return column;
    }

    public void setColumn(MetadataColumn column) {
        this.column = column;
    }

    public Resource getPage() {
        return page;
    }

    public void setPage(Resource page) {
        this.page = page;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
