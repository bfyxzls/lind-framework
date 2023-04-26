Vue.component('AlertComponent', {
    props: {
        // 提示信息
        msg: {
            type: String,
            default: ''
        },
        // 是否显示提示框
        isShow: {
            type: Boolean,
            default: false
        },
        // 插件类型：alert/confirm
        type: {
            type: String,
            default: 'alert'
        },
        // confirm插件接收的确认事件
        success: {
            type: Function,
            default: () => {
                console.log('点击了success');
            }
        },
        // confirm插件接收的取消事件
        cancel: {
            type: Function,
            default: () => {
                console.log('点击了cancel');
            }
        }
    },
    methods: {
        // 关闭弹窗
        close() {
            this.isShow = false
        },
        // alert 插件点击阴影区域关闭弹窗
        closeMask() {
            if(this.type === 'alert') {
                this.close();
            }
        },
        // confirm 插件点击取消触发的事件
        cancelEvent() {
            this.cancel();
            this.close();
        },
        // confirm 插件点击确定触发的事件
        successEvent() {
            this.success();
            this.close();
        }
    },
    updated(){
        var _this = this;
        if(_this.type == 'msg'){
            setTimeout(function(){_this.close()},1500);
        }
    },
    template: `
           <style lang="stylus" rel="stylesheet/stylus">
      .alert {
        width: 500px;
        height: auto;
        position: fixed;
        left: 50%;
        margin-left: -250px;
        top: 50%;
        margin-top: -75px;
        background-color: #fff;
        border-radius: 15px;
        box-shadow: 0 5px 8px rgba(0, 0, 0, .05);
        z-index: 3000;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
      }
    
      .msg{
        background-color: rgba(0, 0, 0, 0);
        box-shadow:none;
      }
      .msg .flex{
          padding: 20px 40px!important;
          background-color: #fff;
          border-radius :10px;
          box-shadow: 10px 10px 18px rgba(0, 0, 0, .2);
        }
    
      .flex {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 40px 30px;
        word-break: break-all;
        line-height:40px;
      }
      .space-around {
        display: flex;
        justify-content: space-around;
        align-items: center;
        width: 100%;
        border-top:1px solid #cfcfcf;
      }
    
      .btnCommon {
        width: 250px;
        height: 92px;
        line-height: 92px;
        text-align: center;
        border-radius: 6px;
      &.success {
         background-color: $btnMain;
         color: #EC736F;
         cursor: pointer;
      &:hover {
         background-color: $btnDark;
       }
      }
      &.cancel {
          width: 249px;
          color: #333;
          cursor: pointer;
          border-right: 1px solid #cfcfcf;
        }
      }
    
      .mask {
        position: fixed;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, .4);
        left: 0;
        top: 0;
        overflow: hidden;
        z-index: 2000;
      }
      .msg .mask{
        display: none;
      }
    </style>
    <template>
  <!-- 初始状态下隐藏提示框 -->
  <div v-show="isShow">
    <div class="alert" :class="type">
      <div class="flex" >{{msg}}</div>
      <!-- alert插件只显示确定按钮 -->
      <div class="space-around" v-if="type === 'alert'">
        <div class="btnCommon success" @click="close">确定</div>
      </div>
      <!-- confirm插件显示取消和确定按钮 -->
      <div class="space-around" v-else-if="type === 'confirm'">
        <div class="btnCommon cancel" @click="cancelEvent">取消</div>
        <div class="btnCommon success" @click="successEvent">确定</div>
      </div>

    </div>
    <!-- 背景遮罩 -->
    <div class="mask" @click="closeMask" v-if="type !== 'msg'"></div>
  </div>
</template>
        `
});
const Alert = {}

// Vue暴露了一个install方法，用于自定义插件
Alert.install = function (Vue) {
    // 创建一个子类
    const AlertConstructor = Vue.extend();
    // 实例化这个子类
    const instance = new AlertConstructor();
    // 创建一个div元素，并把实例挂载到div元素上
    instance.$mount(document.createElement('div'));
    // 将el插入到body元素中
    document.body.appendChild(instance.$el);

    // 添加实例方法
    // msg插件的实例方法：只接收提示信息msg
    Vue.prototype.$msg = msg => {
        instance.type = 'msg';
        instance.msg = msg;
        instance.isShow = true;
    };
    // alert插件的实例方法：只接收提示信息msg
    Vue.prototype.$alert = msg => {
        instance.type = 'alert';
        instance.msg = msg;
        instance.isShow = true;
    };
    // confirm插件的实例方法，可以接收三个参数
    // msg：提示信息
    // success：点击确定执行的函数
    // cancel：点击取消执行的函数
    Vue.prototype.$confirm = (msg, success, cancel) => {
        instance.type = 'confirm';
        instance.msg = msg;
        instance.isShow = true;
        if(typeof success !== 'undefined') {
            instance.success = success;
        }
        if(typeof cancel !== 'undefined') {
            instance.cancel = cancel;
        }
    }
}

export default Alert;
Vue.use(Alert)  // 注意，Vue.use方法必须在new Vue之前被调用
