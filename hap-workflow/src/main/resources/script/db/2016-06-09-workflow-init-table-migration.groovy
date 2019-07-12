package script.db

databaseChangeLog(logicalFilePath: "2016-06-09-init-table-migration.groovy") {

    changeSet(author: "vista yih", id: "2018-11-15-ACT_ID_GROUP") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_ID_GROUP")
            }
        }
        createTable(tableName: "ACT_ID_GROUP") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(primaryKey: true)
            }
            column(name: "REV_", type: "INT")
            column(name: "NAME_", type: "VARCHAR(255)")
            column(name: "TYPE_", type: "VARCHAR(255)")
        }
    }

    changeSet(author: "qiang.zeng", id: "2019-02-15-ACT_ID_USER") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_ID_USER")
            }
        }
        createTable(tableName: "ACT_ID_USER") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(primaryKey: true)
            }
            column(name: "REV_", type: "INT")
            column(name: "FIRST_", type: "VARCHAR(255)")
            column(name: "LAST_", type: "VARCHAR(255)")
            column(name: "EMAIL_", type: "VARCHAR(255)")
            column(name: "PWD_", type: "VARCHAR(255)")
            column(name: "PICTURE_ID_", type: "VARCHAR(64)")
        }
    }

    changeSet(author: "qiang.zeng", id: "2019-02-15-ACT_ID_INFO") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_ID_INFO")
            }
        }
        createTable(tableName: "ACT_ID_INFO") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(primaryKey: true)
            }
            column(name: "REV_", type: "INT")
            column(name: "USER_ID_", type: "VARCHAR(64)")
            column(name: "TYPE_", type: "VARCHAR(64)")
            column(name: "KEY_", type: "VARCHAR(255)")
            column(name: "VALUE_", type: "VARCHAR(255)")
            column(name: "PASSWORD_", type: "BLOB")
            column(name: "PARENT_ID_", type: "VARCHAR(255)")
        }
    }

    changeSet(author: "vista yih", id: "2018-11-15-ACT_ID_MEMBERSHIP") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_ID_MEMBERSHIP")
            }
        }
        createTable(tableName: "ACT_ID_MEMBERSHIP") {
            column(name: "USER_ID_", type: "VARCHAR(64)") {
                constraints(primaryKey: true)
            }
            column(name: "GROUP_ID_", type: "VARCHAR(64)") {
                constraints(primaryKey: true)
            }
        }
        createIndex(tableName: "ACT_ID_MEMBERSHIP", indexName: "ACT_IDX_MEMB_GROUP") {
            column(name: "GROUP_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_ID_MEMBERSHIP", indexName: "ACT_IDX_MEMB_USER") {
            column(name: "USER_ID_", type: "VARCHAR(64)")
        }
        addForeignKeyConstraint(baseColumnNames: "GROUP_ID_", baseTableName: "ACT_ID_MEMBERSHIP", constraintName: "ACT_FK_MEMB_GROUP", referencedColumnNames: "ID_", referencedTableName: "ACT_ID_GROUP")
        addForeignKeyConstraint(baseColumnNames: "USER_ID_", baseTableName: "ACT_ID_MEMBERSHIP", constraintName: "ACT_FK_MEMB_USER", referencedColumnNames: "ID_", referencedTableName: "ACT_ID_USER")
    }

    changeSet(author: "vista yih", id: "2018-11-15-ACT_GE_PROPERTY") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_GE_PROPERTY")
            }
        }
        createTable(tableName: "ACT_GE_PROPERTY") {
            column(name: "NAME_", type: "VARCHAR(64)") {
                constraints(primaryKey: true)
            }
            column(name: "VALUE_", type: "VARCHAR(300)")
            column(name: "REV_", type: "INT")
        }
    }

    changeSet(author: "vista yih", id: "2018-11-15-ACT_RE_DEPLOYMENT") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_RE_DEPLOYMENT")
            }
        }
        createTable(tableName: "ACT_RE_DEPLOYMENT") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(primaryKey: true)
            }
            column(name: "NAME_", type: "VARCHAR(255)")
            column(name: "CATEGORY_", type: "VARCHAR(255)")
            column(name: "KEY_", type: "VARCHAR(255)")
            column(name: "TENANT_ID_", type: "VARCHAR(255)", defaultValue: "")
            column(name: "DEPLOY_TIME_", type: "TIMESTAMP")
            column(name: "ENGINE_VERSION_", type: "VARCHAR(255)")
        }
    }

    changeSet(author: "vista yih", id: "2018-11-15-ACT_GE_BYTEARRAY") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_GE_BYTEARRAY")
            }
        }
        createTable(tableName: "ACT_GE_BYTEARRAY") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(primaryKey: true)
            }
            column(name: "REV_", type: "INT")
            column(name: "NAME_", type: "VARCHAR(255)")
            column(name: "DEPLOYMENT_ID_", type: "VARCHAR(64)")
            column(name: "BYTES_", type: "BLOB")
            column(name: "GENERATED_", type: "TINYINT")
        }
        createIndex(tableName: "ACT_GE_BYTEARRAY", indexName: "ACT_IDX_BYTEAR_DEPL") {
            column(name: "DEPLOYMENT_ID_", type: "VARCHAR(64)")
        }
        addForeignKeyConstraint(baseColumnNames: "DEPLOYMENT_ID_", baseTableName: "ACT_GE_BYTEARRAY", constraintName: "ACT_FK_BYTEARR_DEPL", referencedColumnNames: "ID_", referencedTableName: "ACT_RE_DEPLOYMENT")
    }

    changeSet(author: "vista yih", id: "2018-11-15-ACT_RE_MODEL") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_RE_MODEL")
            }
        }
        createTable(tableName: "ACT_RE_MODEL") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: "REV_", type: "INT")
            column(name: "NAME_", type: "VARCHAR(255)")
            column(name: "KEY_", type: "VARCHAR(255)")
            column(name: "CATEGORY_", type: "VARCHAR(255)")
            column(name: "CREATE_TIME_", type: "TIMESTAMP")
            column(name: "LAST_UPDATE_TIME_", type: "TIMESTAMP")
            column(name: "VERSION_", type: "INT")
            column(name: "META_INFO_", type: "VARCHAR(4000)")
            column(name: "DEPLOYMENT_ID_", type: "VARCHAR(64)")
            column(name: "EDITOR_SOURCE_VALUE_ID_", type: "VARCHAR(64)")
            column(name: "EDITOR_SOURCE_EXTRA_VALUE_ID_", type: "VARCHAR(64)")
            column(name: "TENANT_ID_", type: "VARCHAR(255)", defaultValue: "")
        }
        createIndex(tableName: "ACT_RE_MODEL", indexName: "ACT_IDX_MODEL_SOURCE") {
            column(name: "EDITOR_SOURCE_VALUE_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RE_MODEL", indexName: "ACT_IDX_MODEL_SOURCE_EXTRA") {
            column(name: "EDITOR_SOURCE_EXTRA_VALUE_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RE_MODEL", indexName: "ACT_IDX_MODEL_DEPLOYMENT") {
            column(name: "DEPLOYMENT_ID_", type: "VARCHAR(64)")
        }
        addForeignKeyConstraint(baseTableName: "ACT_RE_MODEL", baseColumnNames: "EDITOR_SOURCE_VALUE_ID_", constraintName: "ACT_FK_MODEL_SOURCE", referencedTableName: "ACT_GE_BYTEARRAY", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_RE_MODEL", baseColumnNames: "EDITOR_SOURCE_EXTRA_VALUE_ID_", constraintName: "ACT_FK_MODEL_SOURCE_EXTRA", referencedTableName: "ACT_GE_BYTEARRAY", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_RE_MODEL", baseColumnNames: "DEPLOYMENT_ID_", constraintName: "ACT_FK_MODEL_DEPLOYMENT", referencedTableName: "ACT_RE_DEPLOYMENT", referencedColumnNames: "ID_")
    }

    changeSet(author: "vista yih", id: "2018-11-15-ACT_RE_PROCDEF") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_RE_PROCDEF")
            }
        }
        createTable(tableName: "ACT_RE_PROCDEF") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: "REV_", type: "INT")
            column(name: "CATEGORY_", type: "VARCHAR(255)")
            column(name: "NAME_", type: "VARCHAR(255)")
            column(name: "KEY_", type: "VARCHAR(255)") {
                constraints(nullable: false)
            }
            column(name: "VERSION_", type: "INT") {
                constraints(nullable: false)
            }
            column(name: "DEPLOYMENT_ID_", type: "VARCHAR(64)")
            column(name: "RESOURCE_NAME_", type: "VARCHAR(4000)")
            column(name: "DGRM_RESOURCE_NAME_", type: "VARCHAR(4000)")
            column(name: "DESCRIPTION_", type: "VARCHAR(4000)")
            column(name: "HAS_START_FORM_KEY_", type: "TINYINT")
            column(name: "HAS_GRAPHICAL_NOTATION_", type: "TINYINT")
            column(name: "SUSPENSION_STATE_", type: "INT")
            column(name: "TENANT_ID_", type: "VARCHAR(255)", defaultValue: "")
            column(name: "ENGINE_VERSION_", type: "VARCHAR(255)")
        }
        if (!helper.isPostgresql()) {
            addUniqueConstraint(tableName: "ACT_RE_PROCDEF", columnNames: "KEY_,VERSION_, TENANT_ID_", constraintName: "ACT_UNIQ_PROCDEF")
        } else {
            addUniqueConstraint(tableName: "ACT_RE_PROCDEF", columnNames: "ID_,KEY_,VERSION_, TENANT_ID_", constraintName: "ACT_UNIQ_PROCDEF")
        }
    }

    changeSet(author: "vista yih", id: "2018-11-15-ACT_RU_EXECUTION") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_RU_EXECUTION")
            }
        }
        createTable(tableName: "ACT_RU_EXECUTION") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(primaryKey: true)
            }
            column(name: "REV_", type: "INT")
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
            column(name: "BUSINESS_KEY_", type: "VARCHAR(255)")
            column(name: "PARENT_ID_", type: "VARCHAR(64)")
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)")
            column(name: "SUPER_EXEC_", type: "VARCHAR(64)")
            column(name: "ROOT_PROC_INST_ID_", type: "VARCHAR(64)")
            column(name: "ACT_ID_", type: "VARCHAR(255)")
            column(name: "IS_ACTIVE_", type: "TINYINT")
            column(name: "IS_CONCURRENT_", type: "TINYINT")
            column(name: "IS_SCOPE_", type: "TINYINT")
            column(name: "IS_EVENT_SCOPE_", type: "TINYINT")
            column(name: "IS_MI_ROOT_", type: "TINYINT")
            column(name: "SUSPENSION_STATE_", type: "INT")
            column(name: "CACHED_ENT_STATE_", type: "INT")
            column(name: "TENANT_ID_", type: "VARCHAR(255)", defaultValue: "")
            column(name: "NAME_", type: "VARCHAR(255)")
            column(name: "START_TIME_", type: "TIMESTAMP")
            column(name: "START_USER_ID_", type: "VARCHAR(255)")
            column(name: "LOCK_TIME_", type: "TIMESTAMP")
            column(name: "IS_COUNT_ENABLED_", type: "TINYINT")
            column(name: "EVT_SUBSCR_COUNT_", type: "INT")
            column(name: "TASK_COUNT_", type: "INT")
            column(name: "JOB_COUNT_", type: "INT")
            column(name: "TIMER_JOB_COUNT_", type: "INT")
            column(name: "SUSP_JOB_COUNT_", type: "INT")
            column(name: "DEADLETTER_JOB_COUNT_", type: "INT")
            column(name: "VAR_COUNT_", type: "INT")
            column(name: "ID_LINK_COUNT_", type: "INT")
        }
        createIndex(tableName: "ACT_RU_EXECUTION", indexName: "ACT_IDX_EXEC_BUSKEY") {
            column(name: "BUSINESS_KEY_", type: "VARCHAR(255)")
        }
        createIndex(tableName: "ACT_RU_EXECUTION", indexName: "ACT_IDC_EXEC_ROOT") {
            column(name: "ROOT_PROC_INST_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_EXECUTION", indexName: "ACT_IDX_EXE_PROCINST") {
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_EXECUTION", indexName: "ACT_IDX_EXE_PARENT") {
            column(name: "PARENT_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_EXECUTION", indexName: "ACT_IDX_EXE_SUPER") {
            column(name: "SUPER_EXEC_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_EXECUTION", indexName: "ACT_IDX_EXE_PROCDEF") {
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)")
        }
        if (!helper.isSqlServer()) {
            if (helper.isMysql()) {
                addForeignKeyConstraint(baseTableName: "ACT_RU_EXECUTION", baseColumnNames: "PROC_INST_ID_", constraintName: "ACT_FK_EXE_PROCINST", referencedTableName: "ACT_RU_EXECUTION", referencedColumnNames: "ID_", onDelete: "CASCADE", onUpdate: "CASCADE")
            } else {
                addForeignKeyConstraint(baseTableName: "ACT_RU_EXECUTION", baseColumnNames: "PROC_INST_ID_", constraintName: "ACT_FK_EXE_PROCINST", referencedTableName: "ACT_RU_EXECUTION", referencedColumnNames: "ID_")
            }
        }
        if (helper.isMysql()) {
            addForeignKeyConstraint(baseTableName: "ACT_RU_EXECUTION", baseColumnNames: "PARENT_ID_", constraintName: "ACT_FK_EXE_PARENT", referencedTableName: "ACT_RU_EXECUTION", referencedColumnNames: "ID_", onDelete: "CASCADE")
            addForeignKeyConstraint(baseTableName: "ACT_RU_EXECUTION", baseColumnNames: "SUPER_EXEC_", constraintName: "ACT_FK_EXE_SUPER", referencedTableName: "ACT_RU_EXECUTION", referencedColumnNames: "ID_", onDelete: "CASCADE")
        } else {
            addForeignKeyConstraint(baseTableName: "ACT_RU_EXECUTION", baseColumnNames: "PARENT_ID_", constraintName: "ACT_FK_EXE_PARENT", referencedTableName: "ACT_RU_EXECUTION", referencedColumnNames: "ID_")
            addForeignKeyConstraint(baseTableName: "ACT_RU_EXECUTION", baseColumnNames: "SUPER_EXEC_", constraintName: "ACT_FK_EXE_SUPER", referencedTableName: "ACT_RU_EXECUTION", referencedColumnNames: "ID_")
        }
        addForeignKeyConstraint(baseTableName: "ACT_RU_EXECUTION", baseColumnNames: "PROC_DEF_ID_", constraintName: "ACT_FK_EXE_PROCDEF", referencedTableName: "ACT_RE_PROCDEF", referencedColumnNames: "ID_")
    }

    changeSet(author: "vista yih", id: "2018-11-15-ACT_RU_JOB") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_RU_JOB")
            }
        }
        createTable(tableName: "ACT_RU_JOB") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: "REV_", type: "INT")
            column(name: "TYPE_", type: "VARCHAR(255)") {
                constraints(nullable: false)
            }
            column(name: "LOCK_EXP_TIME_", type: "TIMESTAMP") {
                constraints(nullable: true)
            }
            column(name: "LOCK_OWNER_", type: "VARCHAR(255)")
            column(name: "EXCLUSIVE_", type: "BOOLEAN")
            column(name: "EXECUTION_ID_", type: "VARCHAR(64)")
            column(name: "PROCESS_INSTANCE_ID_", type: "VARCHAR(64)")
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)")
            column(name: "RETRIES_", type: "INT")
            column(name: "EXCEPTION_STACK_ID_", type: "VARCHAR(64)")
            column(name: "EXCEPTION_MSG_", type: "VARCHAR(4000)")
            column(name: "DUEDATE_", type: "TIMESTAMP")
            column(name: "REPEAT_", type: "VARCHAR(255)")
            column(name: "HANDLER_TYPE_", type: "VARCHAR(255)")
            column(name: "HANDLER_CFG_", type: "VARCHAR(4000)")
            column(name: "TENANT_ID_", type: "VARCHAR(255)", defaultValue: "")
        }
        createIndex(tableName: "ACT_RU_JOB", indexName: "ACT_IDX_JOB_EXECUTION_ID") {
            column(name: "EXECUTION_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_JOB", indexName: "ACT_IDX_JOB_PROC_INST_ID") {
            column(name: "PROCESS_INSTANCE_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_JOB", indexName: "ACT_IDX_JOB_PROC_DEF_ID") {
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_JOB", indexName: "ACT_IDX_JOB_EXCEPTION") {
            column(name: "EXCEPTION_STACK_ID_", type: "VARCHAR(64)")
        }
        addForeignKeyConstraint(baseTableName: "ACT_RU_JOB", baseColumnNames: "EXECUTION_ID_", constraintName: "ACT_FK_JOB_EXECUTION", referencedTableName: "ACT_RU_EXECUTION", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_RU_JOB", baseColumnNames: "PROCESS_INSTANCE_ID_", constraintName: "ACT_FK_JOB_PROCESS_INSTANCE", referencedTableName: "ACT_RU_EXECUTION", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_RU_JOB", baseColumnNames: "PROC_DEF_ID_", constraintName: "ACT_FK_JOB_PROC_DEF", referencedTableName: "ACT_RE_PROCDEF", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_RU_JOB", baseColumnNames: "EXCEPTION_STACK_ID_", constraintName: "ACT_FK_JOB_EXCEPTION", referencedTableName: "ACT_GE_BYTEARRAY", referencedColumnNames: "ID_")
    }

    changeSet(author: "vista yih", id: "2018-11-15-ACT_RU_TIMER_JOB") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_RU_TIMER_JOB")
            }
        }
        createTable(tableName: "ACT_RU_TIMER_JOB") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: "REV_", type: "INT")
            column(name: "TYPE_", type: "VARCHAR(255)") {
                constraints(nullable: false)
            }
            column(name: "LOCK_EXP_TIME_", type: "TIMESTAMP") {
                constraints(nullable: true)
            }
            column(name: "LOCK_OWNER_", type: "VARCHAR(255)")
            column(name: "EXCLUSIVE_", type: "BOOLEAN")
            column(name: "EXECUTION_ID_", type: "VARCHAR(64)")
            column(name: "PROCESS_INSTANCE_ID_", type: "VARCHAR(64)")
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)")
            column(name: "RETRIES_", type: "INT")
            column(name: "EXCEPTION_STACK_ID_", type: "VARCHAR(64)")
            column(name: "EXCEPTION_MSG_", type: "VARCHAR(4000)")
            column(name: "DUEDATE_", type: "TIMESTAMP")
            column(name: "REPEAT_", type: "VARCHAR(255)")
            column(name: "HANDLER_TYPE_", type: "VARCHAR(255)")
            column(name: "HANDLER_CFG_", type: "VARCHAR(4000)")
            column(name: "TENANT_ID_", type: "VARCHAR(255)", defaultValue: "")
        }
        createIndex(tableName: "ACT_RU_TIMER_JOB", indexName: "ACT_IDX_TJOB_EXECUTION_ID") {
            column(name: "EXECUTION_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_TIMER_JOB", indexName: "ACT_IDX_TJOB_PROC_INST_ID") {
            column(name: "PROCESS_INSTANCE_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_TIMER_JOB", indexName: "ACT_IDX_TJOB_PROC_DEF_ID") {
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_TIMER_JOB", indexName: "ACT_IDX_TJOB_EXCEPTION") {
            column(name: "EXCEPTION_STACK_ID_", type: "VARCHAR(64)")
        }
        addForeignKeyConstraint(baseTableName: "ACT_RU_TIMER_JOB", baseColumnNames: "EXECUTION_ID_", constraintName: "ACT_FK_TJOB_EXECUTION", referencedTableName: "ACT_RU_EXECUTION", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_RU_TIMER_JOB", baseColumnNames: "PROCESS_INSTANCE_ID_", constraintName: "ACT_FK_TJOB_PROCESS_INSTANCE", referencedTableName: "ACT_RU_EXECUTION", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_RU_TIMER_JOB", baseColumnNames: "PROC_DEF_ID_", constraintName: "ACT_FK_TJOB_PROC_DEF", referencedTableName: "ACT_RE_PROCDEF", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_RU_TIMER_JOB", baseColumnNames: "EXCEPTION_STACK_ID_", constraintName: "ACT_FK_TJOB_EXCEPTION", referencedTableName: "ACT_GE_BYTEARRAY", referencedColumnNames: "ID_")
    }

    changeSet(author: "vista yih", id: "2018-11-15-ACT_RU_SUSPENDED_JOB") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_RU_SUSPENDED_JOB")
            }
        }
        createTable(tableName: "ACT_RU_SUSPENDED_JOB") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: "REV_", type: "INT")
            column(name: "TYPE_", type: "VARCHAR(255)") {
                constraints(nullable: false)
            }
            column(name: "EXCLUSIVE_", type: "BOOLEAN")
            column(name: "EXECUTION_ID_", type: "VARCHAR(64)")
            column(name: "PROCESS_INSTANCE_ID_", type: "VARCHAR(64)")
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)")
            column(name: "RETRIES_", type: "INT")
            column(name: "EXCEPTION_STACK_ID_", type: "VARCHAR(64)")
            column(name: "EXCEPTION_MSG_", type: "VARCHAR(4000)")
            column(name: "DUEDATE_", type: "TIMESTAMP")
            column(name: "REPEAT_", type: "VARCHAR(255)")
            column(name: "HANDLER_TYPE_", type: "VARCHAR(255)")
            column(name: "HANDLER_CFG_", type: "VARCHAR(4000)")
            column(name: "TENANT_ID_", type: "VARCHAR(255)", defaultValue: "")
        }
        createIndex(tableName: "ACT_RU_SUSPENDED_JOB", indexName: "ACT_IDX_SJOB_EXECUTION_ID") {
            column(name: "EXECUTION_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_SUSPENDED_JOB", indexName: "ACT_IDX_SJOB_PROC_INST_ID") {
            column(name: "PROCESS_INSTANCE_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_SUSPENDED_JOB", indexName: "ACT_IDX_SJOB_PROC_DEF_ID") {
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_SUSPENDED_JOB", indexName: "ACT_IDX_SJOB_EXCEPTION") {
            column(name: "EXCEPTION_STACK_ID_", type: "VARCHAR(64)")
        }
        addForeignKeyConstraint(baseTableName: "ACT_RU_SUSPENDED_JOB", baseColumnNames: "EXECUTION_ID_", constraintName: "ACT_FK_SJOB_EXECUTION", referencedTableName: "ACT_RU_EXECUTION", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_RU_SUSPENDED_JOB", baseColumnNames: "PROCESS_INSTANCE_ID_", constraintName: "ACT_FK_SJOB_PROCESS_INSTANCE", referencedTableName: "ACT_RU_EXECUTION", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_RU_SUSPENDED_JOB", baseColumnNames: "PROC_DEF_ID_", constraintName: "ACT_FK_SJOB_PROC_DEF", referencedTableName: "ACT_RE_PROCDEF", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_RU_SUSPENDED_JOB", baseColumnNames: "EXCEPTION_STACK_ID_", constraintName: "ACT_FK_SJOB_EXCEPTION", referencedTableName: "ACT_GE_BYTEARRAY", referencedColumnNames: "ID_")
    }

    changeSet(author: "vista yih", id: "2018-11-15-ACT_RU_DEADLETTER_JOB") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_RU_DEADLETTER_JOB")
            }
        }
        createTable(tableName: "ACT_RU_DEADLETTER_JOB") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: "REV_", type: "INT")
            column(name: "TYPE_", type: "VARCHAR(255)") {
                constraints(nullable: false)
            }
            column(name: "EXCLUSIVE_", type: "BOOLEAN")
            column(name: "EXECUTION_ID_", type: "VARCHAR(64)")
            column(name: "PROCESS_INSTANCE_ID_", type: "VARCHAR(64)")
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)")
            column(name: "EXCEPTION_STACK_ID_", type: "VARCHAR(64)")
            column(name: "EXCEPTION_MSG_", type: "VARCHAR(4000)")
            column(name: "DUEDATE_", type: "TIMESTAMP")
            column(name: "REPEAT_", type: "VARCHAR(255)")
            column(name: "HANDLER_TYPE_", type: "VARCHAR(255)")
            column(name: "HANDLER_CFG_", type: "VARCHAR(4000)")
            column(name: "TENANT_ID_", type: "VARCHAR(255)", defaultValue: "")
        }
        createIndex(tableName: "ACT_RU_DEADLETTER_JOB", indexName: "ACT_IDX_DJOB_EXECUTION_ID") {
            column(name: "EXECUTION_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_DEADLETTER_JOB", indexName: "ACT_IDX_DJOB_PROC_INST_ID") {
            column(name: "PROCESS_INSTANCE_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_DEADLETTER_JOB", indexName: "ACT_IDX_DJOB_PROC_DEF_ID") {
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_DEADLETTER_JOB", indexName: "ACT_IDX_DJOB_EXCEPTION") {
            column(name: "EXCEPTION_STACK_ID_", type: "VARCHAR(64)")
        }
        addForeignKeyConstraint(baseTableName: "ACT_RU_DEADLETTER_JOB", baseColumnNames: "EXECUTION_ID_", constraintName: "ACT_FK_DJOB_EXECUTION", referencedTableName: "ACT_RU_EXECUTION", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_RU_DEADLETTER_JOB", baseColumnNames: "PROCESS_INSTANCE_ID_", constraintName: "ACT_FK_DJOB_PROCESS_INSTANCE", referencedTableName: "ACT_RU_EXECUTION", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_RU_DEADLETTER_JOB", baseColumnNames: "PROC_DEF_ID_", constraintName: "ACT_FK_DJOB_PROC_DEF", referencedTableName: "ACT_RE_PROCDEF", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_RU_DEADLETTER_JOB", baseColumnNames: "EXCEPTION_STACK_ID_", constraintName: "ACT_FK_DJOB_EXCEPTION", referencedTableName: "ACT_GE_BYTEARRAY", referencedColumnNames: "ID_")
    }

    changeSet(author: "vista yih", id: "2018-11-15-ACT_RU_TASK") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_RU_TASK")
            }
        }
        createTable(tableName: "ACT_RU_TASK") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(primaryKey: true)
            }
            column(name: "REV_", type: "INT")
            column(name: "EXECUTION_ID_", type: "VARCHAR(64)")
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)")
            column(name: "NAME_", type: "VARCHAR(255)")
            column(name: "PARENT_TASK_ID_", type: "VARCHAR(64)")
            column(name: "DESCRIPTION_", type: "VARCHAR(4000)")
            column(name: "TASK_DEF_KEY_", type: "VARCHAR(255)")
            column(name: "OWNER_", type: "VARCHAR(255)")
            column(name: "ASSIGNEE_", type: "VARCHAR(255)")
            column(name: "DELEGATION_", type: "VARCHAR(64)")
            column(name: "PRIORITY_", type: "INT")
            column(name: "CREATE_TIME_", type: "TIMESTAMP")
            column(name: "DUE_DATE_", type: "TIMESTAMP")
            column(name: "CATEGORY_", type: "VARCHAR(255)")
            column(name: "SUSPENSION_STATE_", type: "INT")
            column(name: "TENANT_ID_", type: "VARCHAR(255)", defaultValue: "")
            column(name: "FORM_KEY_", type: "VARCHAR(255)")
            column(name: "CLAIM_TIME_", type: "TIMESTAMP")
        }
        createIndex(tableName: "ACT_RU_TASK", indexName: "ACT_IDX_TASK_CREATE") {
            column(name: "CREATE_TIME_", type: "TIMESTAMP")
        }
        createIndex(tableName: "ACT_RU_TASK", indexName: "ACT_IDX_TASK_EXEC") {
            column(name: "EXECUTION_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_TASK", indexName: "ACT_IDX_TASK_PROCINST") {
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_TASK", indexName: "ACT_IDX_TASK_PROCDEF") {
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)")
        }
        addForeignKeyConstraint(baseTableName: "ACT_RU_TASK", baseColumnNames: "EXECUTION_ID_", constraintName: "ACT_FK_TASK_EXE", referencedTableName: "ACT_RU_EXECUTION", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_RU_TASK", baseColumnNames: "PROC_INST_ID_", constraintName: "ACT_FK_TASK_PROCINST", referencedTableName: "ACT_RU_EXECUTION", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_RU_TASK", baseColumnNames: "PROC_DEF_ID_", constraintName: "ACT_FK_TASK_PROCDEF", referencedTableName: "ACT_RE_PROCDEF", referencedColumnNames: "ID_")
    }

    changeSet(author: "vista yih", id: "2018-11-15-ACT_RU_IDENTITYLINK") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_RU_IDENTITYLINK")
            }
        }
        createTable(tableName: "ACT_RU_IDENTITYLINK") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(primaryKey: true)
            }
            column(name: "REV_", type: "INT")
            column(name: "GROUP_ID_", type: "VARCHAR(255)")
            column(name: "TYPE_", type: "VARCHAR(255)")
            column(name: "USER_ID_", type: "VARCHAR(255)")
            column(name: "TASK_ID_", type: "VARCHAR(64)")
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_IDENTITYLINK", indexName: "ACT_IDX_IDENT_LNK_USER") {
            column(name: "USER_ID_", type: "VARCHAR(255)")
        }
        createIndex(tableName: "ACT_RU_IDENTITYLINK", indexName: "ACT_IDX_IDENT_LNK_GROUP") {
            column(name: "GROUP_ID_", type: "VARCHAR(255)")
        }
        createIndex(tableName: "ACT_RU_IDENTITYLINK", indexName: "ACT_IDX_TSKASS_TASK") {
            column(name: "TASK_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_IDENTITYLINK", indexName: "ACT_IDX_ATHRZ_PROCEDEF") {
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_IDENTITYLINK", indexName: "ACT_IDX_IDL_PROCINST") {
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
        }
        addForeignKeyConstraint(baseTableName: "ACT_RU_IDENTITYLINK", baseColumnNames: "TASK_ID_", constraintName: "ACT_FK_TSKASS_TASK", referencedTableName: "ACT_RU_TASK", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_RU_IDENTITYLINK", baseColumnNames: "PROC_DEF_ID_", constraintName: "ACT_FK_ATHRZ_PROCEDEF", referencedTableName: "ACT_RE_PROCDEF", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_RU_IDENTITYLINK", baseColumnNames: "PROC_INST_ID_", constraintName: "ACT_FK_IDL_PROCINST", referencedTableName: "ACT_RU_EXECUTION", referencedColumnNames: "ID_")
    }

    changeSet(author: "vista yih", id: "2018-11-15-ACT_RU_VARIABLE") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_RU_VARIABLE")
            }
        }
        createTable(tableName: "ACT_RU_VARIABLE") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: "REV_", type: "INT")
            column(name: "TYPE_", type: "VARCHAR(255)") {
                constraints(nullable: false)
            }
            column(name: "NAME_", type: "VARCHAR(255)") {
                constraints(nullable: false)
            }
            column(name: "EXECUTION_ID_", type: "VARCHAR(64)")
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
            column(name: "TASK_ID_", type: "VARCHAR(64)")
            column(name: "BYTEARRAY_ID_", type: "VARCHAR(64)")
            column(name: "DOUBLE_", type: "DOUBLE")
            column(name: "LONG_", type: "BIGINT")
            column(name: "TEXT_", type: "VARCHAR(4000)")
            column(name: "TEXT2_", type: "VARCHAR(4000)")
        }
        createIndex(tableName: "ACT_RU_VARIABLE", indexName: "ACT_IDX_VARIABLE_TASK_ID") {
            column(name: "TASK_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_VARIABLE", indexName: "ACT_IDX_VAR_EXE") {
            column(name: "EXECUTION_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_VARIABLE", indexName: "ACT_IDX_VAR_PROCINST") {
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_RU_VARIABLE", indexName: "ACT_IDX_VAR_BYTEARRAY") {
            column(name: "BYTEARRAY_ID_", type: "VARCHAR(64)")
        }
        addForeignKeyConstraint(baseTableName: "ACT_RU_VARIABLE", baseColumnNames: "EXECUTION_ID_", constraintName: "ACT_FK_VAR_EXE", referencedTableName: "ACT_RU_EXECUTION", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_RU_VARIABLE", baseColumnNames: "PROC_INST_ID_", constraintName: "ACT_FK_VAR_PROCINST", referencedTableName: "ACT_RU_EXECUTION", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_RU_VARIABLE", baseColumnNames: "BYTEARRAY_ID_", constraintName: "ACT_FK_VAR_BYTEARRAY", referencedTableName: "ACT_GE_BYTEARRAY", referencedColumnNames: "ID_")
    }

    changeSet(author: "vista yih", id: "2018-11-15-ACT_RU_EVENT_SUBSCR") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_RU_EVENT_SUBSCR")
            }
        }
        createTable(tableName: "ACT_RU_EVENT_SUBSCR") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: "REV_", type: "INT")
            column(name: "EVENT_TYPE_", type: "VARCHAR(255)") {
                constraints(nullable: false)
            }
            column(name: "EVENT_NAME_", type: "VARCHAR(255)")
            column(name: "EXECUTION_ID_", type: "VARCHAR(64)")
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
            column(name: "ACTIVITY_ID_", type: "VARCHAR(64)")
            column(name: "CONFIGURATION_", type: "VARCHAR(255)")
            column(name: "CREATED_", type: "TIMESTAMP", defaultValueComputed: "CURRENT_TIMESTAMP") {
                constraints(nullable: false)
            }
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)")
            column(name: "TENANT_ID_", type: "VARCHAR(255)", defaultValue: "")
        }
        createIndex(tableName: "ACT_RU_EVENT_SUBSCR", indexName: "ACT_IDX_EVENT_SUBSCR_CONFIG_") {
            column(name: "CONFIGURATION_", type: "VARCHAR(255)")
        }
        createIndex(tableName: "ACT_RU_EVENT_SUBSCR", indexName: "ACT_IDX_EVENT_SUBSCR") {
            column(name: "EXECUTION_ID_", type: "VARCHAR(64)")
        }
        addForeignKeyConstraint(baseTableName: "ACT_RU_EVENT_SUBSCR", baseColumnNames: "EXECUTION_ID_", constraintName: "ACT_FK_EVENT_EXEC", referencedTableName: "ACT_RU_EXECUTION", referencedColumnNames: "ID_")
    }

    changeSet(author: "vista yih", id: "2018-11-15-ACT_EVT_LOG") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_EVT_LOG")
            }
        }
        createTable(tableName: "ACT_EVT_LOG") {
            column(name: "LOG_NR_", type: "BIGINT", autoIncrement: true) {
                constraints(primaryKey: true)
            }
            column(name: "TYPE_", type: "VARCHAR(64)")
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)")
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
            column(name: "EXECUTION_ID_", type: "VARCHAR(64)")
            column(name: "TASK_ID_", type: "VARCHAR(64)")
            column(name: "TIME_STAMP_", type: "TIMESTAMP") {
                constraints(nullable: false)
            }
            column(name: "USER_ID_", type: "VARCHAR(255)")
            column(name: "DATA_", type: "BLOB")
            column(name: "LOCK_OWNER_", type: "VARCHAR(255)")
            column(name: "LOCK_TIME_", type: "TIMESTAMP")
            column(name: "IS_PROCESSED_", type: "TINYINT", defaultValue: 0)
        }
    }

    changeSet(author: "vista yih", id: "2018-11-15-ACT_PROCDEF_INFO") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_PROCDEF_INFO")
            }
        }
        createTable(tableName: "ACT_PROCDEF_INFO") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)") {
                constraints(nullable: false)
            }
            column(name: "REV_", type: "INT")
            column(name: "INFO_JSON_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_PROCDEF_INFO", indexName: "ACT_IDX_PROCDEF_INFO_JSON") {
            column(name: "INFO_JSON_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_PROCDEF_INFO", indexName: "ACT_IDX_PROCDEF_INFO_PROC") {
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)")
        }
        addForeignKeyConstraint(baseTableName: "ACT_PROCDEF_INFO", baseColumnNames: "INFO_JSON_ID_", constraintName: "ACT_FK_INFO_JSON_BA", referencedTableName: "ACT_GE_BYTEARRAY", referencedColumnNames: "ID_")
        addForeignKeyConstraint(baseTableName: "ACT_PROCDEF_INFO", baseColumnNames: "PROC_DEF_ID_", constraintName: "ACT_FK_INFO_PROCDEF", referencedTableName: "ACT_RE_PROCDEF", referencedColumnNames: "ID_")
        if (!helper.isHana()) {
            if (!helper.isPostgresql()) {
                addUniqueConstraint(tableName: "ACT_PROCDEF_INFO", columnNames: "PROC_DEF_ID_", constraintName: "ACT_UNIQ_INFO_PROCDEF")
            } else {
                addUniqueConstraint(tableName: "ACT_PROCDEF_INFO", columnNames: "ID_,PROC_DEF_ID_", constraintName: "ACT_UNIQ_INFO_PROCDEF")
            }
        }
    }

    changeSet(author: "vista yih", id: "2018-11-15-ACT_HI_PROCINST") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_HI_PROCINST")
            }
        }
        createTable(tableName: "ACT_HI_PROCINST") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)") {
                constraints(nullable: false)
            }
            column(name: "BUSINESS_KEY_", type: "VARCHAR(255)")
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)") {
                constraints(nullable: false)
            }
            column(name: "START_TIME_", type: "TIMESTAMP") {
                constraints(nullable: false)
            }
            column(name: "END_TIME_", type: "TIMESTAMP")
            column(name: "DURATION_", type: "BIGINT")
            column(name: "START_USER_ID_", type: "VARCHAR(255)")
            column(name: "START_ACT_ID_", type: "VARCHAR(255)")
            column(name: "END_ACT_ID_", type: "VARCHAR(255)")
            column(name: "SUPER_PROCESS_INSTANCE_ID_", type: "VARCHAR(64)")
            column(name: "DELETE_REASON_", type: "VARCHAR(4000)")
            column(name: "TENANT_ID_", type: "VARCHAR(255)", defaultValue: "")
            column(name: "NAME_", type: "VARCHAR(255)")
        }
        createIndex(tableName: "ACT_HI_PROCINST", indexName: "ACT_IDX_HI_PRO_INST_END") {
            column(name: "END_TIME_", type: "TIMESTAMP")
        }
        createIndex(tableName: "ACT_HI_PROCINST", indexName: "ACT_IDX_HI_PRO_I_BUSKEY") {
            column(name: "BUSINESS_KEY_", type: "VARCHAR(255)")
        }
        if (!helper.isPostgresql()) {
            addUniqueConstraint(tableName: "ACT_HI_PROCINST", columnNames: "PROC_INST_ID_", constraintName: "ACT_UNIQ_PROCINST_HI")
        } else {
            addUniqueConstraint(tableName: "ACT_HI_PROCINST", columnNames: "ID_,PROC_INST_ID_", constraintName: "ACT_UNIQ_PROCINST_HI")
        }
    }
    changeSet(author: "vista yih", id: "2018-11-15-ACT_HI_ACTINST") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_HI_ACTINST")
            }
        }
        createTable(tableName: "ACT_HI_ACTINST") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)") {
                constraints(nullable: false)
            }
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)") {
                constraints(nullable: false)
            }
            column(name: "EXECUTION_ID_", type: "VARCHAR(64)") {
                constraints(nullable: false)
            }
            column(name: "ACT_ID_", type: "VARCHAR(255)") {
                constraints(nullable: false)
            }
            column(name: "TASK_ID_", type: "VARCHAR(64)")
            column(name: "CALL_PROC_INST_ID_", type: "VARCHAR(64)")
            column(name: "ACT_NAME_", type: "VARCHAR(255)")
            column(name: "ACT_TYPE_", type: "VARCHAR(255)") {
                constraints(nullable: false)
            }
            column(name: "ASSIGNEE_", type: "VARCHAR(255)")
            column(name: "START_TIME_", type: "TIMESTAMP") {
                constraints(nullable: false)
            }
            column(name: "END_TIME_", type: "TIMESTAMP")
            column(name: "DURATION_", type: "BIGINT")
            column(name: "DELETE_REASON_", type: "VARCHAR(4000)")
            column(name: "TENANT_ID_", type: "VARCHAR(255)", defaultValue: "")
        }
        createIndex(tableName: "ACT_HI_ACTINST", indexName: "ACT_IDX_HI_ACT_INST_START") {
            column(name: "START_TIME_", type: "TIMESTAMP")
        }
        createIndex(tableName: "ACT_HI_ACTINST", indexName: "ACT_IDX_HI_ACT_INST_END") {
            column(name: "END_TIME_", type: "TIMESTAMP")
        }
        createIndex(tableName: "ACT_HI_ACTINST", indexName: "ACT_IDX_HI_ACT_INST_PROCINST") {
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
            column(name: "ACT_ID_", type: "VARCHAR(255)")
        }
        createIndex(tableName: "ACT_HI_ACTINST", indexName: "ACT_IDX_HI_ACT_INST_EXEC") {
            column(name: "EXECUTION_ID_", type: "VARCHAR(64)")
            column(name: "ACT_ID_", type: "VARCHAR(255)")
        }
    }
    changeSet(author: "vista yih", id: "2018-11-15-ACT_HI_TASKINST") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_HI_TASKINST")
            }
        }
        createTable(tableName: "ACT_HI_TASKINST") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: "PROC_DEF_ID_", type: "VARCHAR(64)")
            column(name: "TASK_DEF_KEY_", type: "VARCHAR(255)")
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
            column(name: "EXECUTION_ID_", type: "VARCHAR(64)")
            column(name: "NAME_", type: "VARCHAR(255)")
            column(name: "PARENT_TASK_ID_", type: "VARCHAR(64)")
            column(name: "DESCRIPTION_", type: "VARCHAR(4000)")
            column(name: "OWNER_", type: "VARCHAR(255)")
            column(name: "ASSIGNEE_", type: "VARCHAR(255)")
            column(name: "START_TIME_", type: "TIMESTAMP") {
                constraints(nullable: false)
            }
            column(name: "CLAIM_TIME_", type: "TIMESTAMP")
            column(name: "END_TIME_", type: "TIMESTAMP")
            column(name: "DURATION_", type: "bigint")
            column(name: "DELETE_REASON_", type: "VARCHAR(4000)")
            column(name: "PRIORITY_", type: "INT")
            column(name: "DUE_DATE_", type: "TIMESTAMP")
            column(name: "FORM_KEY_", type: "VARCHAR(255)")
            column(name: "CATEGORY_", type: "VARCHAR(255)")
            column(name: "TENANT_ID_", type: "VARCHAR(255)", defaultValue: "")
        }
        createIndex(tableName: "ACT_HI_TASKINST", indexName: "ACT_IDX_HI_TASK_INST_PROCINST") {
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
        }
    }

    changeSet(author: "vista yih", id: "2018-11-15-ACT_HI_VARINST") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_HI_VARINST")
            }
        }
        createTable(tableName: "ACT_HI_VARINST") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
            column(name: "EXECUTION_ID_", type: "VARCHAR(64)")
            column(name: "TASK_ID_", type: "VARCHAR(64)")
            column(name: "NAME_", type: "VARCHAR(255)") {
                constraints(nullable: false)
            }
            column(name: "VAR_TYPE_", type: "VARCHAR(100)")
            column(name: "REV_", type: "INT")
            column(name: "BYTEARRAY_ID_", type: "VARCHAR(64)")
            column(name: "DOUBLE_", type: "DOUBLE")
            column(name: "LONG_", type: "BIGINT")
            column(name: "TEXT_", type: "VARCHAR(4000)")
            column(name: "TEXT2_", type: "VARCHAR(4000)")
            column(name: "CREATE_TIME_", type: "TIMESTAMP")
            column(name: "LAST_UPDATED_TIME_", type: "TIMESTAMP")
        }
        createIndex(tableName: "ACT_HI_VARINST", indexName: "ACT_IDX_HI_PROCVAR_PROC_INST") {
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_HI_VARINST", indexName: "ACT_IDX_HI_PROCVAR_NAME_TYPE") {
            column(name: "NAME_", type: "VARCHAR(255)")
            column(name: "VAR_TYPE_", type: "VARCHAR(255)")
        }
        createIndex(tableName: "ACT_HI_VARINST", indexName: "ACT_IDX_HI_PROCVAR_TASK_ID") {
            column(name: "TASK_ID_", type: "VARCHAR(64)")
        }
    }
    changeSet(author: "vista yih", id: "2018-11-15-ACT_HI_DETAIL") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_HI_DETAIL")
            }
        }
        createTable(tableName: "ACT_HI_DETAIL") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: "TYPE_", type: "VARCHAR(255)") {
                constraints(nullable: false)
            }
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
            column(name: "EXECUTION_ID_", type: "VARCHAR(64)")
            column(name: "TASK_ID_", type: "VARCHAR(64)")
            column(name: "ACT_INST_ID_", type: "VARCHAR(64)")
            column(name: "NAME_", type: "VARCHAR(255)") {
                constraints(nullable: false)
            }
            column(name: "VAR_TYPE_", type: "VARCHAR(64)")
            column(name: "REV_", type: "INT")
            column(name: "TIME_", type: "TIMESTAMP") {
                constraints(nullable: false)
            }
            column(name: "BYTEARRAY_ID_", type: "VARCHAR(64)")
            column(name: "DOUBLE_", type: "DOUBLE")
            column(name: "LONG_", type: "BIGINT")
            column(name: "TEXT_", type: "VARCHAR(4000)")
            column(name: "TEXT2_", type: "VARCHAR(4000)")
        }
        createIndex(tableName: "ACT_HI_DETAIL", indexName: "ACT_IDX_HI_DETAIL_PROC_INST") {
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_HI_DETAIL", indexName: "ACT_IDX_HI_DETAIL_ACT_INST") {
            column(name: "ACT_INST_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_HI_DETAIL", indexName: "ACT_IDX_HI_DETAIL_TIME") {
            column(name: "TIME_", type: "TIMESTAMP")
        }
        createIndex(tableName: "ACT_HI_DETAIL", indexName: "ACT_IDX_HI_DETAIL_NAME") {
            column(name: "NAME_", type: "VARCHAR(255)")
        }
        createIndex(tableName: "ACT_HI_DETAIL", indexName: "ACT_IDX_HI_DETAIL_TASK_ID") {
            column(name: "TASK_ID_", type: "VARCHAR(64)")
        }
    }

    changeSet(author: "vista yih", id: "2018-11-15-ACT_HI_COMMENT") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_HI_COMMENT")
            }
        }
        createTable(tableName: "ACT_HI_COMMENT") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: "TYPE_", type: "VARCHAR(255)")
            column(name: "TIME_", type: "TIMESTAMP") {
                constraints(nullable: false)
            }
            column(name: "USER_ID_", type: "VARCHAR(255)")
            column(name: "TASK_ID_", type: "VARCHAR(64)")
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
            column(name: "ACTION_", type: "VARCHAR(255)")
            column(name: "MESSAGE_", type: "VARCHAR(4000)")
            column(name: "FULL_MSG_", type: "BLOB")
        }
    }
    changeSet(author: "vista yih", id: "2018-11-15-ACT_HI_ATTACHMENT") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_HI_ATTACHMENT")
            }
        }
        createTable(tableName: "ACT_HI_ATTACHMENT") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: "REV_", type: "INT")
            column(name: "USER_ID_", type: "VARCHAR(255)")
            column(name: "NAME_", type: "VARCHAR(255)")
            column(name: "DESCRIPTION_", type: "VARCHAR(4000)")
            column(name: "TYPE_", type: "VARCHAR(255)")
            column(name: "TASK_ID_", type: "VARCHAR(64)")
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
            column(name: "URL_", type: "VARCHAR(4000)")
            column(name: "CONTENT_ID_", type: "VARCHAR(64)")
            column(name: "TIME_", type: "TIMESTAMP")
        }
    }
    changeSet(author: "vista yih", id: "2018-11-15-ACT_HI_IDENTITYLINK") {
        preConditions(onFail: "MARK_RAN") {
            not {
                tableExists(tableName: "ACT_HI_IDENTITYLINK")
            }
        }
        createTable(tableName: "ACT_HI_IDENTITYLINK") {
            column(name: "ID_", type: "VARCHAR(64)") {
                constraints(primaryKey: true)
            }
            column(name: "GROUP_ID_", type: "VARCHAR(255)")
            column(name: "TYPE_", type: "VARCHAR(255)")
            column(name: "USER_ID_", type: "VARCHAR(255)")
            column(name: "TASK_ID_", type: "VARCHAR(64)")
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_HI_IDENTITYLINK", indexName: "ACT_IDX_HI_IDENT_LNK_USER") {
            column(name: "USER_ID_", type: "VARCHAR(255)")
        }
        createIndex(tableName: "ACT_HI_IDENTITYLINK", indexName: "ACT_IDX_HI_IDENT_LNK_TASK") {
            column(name: "TASK_ID_", type: "VARCHAR(64)")
        }
        createIndex(tableName: "ACT_HI_IDENTITYLINK", indexName: "ACT_IDX_HI_IDENT_LNK_PROCINST") {
            column(name: "PROC_INST_ID_", type: "VARCHAR(64)")
        }
    }

    def dbType = helper.dbType().toString()

    changeSet(author: "jessen", id: "20160926-activiti.create.engine-1") {
        sqlFile(path: "script/db/data/" + dbType + "/init/wfl_attach_category.sql", encoding: "UTF-8")
    }
}
