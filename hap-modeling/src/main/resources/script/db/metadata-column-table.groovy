package script.db
databaseChangeLog(logicalFilePath: "2017-10-12-init-migration.groovy") {
    changeSet(author: "xausky", id: "20181210-metadata-table") {
        createTable(tableName: "METADATA_TABLE_B", remarks: "表描述元数据") {
            column(name: "ID", type: "VARCHAR(32)", remarks: "ID"){
                constraints(nullable: "false", primaryKey: "true")
            }
            column(name: "TABLE_NAME", type: "VARCHAR(64)", remarks: "表名"){
                constraints(nullable: "false", unique: "true")
            }
            column(name: "MULTI_LANGUAGE", type: "BOOLEAN", remarks: "是否是多语言表")
            column(name: "DESCRIPTION", type: "VARCHAR(64)", remarks: "表描述")

            column(name: "OBJECT_VERSION_NUMBER", type: "BIGINT", defaultValue: "1")
            column(name: "REQUEST_ID", type: "BIGINT", defaultValue: "-1")
            column(name: "PROGRAM_ID", type: "BIGINT", defaultValue: "-1")
            column(name: "CREATED_BY", type: "BIGINT", defaultValue: "-1")
            column(name: "CREATION_DATE", type: "DATETIME", defaultValueComputed: "CURRENT_TIMESTAMP")
            column(name: "LAST_UPDATED_BY", type: "BIGINT", defaultValue: "-1")
            column(name: "LAST_UPDATE_DATE", type: "DATETIME", defaultValueComputed: "CURRENT_TIMESTAMP")
        }
        createTable(tableName: "METADATA_TABLE_TL", remarks: "表描述元数据") {
            column(name: "ID", type: "VARCHAR(32)", remarks: "ID"){
                constraints(nullable: "false", primaryKey: "true")
            }
            column(name: "LANG", type: "VARCHAR(32)", remarks: "语言"){
                constraints(nullable: "false", primaryKey: "true")
            }
            column(name: "DESCRIPTION", type: "VARCHAR(64)", remarks: "表描述")

            column(name: "OBJECT_VERSION_NUMBER", type: "BIGINT", defaultValue: "1")
            column(name: "REQUEST_ID", type: "BIGINT", defaultValue: "-1")
            column(name: "PROGRAM_ID", type: "BIGINT", defaultValue: "-1")
            column(name: "CREATED_BY", type: "BIGINT", defaultValue: "-1")
            column(name: "CREATION_DATE", type: "DATETIME", defaultValueComputed: "CURRENT_TIMESTAMP")
            column(name: "LAST_UPDATED_BY", type: "BIGINT", defaultValue: "-1")
            column(name: "LAST_UPDATE_DATE", type: "DATETIME", defaultValueComputed: "CURRENT_TIMESTAMP")
        }
        createTable(tableName: "METADATA_COLUMN_B", remarks: "列描述元数据") {
            column(name: "ID", type: "VARCHAR(32)", remarks: "ID"){
                constraints(nullable: "false", primaryKey: "true")
            }
            column(name: "TABLE_NAME", type: "VARCHAR(64)", remarks: "表名"){
                constraints(nullable: "false")
            }
            column(name: "COLUMN_NAME", type: "VARCHAR(64)", remarks: "列名"){
                constraints(nullable: "false")
            }
            column(name: "TYPE_NAME", type: "VARCHAR(64)", remarks: "类型名")
            column(name: "COLUMN_SIZE", type: "BIGINT", remarks: "字段大小")
            column(name: "PRIMARY_KEY", type: "BOOLEAN", remarks: "是否主键")
            column(name: "MULTI_LANGUAGE", type: "BOOLEAN", remarks: "是否多语言")
            column(name: "NULLABLE", type: "BOOLEAN", remarks: "是否可空")
            column(name: "DESCRIPTION", type: "VARCHAR(64)", remarks: "列描述")
            column(name: "OBJECT_VERSION_NUMBER", type: "BIGINT", defaultValue: "1")
            column(name: "REQUEST_ID", type: "BIGINT", defaultValue: "-1")
            column(name: "PROGRAM_ID", type: "BIGINT", defaultValue: "-1")
            column(name: "CREATED_BY", type: "BIGINT", defaultValue: "-1")
            column(name: "CREATION_DATE", type: "DATETIME", defaultValueComputed: "CURRENT_TIMESTAMP")
            column(name: "LAST_UPDATED_BY", type: "BIGINT", defaultValue: "-1")
            column(name: "LAST_UPDATE_DATE", type: "DATETIME", defaultValueComputed: "CURRENT_TIMESTAMP")
        }
        addUniqueConstraint(tableName:"METADATA_COLUMN_B", columnNames:"TABLE_NAME,COLUMN_NAME")

        createTable(tableName: "METADATA_COLUMN_TL", remarks: "列描述元数据") {
            column(name: "ID", type: "VARCHAR(32)", remarks: "ID"){
                constraints(nullable: "false", primaryKey: "true")
            }
            column(name: "LANG", type: "VARCHAR(32)", remarks: "语言"){
                constraints(nullable: "false", primaryKey: "true")
            }
            column(name: "DESCRIPTION", type: "VARCHAR(64)", remarks: "列描述")

            column(name: "OBJECT_VERSION_NUMBER", type: "BIGINT", defaultValue: "1")
            column(name: "REQUEST_ID", type: "BIGINT", defaultValue: "-1")
            column(name: "PROGRAM_ID", type: "BIGINT", defaultValue: "-1")
            column(name: "CREATED_BY", type: "BIGINT", defaultValue: "-1")
            column(name: "CREATION_DATE", type: "DATETIME", defaultValueComputed: "CURRENT_TIMESTAMP")
            column(name: "LAST_UPDATED_BY", type: "BIGINT", defaultValue: "-1")
            column(name: "LAST_UPDATE_DATE", type: "DATETIME", defaultValueComputed: "CURRENT_TIMESTAMP")
        }
    }
}