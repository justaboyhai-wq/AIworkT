<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="配置名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入配置名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="提供商" prop="provider">
        <el-select v-model="queryParams.provider" placeholder="请选择提供商" clearable>
          <el-option label="OpenAI" value="OpenAI" />
          <el-option label="DeepSeek" value="DeepSeek" />
          <el-option label="AliQwen (通义千问)" value="AliQwen" />
          <el-option label="Google Gemini" value="Gemini" />
          <el-option label="Custom (OpenAI Compatible)" value="Custom" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['tool:llm:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['tool:llm:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['tool:llm:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="configList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="配置名称" align="center" prop="name" />
      <el-table-column label="提供商" align="center" prop="provider">
        <template slot-scope="scope">
            <el-tag>{{ scope.row.provider }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="模型编码" align="center" prop="modelCode" />
      <el-table-column label="Base URL" align="center" prop="baseUrl" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
           <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
               {{ scope.row.status === '0' ? '正常' : '停用' }}
           </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-connection"
            @click="handleTest(scope.row)"
            v-hasPermi="['tool:llm:query']"
          >测试</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['tool:llm:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['tool:llm:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- Add or Edit Dialog -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="配置名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入配置名称 (如: DeepSeek-V3)" />
        </el-form-item>
        <el-form-item label="提供商" prop="provider">
          <el-select v-model="form.provider" placeholder="请选择提供商" @change="handleProviderChange">
            <el-option label="OpenAI" value="OpenAI" />
            <el-option label="DeepSeek" value="DeepSeek" />
            <el-option label="AliQwen (通义千问)" value="AliQwen" />
            <el-option label="Google Gemini" value="Gemini" />
            <el-option label="Custom (OpenAI Compatible)" value="Custom" />
          </el-select>
        </el-form-item>
        <el-form-item label="模型编码" prop="modelCode">
          <el-input v-model="form.modelCode" placeholder="请输入模型编码 (如: gpt-4, deepseek-chat)" />
          <div class="tip-text" v-if="form.provider === 'DeepSeek'">推荐: deepseek-chat, deepseek-reasoner</div>
          <div class="tip-text" v-if="form.provider === 'AliQwen'">推荐: qwen-max, qwen-plus</div>
        </el-form-item>
        <el-form-item label="API Key" prop="apiKey">
          <el-input v-model="form.apiKey" placeholder="请输入 API Key" show-password />
        </el-form-item>
        <el-form-item label="Base URL" prop="baseUrl">
          <el-input v-model="form.baseUrl" placeholder="请输入 Base URL (如: https://api.openai.com/v1)" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="warning" plain icon="el-icon-connection" @click="handleTestForm" :loading="testing">测试连接</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listLlmConfig, getLlmConfig, delLlmConfig, addLlmConfig, updateLlmConfig, testLlmConnection } from "@/api/tool/llm";

export default {
  name: "LlmConfig",
  data() {
    return {
      // Loading
      loading: true,
      testing: false,
      // Selected IDs
      ids: [],
      // Non-single disable
      single: true,
      // Non-multiple disable
      multiple: true,
      // Show Search
      showSearch: true,
      // Total count
      total: 0,
      // Table Data
      configList: [],
      // Popup Title
      title: "",
      // Show Popup
      open: false,
      // Query Params
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        provider: undefined,
        status: undefined
      },
      // Form Params
      form: {},
      // Validation Rules
      rules: {
        name: [
          { required: true, message: "配置名称不能为空", trigger: "blur" }
        ],
        provider: [
          { required: true, message: "提供商不能为空", trigger: "change" }
        ],
        modelCode: [
          { required: true, message: "模型编码不能为空", trigger: "blur" }
        ],
        apiKey: [
          { required: true, message: "API Key不能为空", trigger: "blur" }
        ],
        baseUrl: [
          { required: true, message: "Base URL不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** Query List */
    getList() {
      this.loading = true;
      listLlmConfig(this.queryParams).then(response => {
        this.configList = response.rows;
        this.total = response.total;
        this.loading = false;
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
        provider: "OpenAI",
        modelCode: undefined,
        apiKey: undefined,
        baseUrl: "https://api.openai.com/v1",
        status: "0",
        remark: undefined
      };
      this.resetForm("form");
    },
    /** Search Button */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** Reset Button */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** Selection Change */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** Add Button */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加大模型配置";
    },
    /** Edit Button */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getLlmConfig(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改大模型配置";
      });
    },
    /** Submit Button */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateLlmConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLlmConfig(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** Delete Button */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除编号为"' + ids + '"的数据项？').then(function() {
        return delLlmConfig(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** Test Connection */
    handleTest(row) {
        const loading = this.$loading({
            lock: true,
            text: '正在测试连接...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
        });
        testLlmConnection(row).then(response => {
            loading.close();
            this.$modal.msgSuccess("连接成功！");
        }).catch(() => {
            loading.close();
        });
    },
    handleTestForm() {
        this.$refs["form"].validate(valid => {
            if (valid) {
                this.testing = true;
                testLlmConnection(this.form).then(response => {
                    this.testing = false;
                    this.$modal.msgSuccess("连接成功！");
                }).catch(() => {
                    this.testing = false;
                });
            }
        });
    },
    handleProviderChange(val) {
        if (val === 'DeepSeek') {
            this.form.baseUrl = 'https://api.deepseek.com';
            this.form.modelCode = 'deepseek-chat';
        } else if (val === 'AliQwen') {
            this.form.baseUrl = 'https://dashscope.aliyuncs.com/compatible-mode/v1';
            this.form.modelCode = 'qwen-plus';
        } else if (val === 'OpenAI') {
            this.form.baseUrl = 'https://api.openai.com/v1';
            this.form.modelCode = 'gpt-4o';
        } else if (val === 'Gemini') {
            this.form.baseUrl = 'https://generativelanguage.googleapis.com/v1beta/openai/';
            this.form.modelCode = 'gemini-1.5-flash';
        }
    }
  }
};
</script>

<style scoped>
.tip-text {
    font-size: 12px;
    color: #909399;
    line-height: 1.5;
    padding-top: 4px;
}
</style>
