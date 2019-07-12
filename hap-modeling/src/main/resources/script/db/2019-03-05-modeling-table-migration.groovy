package script.db

databaseChangeLog(logicalFilePath: "2019-03-05-modeling-table-migration.groovy") {

    changeSet(author: "qiangzeng", id: "20181115-metadata-1") {
        createTable(tableName: "METADATA", remarks: "元数据表") {
            column(name: "ID", type: "VARCHAR(32)", remarks: "ID") {
                constraints(nullable: "false", primaryKey: "true")
            }
            column(name: "NAME", type: "VARCHAR(50)", remarks: "名称")
            column(name: "DESCRIPTION", type: "VARCHAR(255)", remarks: "描述")
            column(name: "DATA_TYPE", type: "VARCHAR(20)", remarks: "Xml类型")
            column(name: "STATUS", type: "VARCHAR(10)", remarks: "状态")
            column(name: "LOCKED_BY", type: "varchar(40)", remarks: "被谁锁定(用户名)")
            column(name: "DATA_ID", type: "VARCHAR(32)", remarks: "数据ID")
            column(name: "CHANGE_ID", type: "VARCHAR(32)", remarks: "当前变更ID")

            column(name: "OBJECT_VERSION_NUMBER", type: "BIGINT", defaultValue: "1")
            column(name: "REQUEST_ID", type: "BIGINT", defaultValue: "-1")
            column(name: "PROGRAM_ID", type: "BIGINT", defaultValue: "-1")
            column(name: "CREATED_BY", type: "BIGINT", defaultValue: "-1")
            column(name: "CREATION_DATE", type: "DATETIME", defaultValueComputed: "CURRENT_TIMESTAMP")
            column(name: "LAST_UPDATED_BY", type: "BIGINT", defaultValue: "-1")
            column(name: "LAST_UPDATE_DATE", type: "DATETIME", defaultValueComputed: "CURRENT_TIMESTAMP")
        }

        if (!helper.isPostgresql()) {
            addUniqueConstraint(tableName: "METADATA", columnNames: "NAME,DATA_TYPE", constraintName: "METADATA_NAME_U1")
        } else {
            addUniqueConstraint(tableName: "METADATA", columnNames: "ID,NAME,DATA_TYPE", constraintName: "METADATA_NAME_U1")
        }
    }

    changeSet(author: "qiangzeng", id: "20181115-metadata-item-1") {
        createTable(tableName: "METADATA_ITEM", remarks: "元数据项表") {
            column(name: "ID", type: "VARCHAR(32)", remarks: "ID") {
                constraints(nullable: "false", primaryKey: "true")
            }
            column(name: "METADATA_ID", type: "VARCHAR(32)", remarks: "metadata表ID")
            column(name: "DATA", type: "CLOB", remarks: "xml数据")
            column(name: "DATA_VERSION", type: "BIGINT", remarks: "数据版本", defaultValue: "1")

            column(name: "OBJECT_VERSION_NUMBER", type: "BIGINT", defaultValue: "1")
            column(name: "REQUEST_ID", type: "BIGINT", defaultValue: "-1")
            column(name: "PROGRAM_ID", type: "BIGINT", defaultValue: "-1")
            column(name: "CREATED_BY", type: "BIGINT", defaultValue: "-1")
            column(name: "CREATION_DATE", type: "DATETIME", defaultValueComputed: "CURRENT_TIMESTAMP")
            column(name: "LAST_UPDATED_BY", type: "BIGINT", defaultValue: "-1")
            column(name: "LAST_UPDATE_DATE", type: "DATETIME", defaultValueComputed: "CURRENT_TIMESTAMP")
        }
    }

    changeSet(author: "xausky", id: "20181210-metadata-relation") {
        createTable(tableName: "METADATA_RELATION", remarks: "关联关系表") {
            column(name: "ID", type: "VARCHAR(32)", remarks: "ID") {
                constraints(nullable: "false", primaryKey: "true")
            }
            column(name: "ENABLE", type: "BOOLEAN", remarks: "是否启用") {
                constraints(nullable: "false")
            }
            column(name: "RELATION_TYPE", type: "VARCHAR(32)", remarks: "关系类型") {
                constraints(nullable: "false")
            }
            column(name: "MASTER_TABLE", type: "VARCHAR(64)", remarks: "主表名") {
                constraints(nullable: "false")
            }
            column(name: "MASTER_COLUMN", type: "VARCHAR(64)", remarks: "主表字段") {
                constraints(nullable: "false")
            }
            column(name: "RELATION_TABLE", type: "VARCHAR(64)", remarks: "关联表名") {
                constraints(nullable: "false")
            }
            column(name: "RELATION_COLUMN", type: "VARCHAR(64)", remarks: "关联字段") {
                constraints(nullable: "false")
            }
            // 采用 masterTable 和 relationTable 进行字典排序，中间加 - 生成
            column(name: "UNIQUE_NAME", type: "VARCHAR(128)", remarks: "关系唯一名称") {
                constraints(nullable: "false", unique: "true")
            }

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