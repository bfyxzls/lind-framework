<template>
  <div class="app-container">
    <el-card>
      <el-tabs v-model="activeName">
        <el-tab-pane label="列表检索" name="list">
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
              <el-col :span="5">
                <el-form-item label="热加载" prop="subData">
                  <auto-complete-list-demo ref="subComplete" :all-infos="dataList"/>
                </el-form-item>
              </el-col>
              <el-col :span="19">
                <el-form-item label="更新日期" prop="updateTime">
                  <el-date-picker v-model="queryParams.updateTime"
                                  :picker-options="pickerOptions"
                                  align="right"
                                  clearable
                                  end-placeholder="结束日期"
                                  format="yyyy-MM-dd"
                                  range-separator="至"
                                  start-placeholder="开始日期"
                                  type="daterange"
                                  value-format="yyyy-MM-dd"
                                  width="200px">

                  </el-date-picker>
                </el-form-item>
              </el-col>

            </el-row>
            <el-row>
              <el-col :span="5">
                <el-form-item label="城市" prop="optionData">
                  <el-select v-model="currentOption" clearable placeholder="请输入城市编号">
                    <el-option
                      v-for="item in optionData"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="10">

                <el-form-item label="vuex状态变更" label-width="200px">
                  <el-row>
                    <el-col :span="10">
                      <el-button @click="increment">+</el-button>
                    </el-col>
                    <el-col :span="14">
                      <el-input v-model="count1" placeholder="请输入姓名"></el-input>
                    </el-col>
                  </el-row>
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-select v-model="selectedOption">
                  <el-option
                    v-for="option in options"
                    :key="option.id"
                    :label="option.name"
                    :value="option.id"
                  ></el-option>
                </el-select>
              </el-col>

            </el-row>
            <el-row justify="end" type="flex">
              <el-form-item>
                <el-button icon="el-icon-search" size="mini" type="primary" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
              </el-form-item>
            </el-row>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="登录窗口" name="login">
          <div class="login">

            <el-form ref="loginForm" :model="loginForm" class="login-form">
              <h3 class="title">Lind管理系统</h3>
              <el-form-item prop="username">
                <el-input
                  v-model="loginForm.username"
                  auto-complete="off"
                  placeholder="账号"
                  type="text"
                >
                  <svg-icon slot="prefix" class="el-input__icon input-icon" icon-class="user"/>
                </el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input
                  v-model="loginForm.password"
                  auto-complete="off"
                  placeholder="密码"
                  type="password"
                  @keyup.enter.native="handleLogin"
                >
                  <svg-icon slot="prefix" class="el-input__icon input-icon" icon-class="password"/>
                </el-input>
              </el-form-item>
              <el-form-item prop="code">
                <el-input
                  v-model="loginForm.code"
                  auto-complete="off"
                  placeholder="验证码"
                  style="width: 63%"
                  @keyup.enter.native="handleLogin"
                >
                  <svg-icon slot="prefix" class="el-input__icon input-icon" icon-class="validCode"/>
                </el-input>
                <div class="login-code">
                  <img :src="codeUrl" class="login-code-img" @click="getCode"/>
                </div>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>
        <el-tab-pane label="各库表单" name="details">

          <template v-for="item in columns">
            <p>
              <el-button type="success" @click="getUrl(item.tableName)">{{ item.tableName }}</el-button>
            </p>
          </template>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 外部内容显示 -->
    <el-dialog
      :visible.sync="dialogVisible"
      height="100%"
      title="详细信息"
      width="80%"
    >
      <iframe :src="frameSrc" frameborder="0" height="600px" width="100%"></iframe>
    </el-dialog>
  </div>

</template>

<script>

import AutoCompleteListDemo from "@/components/AutoCompleteListDemo/index.vue";
import {getCodeImg} from "@/api/login";
import request from "@/utils/request";


export default {
  name: "test",
  components: {AutoCompleteListDemo},
  data() {
    return {
      frameSrc: null,
      dialogVisible: false,
      columns: [], //存储某个表的字段
      activeName: "list",
      selectedOption: null, // 选中的选项的值
      options: [
        {id: 1, name: '选1'},
        {id: 2, name: '选2'},
        {id: 3, name: '选3'},
      ],
      dic: {},//字典
      currentDic: null,
      codeUrl: "",
      rules: {},
      loginForm: {
        username: "admin",
        password: "admin123",
        rememberMe: false,
        code: "",
        uuid: ""
      },
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
      return this.$store.state.test.count;
    }
  },
  created() {
    this.getCode();
    this.getTables();
  },
  methods: {
    getUrl(table) {
      this.dialogVisible = true;
      this.frameSrc = "/detail?tableName=" + table;
    },
    getTables() {//获取表中的所有列信息，渲染表单
      request({
        url: '/tool/gen/list',
        method: 'get'
      }).then(response => {
        this.columns = response.rows;
      });
    },
    submitForm() {
      this.$refs["detailForm"].validate(valid => {
        if (valid) {
          this.$alert("提交表单：" + this.dic);
        }
      });
    },
    getCode() {
      getCodeImg().then(res => {
        this.codeUrl = "data:image/gif;base64," + res.img;
        this.loginForm.uuid = res.uuid;
      });
    },
    handleQuery() {
      this.$alert("检索：" + this.selectedOption);
      this.$refs.subComplete.subValue = 3; //修改子组件中的data下面subValue属性的值,其中subComplete是在父组件中为ref设置的值
    },
    resetQuery() {
      this.$refs.queryForm.resetFields();
      this.$emit("query", this.queryParams);
    },
    increment() {
      this.$cache.local.set("author", "lind"); // 本地存储
      this.$cache.session.set("huihua", "会话"); // 会话存储
      var count = this.$store.getters.testCount;
      count += 1;
      this.$store.dispatch("test/changeSetting", {// changeSetting是vuex中的action
        key: 'count',
        value: count
      });
    },

  },

}

// Promise
function promise() {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      resolve('done');
    }, 1000);
  });
}


// async/await
async function asyncFunc() {
  const result = await promise();
  console.log(result);
}
</script>
<style lang="scss" rel="stylesheet/scss">
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;

  .el-input {
    height: 38px;

    input {
      height: 38px;
    }
  }

  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}

.login-code {
  width: 33%;
  height: 38px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }
}


.login-code-img {
  height: 38px;
}
</style>
