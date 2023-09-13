<!-- 文本框自动补完功能 -->
<template>
  <div>
    <el-autocomplete
      v-model="dataValue"
      :fetch-suggestions="fetchSuggestions"
      @select="handleSelect"
    >
      <template slot-scope="{ item }">{{ item.label }}</template>
    </el-autocomplete>
  </div>
</template>

<script>
export default {
  name: 'AutoCompleteListDemo',
  //allInfos是父组件传来的值，如果allInfos不是父组件传来的就不用这样写
  props: ["allInfos"],
  data() {
    return {dataValue: null}
  },
  methods: {
    fetchSuggestions(queryString, cb) {
      let results = this.allInfos;
      results = queryString
        ? results.filter(this.createFilter(queryString))
        : results;
      cb(results);
    },
    createFilter(queryString) {
      return (item) => {
        return item.value.toUpperCase().match(queryString.toUpperCase());
      };
    },
    handleSelect(item) {
      this.dataValue = item.label; // 回调在文框中显示的值
      this.$emit('input', item.value);// 绑定到父组件的值
    },
  }
};
</script>
