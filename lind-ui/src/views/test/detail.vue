<!-- 根据实体字段渲染表单 -->
<template>
  <div class="app-container">
    <el-form ref="detailForm" :model="form" :rules="rules">
      <template v-for="item in columns">

        <el-form-item :label="item.columnComment" :prop="item.columnName" label-width="200px">

          <el-select v-if="item.htmlType=='select' && item.dictType!='' && item.dictType!=null"
                     v-model="form[item.columnName]" clearable multiple placeholder="请选择">
            <!-- 其中v-model它绑定了form对象里的columnName这个属性，form的赋值在create()方法中完成 -->
            <el-option
              v-for="dict in getDict(item.dictType)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            >
            </el-option>
          </el-select>

          <el-radio-group v-else-if="item.htmlType=='radio' && item.dictType!='' && item.dictType!=null"
                          v-model="form[item.columnName]">
            <el-radio
              v-for="dict in getDict(item.dictType)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            >
            </el-radio>
          </el-radio-group>

          <el-checkbox-group v-else-if="item.htmlType=='checkbox' && item.dictType!='' && item.dictType!=null"
                             v-model="form[item.columnName]" size="medium">
            <el-checkbox v-for="dict in getDict(item.dictType)"
                         :label="dict.value">{{ dict.label }}
            </el-checkbox>
          </el-checkbox-group>

          <el-input v-else v-model="form[item.columnName]" :placeholder="'请输入'+item.columnComment"></el-input>
        </el-form-item>

      </template>
      <el-row>
        <el-form-item>
          <el-button icon="el-icon-search" size="mini" type="primary" @click="submitForm">提交</el-button>
        </el-form-item>
      </el-row>
    </el-form>
  </div>
</template>
<script>
import request from "@/utils/request";

export default {
  name: "testDetail",
  dicts: ['sys_common_status', 'sys_oper_type', 'sys_notice_type'],

  created() {
    const tableName = this.$route.query.tableName;
    this.tableName = tableName;
    this.getColumns(tableName);
  },
  data() {
    return {
      columns: [],
      rules: {},
      dic: [],
      dictoptions: [],
      tableName: null,
      form: {},
    };
  },

  methods: {
    getDict(type) {
      let a = eval("this.dict.type." + type);
      return a
    },
    getField(type) {
      let a = eval("this.form." + type);
      return a
    },
    getColumns(tableName) {//获取表中的所有列信息，渲染表单
      request({
        url: '/tool/gen/1',
        method: 'get'
      }).then(response => {
        this.columns = response.data.tables.filter((o) => o.tableName == this.tableName)[0].columns;
        for (const o of this.columns) {

          if (o.htmlType == "checkbox" || o.htmlType == "select") {
            this.$set(this.form, o.columnName, []);
          }
          if (o.isRequired)
            this.$set(this.rules, o.columnName, [{required: true, message: "不能为空", trigger: "blur"},]);
        }

      });
    },
    submitForm() {
      this.$refs["detailForm"].validate(valid => {
        if (valid) {
          this.$alert("提交表单");
          console.log(this.form);
        }
      });
    }
  }
}
</script>
