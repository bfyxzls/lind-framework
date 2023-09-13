<template>
  <div class="app-container">
    <el-form ref="queryForm" :inline="true" :model="queryParams" label-width="68px"
             size="small">
      <el-row>
        <el-col :span="5">
          <el-form-item label="姓名" prop="name">
            <el-input
              v-model="queryParams.name"
              clearable
              placeholder="请输入姓名"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="邮件" prop="email">
            <el-input
              v-model="queryParams.email"
              clearable
              placeholder="请输入邮件"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="身份份号" prop="certNumber">
            <el-input
              v-model="queryParams.certNumber"
              clearable
              placeholder="请输入身份份号"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="9">
          <el-form-item label="联系电话" prop="phone">
            <el-input
              v-model="queryParams.phone"
              clearable
              placeholder="请输入联系电话"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="10">
          <el-form-item label="auto-complete-list-demo组件下拉选择" label-width="200px" prop="phone">
            <auto-complete-list-demo ref="subData" :all-infos="dataList"/>
          </el-form-item>
        </el-col>
        <el-col :span="14">
          <el-form-item label="更新日期" prop="phone">
            <el-date-picker v-model="queryParams.updateTime"
                            :picker-options="pickerOptions"
                            align="right"
                            clearable
                            end-placeholder="结束日期"
                            format="yyyy-MM-dd"
                            range-separator="至"
                            start-placeholder="开始日期"
                            type="daterange"
                            value-format="yyyy-MM-dd">

            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row justify="end" type="flex">
        <el-form-item>
          <el-button icon="el-icon-search" size="mini" type="primary" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-row>
    </el-form>
    <el-select v-model="currentOption" clearable placeholder="请输入城市编号">
      <el-option
        v-for="item in optionData"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      ></el-option>
    </el-select>

    <div>
      <el-button @click="increment">+</el-button>
      vuex状态变更：
      <el-input v-model="count1" placeholder="请输入姓名"></el-input>
    </div>
  </div>

</template>

<script>

import AutoCompleteListDemo from "@/components/AutoCompleteListDemo/index.vue";

export default {
  name: "test",
  components: {AutoCompleteListDemo},
  data() {
    return {
      pickerOptions: {
        // 使用 disabledDate 方法来限制选择的日期不能大于当前日期
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },
      dataList: [{value: "1", label: "北京"},
        {value: "2", label: "上海"},
        {value: "3", label: "广州"},
        {value: "4", label: "深圳"}],
      currentOption: "1",
      optionData: [
        {value: "1", label: "北京"},
        {value: "2", label: "上海"},
        {value: "3", label: "广州"},
        {value: "4", label: "深圳"}],
      queryParams: {
        name: "",
        email: "",
        certNumber: "",
        phone: "",
        updateTime: []
      },
      msgData: "Welcome to Your Vue.js App",

    }
  },
  computed: {
    count1() {
      return this.$store.state.count;
    }
  },
  methods: {
    handleQuery() {
      this.$alert("检索");
      this.$refs.subData.subValue = 3; //修改子组件中的data下面subData属性的值
    },
    resetQuery() {
      this.$refs.queryForm.resetFields();
      this.$emit("query", this.queryParams);
    },
    increment() {
      this.$cache.local.set("author", "lind"); // 本地存储
      this.$cache.session.set("huihua", "会话"); // 会话存储
      this.$store.dispatch("changeSetting", {
        key: 'count',
        value: 10
      });
      console.log(this.$store.state.count);
    },

  },

}
</script>
