<template>
  <div class="schema-graph-container" v-loading="loading">
    <div ref="chart" class="chart-container"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import { getDataSourceSchema } from "@/api/tool/datasource";

export default {
  name: "SchemaGraph",
  props: {
    dataSourceId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      loading: false,
      chart: null,
      graphData: {
        nodes: [],
        links: []
      }
    };
  },
  watch: {
    dataSourceId: {
      handler(val) {
        if (val) {
          this.fetchData();
        }
      },
      immediate: true
    }
  },
  mounted() {
    this.initChart();
    window.addEventListener('resize', this.resizeChart);
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose();
    }
    window.removeEventListener('resize', this.resizeChart);
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$refs.chart);
      this.chart.on('click', (params) => {
        if (params.dataType === 'node') {
          // Handle node click if needed
          console.log('Node clicked:', params.data);
        }
      });
    },
    resizeChart() {
      if (this.chart) {
        this.chart.resize();
      }
    },
    fetchData() {
      this.loading = true;
      getDataSourceSchema(this.dataSourceId).then(response => {
        const tables = response.data;
        this.processGraphData(tables);
        this.renderChart();
        this.loading = false;
      }).catch(err => {
        this.loading = false;
        this.$message.error('加载图谱失败: ' + err);
      });
    },
    processGraphData(tables) {
      const nodes = [];
      const links = [];
      
      // Create Nodes
      tables.forEach((table, index) => {
        // Format tooltip content
        let tooltipContent = `<b>${table.name}</b><br/>`;
        if (table.comment) tooltipContent += `<i>${table.comment}</i><br/>`;
        tooltipContent += '<hr/>';
        if (table.columns && table.columns.length > 0) {
            table.columns.forEach(col => {
                tooltipContent += `${col.name} (${col.type})<br/>`;
            });
        }

        nodes.push({
          id: table.name,
          name: table.name,
          symbolSize: 50,
          value: table.columns ? table.columns.length : 0, // Size by column count or fixed
          category: 0,
          tooltip: {
            formatter: tooltipContent
          },
          // Store raw data
          raw: table
        });
      });

      // 1. First priority: Real Foreign Keys
      tables.forEach(source => {
        if (source.importedKeys && source.importedKeys.length > 0) {
            source.importedKeys.forEach(fk => {
                 // Prevent self-loop if not needed, but sometimes self-loop exists.
                 // Link from current table (fktable) to referenced table (pktable)
                 // Note: 'source' in links usually means where the link starts.
                 // In ER diagrams, usually we point FROM child (FK) TO parent (PK).
                 links.push({
                    source: fk.fktable,
                    target: fk.pktable,
                    value: 'FK',
                    label: {
                        show: true,
                        formatter: `${fk.fkcolumn} → ${fk.pkcolumn}`,
                        fontSize: 10
                    },
                    lineStyle: {
                        color: '#ff4949', // Red color for strong FK
                        width: 2,
                        type: 'solid'
                    }
                 });
            });
        }
      });

      // 2. Second priority: Heuristic (Name Matching)
      // Only add if no link exists between these two nodes yet (optional, or allow multi-edges)
      tables.forEach(source => {
        if (!source.columns) return;
        source.columns.forEach(col => {
            const colName = col.name.toLowerCase().replace('_', '');
            nodes.forEach(target => {
                if (source.name === target.name) return;
                
                // Skip if a real FK link already exists
                const existingLink = links.find(l => l.source === source.name && l.target === target.name);
                if (existingLink) return;

                const targetName = target.name.toLowerCase().replace('_', '');
                // Check if column is like "target_id" or "targetid"
                if (colName === targetName + 'id') {
                    links.push({
                        source: source.name,
                        target: target.name,
                        value: 'Inferred',
                        label: {
                            show: true,
                            formatter: `${col.name} → id?`,
                            fontSize: 10,
                            color: '#999'
                        },
                        lineStyle: {
                            color: '#aaa', // Grey for inferred
                            width: 1,
                            type: 'dashed'
                        }
                    });
                }
            });
        });
      });

      this.graphData = { nodes, links };
    },
    renderChart() {
      if (!this.chart) return;

      const option = {
        title: {
          text: '数据源表结构知识图谱',
          subtext: '实线：数据库外键 | 虚线：命名推断',
          top: 'bottom',
          left: 'right'
        },
        tooltip: {
            trigger: 'item',
            triggerOn: 'mousemove'
        },
        series: [
          {
            type: 'graph',
            layout: 'force',
            data: this.graphData.nodes,
            links: this.graphData.links,
            roam: true,
            label: {
              show: true,
              position: 'right',
              formatter: '{b}'
            },
            edgeSymbol: ['none', 'arrow'], // Add arrows
            edgeSymbolSize: [4, 10],
            force: {
              repulsion: 400,
              edgeLength: [150, 250]
            },
            lineStyle: {
                color: 'source',
                curveness: 0.2
            },
            autoCurveness: true, // Allow multiple edges to curve
            emphasis: {
                focus: 'adjacency',
                lineStyle: {
                    width: 4
                }
            }
          }
        ]
      };

      this.chart.setOption(option);
    }
  }
};
</script>

<style scoped>
.schema-graph-container {
  width: 100%;
  height: 600px;
}
.chart-container {
  width: 100%;
  height: 100%;
}
</style>