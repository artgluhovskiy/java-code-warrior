databaseChangeLog {
  changeSet(id: '1562927966888-1', author: 'ahlukhouski (generated)') {
    createTable(tableName: 'hibernate_sequence') {
      column(name: 'next_val', type: 'BIGINT')
    }
  }

  changeSet(id: '1562927966888-2', author: 'ahlukhouski (generated)') {
    createTable(tableName: 'roles') {
      column(name: 'ID', type: 'BIGINT') {
        constraints(primaryKey: true)
      }
      column(name: 'NAME', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1562927966888-3', author: 'ahlukhouski (generated)') {
    createTable(tableName: 'task_orders') {
      column(name: 'ID', type: 'BIGINT') {
        constraints(primaryKey: true)
      }
      column(name: 'TASK_DESCRIPTION', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'TASK_NAME', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'TASK_NAME_ID', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'REG_DATE', type: 'datetime')
      column(name: 'USER_ID', type: 'BIGINT')
    }
  }

  changeSet(id: '1562927966888-4', author: 'ahlukhouski (generated)') {
    createTable(tableName: 'user_roles') {
      column(name: 'USER_ID', type: 'BIGINT') {
        constraints(nullable: false)
      }
      column(name: 'ROLE_ID', type: 'BIGINT') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1562927966888-5', author: 'ahlukhouski (generated)') {
    createTable(tableName: 'users') {
      column(name: 'ID', type: 'BIGINT') {
        constraints(primaryKey: true)
      }
      column(name: 'EMAIL', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'ENABLED', type: 'BIT(1)')
      column(name: 'FIRST_NAME', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'LAST_NAME', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'PASSWORD', type: 'VARCHAR(255)') {
        constraints(nullable: false)
      }
      column(name: 'REG_DATE', type: 'datetime')
    }
  }

  changeSet(id: '1562927966888-6', author: 'ahlukhouski (generated)') {
    createIndex(indexName: 'FK61x3nvxbu49xad90tp9f8kmo1', tableName: 'user_roles') {
      column(name: 'ROLE_ID')
    }
  }

  changeSet(id: '1562927966888-7', author: 'ahlukhouski (generated)') {
    createIndex(indexName: 'FKk3qtke64s9k5pv5hoq6yyq7py', tableName: 'user_roles') {
      column(name: 'USER_ID')
    }
  }

  changeSet(id: '1562927966888-8', author: 'ahlukhouski (generated)') {
    createIndex(indexName: 'FKl1meqc505b691fn3o9s7mumxh', tableName: 'task_orders') {
      column(name: 'USER_ID')
    }
  }
}
