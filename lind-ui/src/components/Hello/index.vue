<!-- 定义一个组件，有3个输入属性，value,options,msg-->
<template>
  <div>
    <h1>{{ msg }}</h1>

    <template v-for="(item, index) in options">
      <template v-if="values.includes(item.value)">
        <el-tag
          :key="item.value"
          :index="index"
        >
          {{ item.label + " " }}
        </el-tag>
      </template>
    </template>
  </div>
</template>
<script>
export default {
  name: "HelloWorld",
  props: {
    options: {
      type: Array,
      default: null,
    },
    msg: {type: String, default: 'hello world'},
    value: [Number, String, Array]
  },
  data() {
    return {};
  },
  computed: {
    values() {
      if (this.value !== null && typeof this.value !== "undefined") {
        return Array.isArray(this.value) ? this.value : [String(this.value)];
      } else {
        return [];
      }
    }
  }
};
</script>

