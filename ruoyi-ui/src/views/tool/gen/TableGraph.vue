<template>
  <el-dialog title="数据表关系图谱" :visible.sync="visible" width="90%" top="5vh" append-to-body>
    <div v-loading="loading" class="graph-container" style="height: 80vh;">
      <div id="tableGraph" style="width: 100%; height: 100%;"></div>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts';
import { getGenGraph } from "@/api/tool/gen";

export default {
  name: "TableGraph",
  data() {
    return {
      visible: false,
      loading: false,
      chart: null,
      graphData: {
        nodes: [],
        links: []
      }
    };
  },
  methods: {
    show() {
      this.visible = true;
      this.loading = true;
      this.$nextTick(() => {
        this.initChart();
        this.getData();
      });
    },
    getData() {
      getGenGraph().then(response => {
        this.graphData = response.data;
        this.loading = false;
        this.updateChart();
      });
    },
    initChart() {
      if (this.chart) {
        this.chart.dispose();
      }
      this.chart = echarts.init(document.getElementById('tableGraph'));
      window.addEventListener("resize", this.resizeChart);
    },
    resizeChart() {
      this.chart && this.chart.resize();
    },
    updateChart() {
      if (!this.chart) return;
      
      const option = {
        title: {
          text: '数据表依赖关系',
          top: 'bottom',
          left: 'right'
        },
        tooltip: {
            formatter: function (params) {
                if (params.dataType === 'edge') {
                    return params.data.source + ' -> ' + params.data.target + ' : ' + params.data.name;
                }
                return params.data.name + '<br/>' + (params.data.value || '');
            }
        },
        legend: [{
          // selectedMode: 'single',
          data: this.graphData.categories ? this.graphData.categories.map(function (a) {
            return a.name;
          }) : []
        }],
        animationDuration: 1500,
        animationEasingUpdate: 'quinticInOut',
        series: [
          {
            name: 'Table Structure',
            type: 'graph',
            layout: 'force',
            data: this.graphData.nodes.map(function (node) {
                return {
                    id: node.id,
                    name: node.name,
                    symbolSize: 50,
                    value: node.value,
                    category: node.category,
                    label: {
                        show: true
                    }
                };
            }),
            links: this.graphData.links,
            categories: [{name: 'Table'}],
            roam: true,
            label: {
              position: 'right',
              formatter: '{b}'
            },
            lineStyle: {
              color: 'source',
              curveness: 0.3
            },
            force: {
                repulsion: 1000,
                edgeLength: 200
            },
            emphasis: {
              focus: 'adjacency',
              lineStyle: {
                width: 10
              }
            }
          }
        ]
      };
      this.chart.setOption(option);
    }
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.resizeChart);
    if (this.chart) {
      this.chart.dispose();
    }
  }
};
</script>

<style scoped>
.graph-container {
  border: 1px solid #e6e6e6;
  border-radius: 4px;
}
</style>
