<template>
  <div class="dashboard-container">
    <!-- Welcome Section -->
    <div class="welcome-section">
      <div class="welcome-content">
        <h1 class="welcome-title">欢迎回来，{{ nickName || '管理员' }}</h1>
        <p class="welcome-subtitle">今天是 {{ currentDate }}，准备好开始一天的工作了吗？</p>
      </div>
      <div class="welcome-actions">
        <el-button type="primary" icon="el-icon-plus" @click="handleAction('new')">新建项目</el-button>
        <el-button icon="el-icon-document" @click="handleAction('docs')">查看文档</el-button>
      </div>
    </div>

    <!-- Quick Stats (Placeholder) -->
    <el-row :gutter="24" class="stats-container">
      <el-col :xs="24" :sm="12" :md="6" v-for="(stat, index) in stats" :key="index">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" :class="stat.color">
            <i :class="stat.icon"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stat.value }}</div>
            <div class="stat-label">{{ stat.label }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Main Content Grid -->
    <el-row :gutter="24">
      <el-col :xs="24" :lg="16">
        <el-card class="main-card" shadow="hover">
          <div slot="header" class="card-header">
            <span>关于若依框架</span>
            <el-tag size="small" effect="plain">v{{ version }}</el-tag>
          </div>
          <div class="project-intro">
            <p>
              若依是一个基于 Spring Boot 和 Vue 的后台管理系统。它采用了现代化的前后端分离架构，
              为您提供了一个稳定、高效、可扩展的开发平台。
            </p>
            <div class="feature-list">
              <div class="feature-item">
                <i class="el-icon-check feature-icon"></i>
                <span>完全响应式布局</span>
              </div>
              <div class="feature-item">
                <i class="el-icon-check feature-icon"></i>
                <span>灵活的权限管理</span>
              </div>
              <div class="feature-item">
                <i class="el-icon-check feature-icon"></i>
                <span>多终端支持</span>
              </div>
              <div class="feature-item">
                <i class="el-icon-check feature-icon"></i>
                <span>代码生成器</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="8">
        <el-card class="tech-card" shadow="hover">
          <div slot="header" class="card-header">
            <span>技术栈</span>
          </div>
          <div class="tech-list">
            <div class="tech-category">
              <h4>后端</h4>
              <div class="tech-tags">
                <el-tag size="small">Spring Boot</el-tag>
                <el-tag size="small" type="success">MyBatis</el-tag>
                <el-tag size="small" type="warning">Redis</el-tag>
              </div>
            </div>
            <div class="tech-category">
              <h4>前端</h4>
              <div class="tech-tags">
                <el-tag size="small">Vue</el-tag>
                <el-tag size="small" type="info">Element UI</el-tag>
                <el-tag size="small" type="danger">Axios</el-tag>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: "Index",
  data() {
    return {
      version: "3.9.1",
      currentDate: new Date().toLocaleDateString('zh-CN', { month: 'long', day: 'numeric', weekday: 'long' }),
      stats: [
        { label: '用户总数', value: '1,203', icon: 'el-icon-user', color: 'blue' },
        { label: '系统消息', value: '42', icon: 'el-icon-message', color: 'green' },
        { label: '今日访问', value: '3,105', icon: 'el-icon-view', color: 'orange' },
        { label: '待办事项', value: '8', icon: 'el-icon-finished', color: 'purple' },
      ]
    }
  },
  computed: {
    ...mapGetters([
      'nickName'
    ])
  },
  methods: {
    handleAction(type) {
      this.$message.info('功能开发中...')
    }
  }
}
</script>

<style scoped lang="scss">
.dashboard-container {
  padding: 32px;
  background-color: #f8fafc;
  min-height: 100vh;
}

.welcome-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
  
  .welcome-title {
    font-size: 24px;
    font-weight: 700;
    color: #1e293b;
    margin: 0 0 8px 0;
  }
  
  .welcome-subtitle {
    color: #64748b;
    margin: 0;
    font-size: 14px;
  }
}

.stats-container {
  margin-bottom: 24px;
}

.stat-card {
  margin-bottom: 20px;
  border: none;
  
  ::v-deep .el-card__body {
    display: flex;
    align-items: center;
    padding: 20px;
  }
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-right: 16px;
  
  &.blue { background-color: rgba(59, 130, 246, 0.1); color: #3b82f6; }
  &.green { background-color: rgba(16, 185, 129, 0.1); color: #10b981; }
  &.orange { background-color: rgba(245, 158, 11, 0.1); color: #f59e0b; }
  &.purple { background-color: rgba(139, 92, 246, 0.1); color: #8b5cf6; }
}

.stat-info {
  .stat-value {
    font-size: 24px;
    font-weight: 700;
    color: #1e293b;
    line-height: 1.2;
  }
  
  .stat-label {
    font-size: 13px;
    color: #64748b;
    margin-top: 4px;
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  span {
    font-size: 16px;
    font-weight: 600;
    color: #1e293b;
  }
}

.project-intro {
  color: #475569;
  line-height: 1.6;
  
  .feature-list {
    margin-top: 24px;
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
  
  .feature-item {
    display: flex;
    align-items: center;
    
    .feature-icon {
      color: #10b981;
      margin-right: 8px;
      font-weight: bold;
    }
  }
}

.tech-list {
  .tech-category {
    margin-bottom: 20px;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    h4 {
      margin: 0 0 12px 0;
      font-size: 14px;
      color: #64748b;
    }
    
    .tech-tags {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
    }
  }
}
</style>
