<template>
  <div class="app-container">
    <div class="header-section">
      <div class="header-content">
        <h2 class="page-title">数据源管理</h2>
        <p class="page-desc">配置和管理系统连接的多源数据库，支持动态切换和连接池监控。</p>
      </div>
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd" v-hasPermi="['tool:datasource:add']">新建连接</el-button>
    </div>

    <!-- Data Source Cards -->
    <el-row :gutter="24" class="source-list" v-loading="loading">
      <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="(item, index) in sourceList" :key="index">
        <el-card class="source-card" shadow="hover">
          <div class="card-status" :class="item.status === '0' ? 'active' : 'inactive'"></div>
          <div class="card-header-actions">
            <el-dropdown trigger="click">
              <span class="el-dropdown-link">
                <i class="el-icon-more"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item icon="el-icon-edit" @click.native="handleEdit(item)" v-hasPermi="['tool:datasource:edit']">编辑</el-dropdown-item>
                <el-dropdown-item icon="el-icon-share" @click.native="handleGraph(item)" v-hasPermi="['tool:datasource:query']">知识图谱</el-dropdown-item>
                <el-dropdown-item icon="el-icon-refresh" @click.native="handleTest(item)" v-hasPermi="['tool:datasource:query']">测试连接</el-dropdown-item>
                <el-dropdown-item icon="el-icon-delete" divided style="color: #f56c6c;" @click.native="handleDelete(item)" v-hasPermi="['tool:datasource:remove']">删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
          
          <div class="source-icon">
            <img :src="getDbIcon(item.type)" :alt="item.type">
          </div>
          
          <div class="source-info">
            <h3 class="source-name">{{ item.name }}</h3>
            <div class="source-meta">
              <el-tag size="mini" effect="plain" :type="getDbTypeTag(item.type)">{{ item.type }}</el-tag>
              <el-tooltip :content="item.host" placement="top" :open-delay="500">
                <span class="source-host">{{ item.host }}</span>
              </el-tooltip>
            </div>
          </div>

          <div class="source-stats">
            <div class="stat-item">
              <span class="label">连接池</span>
              <span class="value">{{ item.activeConn || 0 }}/{{ item.maxConn || item.maxActive || 20 }}</span>
            </div>
            <div class="stat-item">
              <span class="label">延迟</span>
              <span class="value success">{{ item.latency || '-' }}ms</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Add/Edit Dialog -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body custom-class="datasource-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="连接名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入连接名称（如：主库_MySQL）" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择">
                <el-option label="MySQL" value="MySQL" />
                <el-option label="PostgreSQL" value="PostgreSQL" />
                <el-option label="Oracle" value="Oracle" />
                <el-option label="SQL Server" value="SQLServer" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="驱动类" prop="driver">
              <el-select v-model="form.driver" filterable allow-create default-first-option placeholder="请选择或输入驱动类">
                <el-option
                  v-for="item in driverOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="18">
            <el-form-item label="主机地址" prop="host">
              <el-input v-model="form.host" placeholder="127.0.0.1" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="端口" prop="port">
              <el-input v-model="form.port" placeholder="3306" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="数据库名" prop="dbName">
              <el-input v-model="form.dbName" placeholder="ry-vue" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" placeholder="root" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input v-model="form.password" type="password" placeholder="请输入密码" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
             <el-divider content-position="left">连接池配置 (Druid)</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="初始连接数">
              <el-input-number v-model="form.initialSize" :min="1" :max="100" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大连接数">
              <el-input-number v-model="form.maxActive" :min="1" :max="200" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="warning" plain icon="el-icon-connection" @click="handleTestForm">测试连接</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- Knowledge Graph Dialog -->
    <el-dialog :title="graphTitle" :visible.sync="graphOpen" width="80%" append-to-body destroy-on-close custom-class="graph-dialog">
        <schema-graph v-if="graphOpen" :data-source-id="currentDataSourceId" />
    </el-dialog>
  </div>
</template>

<script>
import { listDataSource, getDataSource, delDataSource, addDataSource, updateDataSource, testDataSource } from "@/api/tool/datasource";
import SchemaGraph from "./SchemaGraph";

export default {
  name: "DataSource",
  components: { SchemaGraph },
  data() {
    return {
      // Masking loading state
      loading: true,
      // Source List
      sourceList: [],
      title: "",
      open: false,
      // Graph Dialog
      graphOpen: false,
      graphTitle: "",
      currentDataSourceId: null,
      form: {},
      // Query parameters
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        type: undefined
      },
      rules: {
        name: [{ required: true, message: "连接名称不能为空", trigger: "blur" }],
        host: [{ required: true, message: "主机地址不能为空", trigger: "blur" }],
        username: [{ required: true, message: "用户名不能为空", trigger: "blur" }]
      },
      driverOptions: [
        { value: 'com.mysql.cj.jdbc.Driver', label: 'MySQL (com.mysql.cj.jdbc.Driver)' },
        { value: 'com.mysql.jdbc.Driver', label: 'MySQL 5.x (com.mysql.jdbc.Driver)' },
        { value: 'org.postgresql.Driver', label: 'PostgreSQL (org.postgresql.Driver)' },
        { value: 'oracle.jdbc.driver.OracleDriver', label: 'Oracle (oracle.jdbc.driver.OracleDriver)' },
        { value: 'com.microsoft.sqlserver.jdbc.SQLServerDriver', label: 'SQL Server (com.microsoft.sqlserver.jdbc.SQLServerDriver)' }
      ]
    };
  },
  watch: {
    'form.type': function(val) {
      if (val && !this.form.driver) {
        if (val === 'MySQL') this.form.driver = 'com.mysql.cj.jdbc.Driver';
        else if (val === 'PostgreSQL') this.form.driver = 'org.postgresql.Driver';
        else if (val === 'Oracle') this.form.driver = 'oracle.jdbc.driver.OracleDriver';
        else if (val === 'SQLServer') this.form.driver = 'com.microsoft.sqlserver.jdbc.SQLServerDriver';
      }
    }
  },
  created() {
    this.getList();
  },
  methods: {
    getDbIcon(type) {
        if (!type) return '';
        return `https://cdn.jsdelivr.net/gh/devicons/devicon/icons/${type.toLowerCase()}/${type.toLowerCase()}-original.svg`;
    },
    getDbTypeTag(type) {
      const map = {
        'MySQL': '',
        'PostgreSQL': 'success',
        'Oracle': 'danger',
        'SQLServer': 'warning'
      }
      return map[type] || 'info';
    },
    /** Query Data Source List */
    getList() {
      this.loading = true;
      listDataSource(this.queryParams).then(response => {
        this.sourceList = response.rows.map(item => ({
            ...item,
            status: '0', // Default to active for display or need real check
            activeConn: 0,
            maxConn: item.maxActive,
            latency: '-'
        }));
        this.loading = false;
        // Asynchronously check status for all sources
        this.checkAllStatus();
      });
    },
    checkAllStatus() {
        this.sourceList.forEach((item, index) => {
            testDataSource(item).then(response => {
                if (response.code === 200 && response.data) {
                    const info = response.data;
                    this.$set(this.sourceList, index, {
                        ...item,
                        status: '0', // Active
                        activeConn: info.activeConn || 0,
                        maxConn: info.maxConn || item.maxActive,
                        latency: info.latency
                    });
                }
            }).catch(() => {
                 this.$set(this.sourceList, index, {
                    ...item,
                    status: '1', // Inactive
                    latency: 'Err'
                });
            });
        });
    },
    // Cancel Button
    cancel() {
      this.open = false;
      this.reset();
    },
    // Form Reset
    reset() {
      this.form = {
        id: undefined,
        name: undefined,
        type: "MySQL",
        host: "127.0.0.1",
        port: "3306",
        username: "root",
        password: undefined,
        dbName: undefined,
        initialSize: 5,
        maxActive: 20
      };
      this.resetForm("form");
    },
    /** Search Button Operation */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** Add Button Operation */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加数据源";
    },
    /** Knowledge Graph */
    handleGraph(row) {
        this.currentDataSourceId = row.id || this.ids;
        this.graphTitle = `知识图谱 - ${row.name}`;
        this.graphOpen = true;
    },
    /** Edit Button Operation */
    handleEdit(row) {
      this.reset();
      const id = row.id || this.ids;
      getDataSource(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改数据源";
      });
    },
    /** Submit Button */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateDataSource(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDataSource(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** Delete Button Operation */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除数据源编号为"' + ids + '"的数据项？').then(function() {
        return delDataSource(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** Test Connection (List Item) */
    handleTest(row) {
        this.loading = true;
        testDataSource(row).then(response => {
            this.loading = false;
            this.$modal.msgSuccess("连接成功！");
        }).catch(() => {
            this.loading = false;
        });
    },
    /** Test Connection (Form) */
    handleTestForm() {
        this.$refs["form"].validate(valid => {
            if (valid) {
                const loading = this.$loading({
                    lock: true,
                    text: '正在测试连接...',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                testDataSource(this.form).then(response => {
                    loading.close();
                    this.$modal.msgSuccess("连接成功！");
                }).catch(() => {
                    loading.close();
                });
            }
        });
    }
  }
};
</script>

<style lang="scss" scoped>
.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  
  .header-content {
    .page-title {
      font-size: 20px;
      font-weight: 600;
      color: #1e293b;
      margin: 0 0 8px 0;
    }
    .page-desc {
      color: #64748b;
      margin: 0;
      font-size: 14px;
    }
  }
}

.source-card {
  position: relative;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
  margin-bottom: 24px;
  transition: all 0.3s ease;
  overflow: visible; /* For status dot */

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 10px 20px -5px rgba(0, 0, 0, 0.1);
    border-color: #cbd5e1;
  }

  ::v-deep .el-card__body {
    padding: 24px;
  }
}

.card-status {
  position: absolute;
  top: 24px;
  left: 24px;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  
  &.active {
    background-color: #10b981;
    box-shadow: 0 0 0 4px rgba(16, 185, 129, 0.1);
  }
  
  &.inactive {
    background-color: #94a3b8;
  }
}

.card-header-actions {
  position: absolute;
  top: 20px;
  right: 20px;
  cursor: pointer;
  color: #94a3b8;
  
  &:hover {
    color: #3b82f6;
  }
}

.source-icon {
  width: 48px;
  height: 48px;
  margin: 0 auto 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  
  img {
    width: 100%;
    height: 100%;
    object-fit: contain;
  }
}

.source-info {
  text-align: center;
  margin-bottom: 20px;
  
  .source-name {
    font-size: 16px;
    font-weight: 600;
    color: #1e293b;
    margin: 0 0 8px 0;
  }
  
  .source-meta {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    
    .source-host {
      font-size: 12px;
      color: #64748b;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      max-width: 140px;
      display: inline-block;
      vertical-align: middle;
    }
  }
}

.source-stats {
  display: flex;
  justify-content: space-between;
  padding-top: 16px;
  border-top: 1px solid #f1f5f9;
  
  .stat-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    flex: 1;
    
    .label {
      font-size: 12px;
      color: #94a3b8;
      margin-bottom: 4px;
    }
    
    .value {
      font-size: 14px;
      font-weight: 600;
      color: #334155;
      
      &.success {
        color: #10b981;
      }
    }
  }
}
</style>
