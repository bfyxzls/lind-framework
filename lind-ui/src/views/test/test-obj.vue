<template>
  <div class="app-container">
    <el-button @click="startHacking">Start</el-button>
    <el-button @click="print">导出类Person</el-button>
    <el-button @click="alertTest">全局插件</el-button>
  </div>
</template>

<script>
import Person from "@/utils/Person";

export default {
  name: "test-obj",
  data() {
    return {}
  },
  methods: {
    alertTest() {
      this.$modal.confirm("不用在vue显示引用plugins/modal.js，它通过在plugins/index.js中引用，在main.js中注册，已经是全局的了");
    },
    print() {
      const person = new Person({id: "1", name: "zhangsan"});
      person.print();
    },
    startHacking() {
      this.$notify({
        title: 'It works!',
        type: 'success',
        message: 'We\'ve laid the ground work for you. It\'s time for you to build something epic!',
        duration: 5000
      })
    }
  }
}
</script>
