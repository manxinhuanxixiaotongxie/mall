<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table
        :data="dataList"
        border
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandle"
        style="width: 100%;"
    >
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <el-table-column prop="id" header-align="center" align="center" label="id"></el-table-column>
      <el-table-column prop="couponId" header-align="center" align="center" label="优惠券id"></el-table-column>
      <el-table-column prop="memberId" header-align="center" align="center" label="会员id"></el-table-column>
      <el-table-column prop="memberNickName" header-align="center" align="center" label="会员名字"></el-table-column>
      <el-table-column prop="getType" header-align="center" align="center" label="获取方式">
        <template slot-scope="scope">
          <el-tag type="primary" v-if="scope.row.getType==0">后台赠送</el-tag>
          <el-tag type="success" v-else>主动领取</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" header-align="center" align="center" label="创建时间"></el-table-column>
      <el-table-column
          prop="useType"
          header-align="center"
          align="center"
          label="使用状态"
      >
        <template slot-scope="scope">
          <el-tag type="primary" v-if="scope.row.useType==0">未使用</el-tag>
          <el-tag type="success" v-if="scope.row.useType==1">已使用</el-tag>
          <el-tag type="warning" v-if="scope.row.useType==2">已过期</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="useTime" header-align="center" align="center" label="使用时间"></el-table-column>
      <el-table-column prop="orderId" header-align="center" align="center" label="订单id"></el-table-column>
      <el-table-column prop="orderSn" header-align="center" align="center" label="订单号"></el-table-column>
    </el-table>
    <el-pagination
        @size-change="sizeChangeHandle"
        @current-change="currentChangeHandle"
        :current-page="pageIndex"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        :total="totalPage"
        layout="total, sizes, prev, pager, next, jumper"
    ></el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
  </div>
</template>

<script>
export default {
  data() {
    return {
      dataForm: {
        key: ""
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false
    };
  },
  components: {},
  activated() {
    this.getDataList();
  },
  methods: {
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true;
      this.$http({
        url: this.$http.adornUrl("/coupon/couponhistory/list"),
        method: "get",
        params: this.$http.adornParams({
          page: this.pageIndex,
          limit: this.pageSize,
          key: this.dataForm.key
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.dataList = data.page.list;
          this.totalPage = data.page.totalCount;
        } else {
          this.dataList = [];
          this.totalPage = 0;
        }
        this.dataListLoading = false;
      });
    },
    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getDataList();
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.getDataList();
    },
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val;
    },
    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true;
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id);
      });
    },
    // 删除
    deleteHandle(id) {
      var ids = id
          ? [id]
          : this.dataListSelections.map(item => {
            return item.id;
          });
      this.$confirm(
          `确定对[id=${ids.join(",")}]进行[${id ? "删除" : "批量删除"}]操作?`,
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }
      ).then(() => {
        this.$http({
          url: this.$http.adornUrl("/coupon/couponhistory/delete"),
          method: "post",
          data: this.$http.adornData(ids, false)
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message({
              message: "操作成功",
              type: "success",
              duration: 1500,
              onClose: () => {
                this.getDataList();
              }
            });
          } else {
            this.$message.error(data.msg);
          }
        });
      });
    }
  }
};
</script>
