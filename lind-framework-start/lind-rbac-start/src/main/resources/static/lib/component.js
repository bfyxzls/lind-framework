import {getRequest} from "/lib/index.js"
// 定义组件
var todoItem = Vue.extend({
    props: {
        todoData: []
    },
    template:
        `
        <ul>
            <li v-for='(d, i) in todoData' :key="i">
                {{ d.text }}
            </li>
        </ul>
       `
});

//注册全局组件
Vue.component('todoItem', todoItem)

Vue.component('my-component', {
    template: '<div>A custom component!</div>'
})

Vue.component('box-body', {
    props: {height: String},//添加最大高度
    data: function () {
        return this.height ? {dialogStyle: {'max-height': this.height + 'px', 'overflow-y': 'auto'}} : {dialogStyle: {}}
    },
    template: `
          <div class="panel-body" :style="dialogStyle">
              <slot></slot>
          </div>
        `
});

Vue.component('range-date', {
    props: {title: String},//标题
    data: function () {
        return {
            name: this.title ? this.title : '开始时间',
            searchForm: {}
        }
    },
    methods: {
        selectDateRange(v) {
            if (v) {
                this.searchForm.fromDate = v[0];
                this.searchForm.toDate = v[1];
            }
        }
    },
    template: `
            <form-item :label="name">
                    <date-picker
                            type="daterange"
                            format="yyyy-MM-dd"
                            clearable
                            @on-change="selectDateRange"
                            placeholder="选择起始时间"
                            style="width: 200px"
                    ></date-picker>
                </form-item>
        `
});

// 左侧菜单
Vue.component('nav-menu', {
    data: function () {
        return {
            activeName: '/sys/userList',
        }
    },
    methods: {
        menuSelect: function (name) {
            this.activeName = name;
            //调用子组件breadcrumb
            if (this.$route.path != name) {
                this.$router.push(name);//按着路由path完成跳转，menu上绑定name时可以直接使用route.path，注意初需要先把路由字典初始化
            }
        }
    },
    template: `
         <i-menu theme="light" width="auto" :active-name="activeName"  @on-select='menuSelect' :open-names="['1','6']" >
              <nav-menu-item/>
         </i-menu>`
});

// 递归构建组件
Vue.component('nav-menu-item', {
    props: {
        navMenus: Array
    }, methods: {
        load: function () {
            var data;
            $.ajax({
                url: "/permission",
                async: false,//这块需要是同步请求，将同步的结果赋值常量routerConfig
                success: function (res) {
                    data = res.data;
                }
            })
            return data;
        }
    },
    data() {
        return {
            navList: this.navMenus ? this.navMenus : this.load()
        }
    }, template: `
           <div>
               <div v-for="navMenu in navList">
                    <menu-item  v-if="navMenu.sons==null" :name="navMenu.path">               
                           <Icon type="ios-navigate"/>
                          {{navMenu.title}}
                    </menu-item>
                    <Submenu v-if="navMenu.sons" :name="navMenu.id">
                        <template slot="title"> 
                             {{navMenu.title}}
                        </template>
                         <nav-menu-item :navMenus="navMenu.sons"></nav-menu-item>
                   </Submenu>
                  
               </div>
           </div>`
});
// 面包绡
Vue.component("breadcrumb", {
    data: function () {
        return {
            breadcrumbList: []
        }
    },
    watch: {
        $route: {
            handler(newName, oldName) {
                getRequest('/permission/father?path=' + newName.path).then(res => (this.breadcrumbList = res.data.data));
            },
            deep: true,
            immediate: true
        }
    },
    template: `
     <div class="layout-breadcrumb">
        <Breadcrumb :style="{margin: '24px 0'}" separator=">">
            <Breadcrumb-item v-for="(item,index) in breadcrumbList">{{item.title}}</Breadcrumb-item>
        </Breadcrumb>
     </div>
`
});

Vue.component('user-list', {
    data() {
        return {
            modalDetailVisible: false,//是否显示弹层
            modalTitle: null,//当前条目标题
            modalEditTitle: null,
            modalEditVisible: false,
            cartList: [],//列表对象
            currentRecord: {},//当前条目
            searchForm: {//检索表单
                pageNumber: 1,
                pageSize: 10,
                fromDate: "",
                toDate: "",
            },
            columns: [
                {
                    type: "selection",
                    width: 60,
                    align: "center",
                },
                {
                    title: '名称',
                    key: 'username'
                },
                {
                    title: '电话',
                    key: 'phone'
                },
                {
                    title: 'Email',
                    key: 'email'
                }, {
                    title: '建立时间',
                    key: 'createTime'
                },
                {
                    title: '操作',
                    render: (h, params) => {
                        return h('div', [
                            h('Button', {
                                props: {
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '5px'
                                },
                                on: {
                                    click: () => {
                                        this.detail(params.row);
                                    }
                                }
                            }, '查看'),
                            h('Button', {
                                props: {
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '5px'
                                },
                                on: {
                                    click: () => {
                                        this.edit(params.row);
                                    }
                                }
                            }, '编辑'),
                            h('Button', {
                                props: {
                                    type: 'error',
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '5px'
                                },
                                on: {
                                    click: () => {
                                        this.deleteItem(params.row.id);
                                    }
                                }
                            }, '删除')
                        ]);
                    }
                }
            ]
        }
    },
    methods: {
        selectDateRange(v) {

            if (v) {
                this.searchForm.fromDate = v[0];
                this.searchForm.toDate = v[1];
            }
        },
        handleSearch() {
            this.searchForm.pageNumber = 1;
            this.searchForm.pageSize = 10;
            this.getList();
        },
        getList() {
            getRequest('/user/list', this.searchForm).then(res => {
                this.cartList = res.data.data.records;
            });
        },
        edit(v) {
            this.modalEditTitle = "编辑";
            this.modalEditVisible = true;
            this.$refs.searchForm.resetFields();
            let str = JSON.stringify(v);
            this.currentRecord = JSON.parse(str);
        },
        detail(v) {
            this.modalTitle = "查看";
            for (let attr in v) {
                if (v[attr] == null) {
                    v[attr] = "";
                }
            }
            let str = JSON.stringify(v);
            let info = JSON.parse(str);
            axios.get('/user/' + info.id).then(res => (this.currentRecord = res.data.data));
            this.modalDetailVisible = true;
        },
        deleteItem: function (id) {
            for (let i = 0; i < this.cartList.length; i++) {
                if (this.cartList[i].id === id) {
                    // 询问是否删除
                    this.$Modal.confirm({
                        title: '提示',
                        content: '确定要删除吗？',
                        onOk: () => {
                            axios.del('/user/del/' + info.id).then(res => {
                                this.cartList.splice(i, 1);
                            });
                        },
                        onCancel: () => {
                            // 什么也不做
                        }
                    });
                }
            }
        },
        save:function() {
             putRequest("/user/update/"+this.currentRecord.id,$this.$data.form)
        }
    },
    mounted() {
        this.getList();
    },
    template: `<div>
                    <div class="search">
                        <i-form ref="searchForm" :model="searchForm" inline :label-width="70">
                              <form-item :label="name">
                                <date-picker
                                        type="daterange"
                                        format="yyyy-MM-dd"
                                        clearable
                                        @on-change="selectDateRange"
                                        placeholder="选择起始时间"
                                        style="width: 200px"
                                ></date-picker>
                            </form-item>
                            <form-item class="br">
                                <input type="button" @click="handleSearch" class="ivu-btn ivu-btn-default" value="搜 索"/>
                            </form-item>
                        </i-form>
                     </div>
                    <i-table id="datatable1"
                             size="small"
                             :columns="columns"
                             :data="cartList"
                             stripe
                             highlight-row>
                    </i-table>
    
                    <Modal :title="modalTitle" v-model="modalDetailVisible" :mask-closable="false" :width="500">
                        <div style="padding:5px;">
                            <div style="padding:5px;border-bottom: 1px dashed #ddd;">姓名：{{currentRecord.realName}}</div>
                            <div style="padding:5px;border-bottom: 1px dashed #ddd;">电话：{{currentRecord.phone}}</div>
                            <div style="padding:5px;border-bottom: 1px dashed #ddd;">账号：{{currentRecord.username}}</div>
                            <div style="padding:5px;border-bottom: 1px dashed #ddd;">Email：{{currentRecord.email}}</div>
                            <div style="padding:5px;border-bottom: 1px dashed #ddd;">建立时间：{{currentRecord.createTime}}</div>
                        </div>
                    </Modal>
                    
                       <Modal :title="modalEditTitle" v-model="modalEditVisible" :mask-closable="false" :width="500">
                          <i-form ref="currentRecord" :model="currentRecord" inline :label-width="70">
                                <div style="padding:5px;">
                                    
                                     <form-item class="br">姓名
                                         <i-input type="username"  v-model="currentRecord.username" placeholder="username">
                                          </i-input>
                                    </form-item>
                                    <form-item class="br">电话
                                         <i-input type="phone"  v-model="currentRecord.phone" placeholder="phone">
                                          </i-input>
                                    </form-item>
                                     <form-item class="br">Email
                                         <i-input type="email"  v-model="currentRecord.email" placeholder="email">
                                          </i-input>
                                    </form-item>
                                     <form-item class="br">建立时间
                                         <i-input type="createTime"  v-model="currentRecord.createTime" placeholder="createTime">
                                          </i-input>
                                    </form-item>
                              
                                </div>
      
                            </i-form>
                          <div slot="footer" align="center">
                              <Button class="btn" size="default" type="default" @click="modalEditVisible = false">取消</Button>
                              <Button class="btn" size="default" type="primary" @click="save">确定1</Button>
                          </div>  
                    </Modal>
                    
                </div>
                `
});

Vue.component('role-list', {
    data() {
        return {
            modalDetailVisible: false,//是否显示弹层
            cartList: [],//列表对象
            modalTitle: null,//当前条目标题
            currentRecord: {},//当前条目
            searchForm: {//检索表单
                pageNumber: 1,
                pageSize: 10,
                fromDate: "",
                toDate: "",
            },
            columns: [
                {
                    type: "selection",
                    width: 60,
                    align: "center",
                },
                {
                    title: '名称',
                    key: 'name'
                }, {
                    title: '建立时间',
                    key: 'createTime'
                },
                {
                    title: '操作',
                    render: (h, params) => {
                        return h('div', [
                            h('Button', {
                                props: {
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '5px'
                                },
                                on: {
                                    click: () => {
                                        this.detail(params.row);
                                    }
                                }
                            }, '查看'),
                            h('Button', {
                                props: {
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '5px'
                                },
                                on: {
                                    click: () => {
                                        this.edit(params.row);
                                    }
                                }
                            }, '编辑'),
                            h('Button', {
                                props: {
                                    type: 'error',
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '5px'
                                },
                                on: {
                                    click: () => {
                                        this.deleteItem(params.row.id);
                                    }
                                }
                            }, '删除')
                        ]);
                    }
                }
            ]
        }
    },
    methods: {
        selectDateRange(v) {

            if (v) {
                this.searchForm.fromDate = v[0];
                this.searchForm.toDate = v[1];
            }
        },
        handleSearch() {
            this.searchForm.pageNumber = 1;
            this.searchForm.pageSize = 10;
            this.getList();
        },
        getList() {
            getRequest('/role/list', this.searchForm).then(res => {
                this.cartList = res.data.data.records;
            });
        },
        edit(v) {
            this.modalTitle = "编辑";
            this.$refs.searchForm.resetFields();
            this.detail(v);
        },
        detail(v) {
            this.modalTitle = "查看";
            for (let attr in v) {
                if (v[attr] == null) {
                    v[attr] = "";
                }
            }
            let str = JSON.stringify(v);
            let info = JSON.parse(str);
            axios.get('/user/' + info.id).then(res => (this.currentRecord = res.data.data));
            this.modalDetailVisible = true;
        },
        deleteItem: function (id) {
            for (let i = 0; i < this.cartList.length; i++) {
                if (this.cartList[i].id === id) {
                    // 询问是否删除
                    this.$Modal.confirm({
                        title: '提示',
                        content: '确定要删除吗？',
                        onOk: () => {
                            axios.del('/role/del' + info.id).then(res => {
                                this.cartList.splice(i, 1);
                            });
                        },
                        onCancel: () => {
                            // 什么也不做
                        }
                    });
                }
            }
        }

    },
    mounted() {
        this.getList();
    },
    template: `<div>
                    <div class="search">
                        <i-form ref="searchForm" :model="searchForm" inline :label-width="70">
                              <form-item :label="name">
                                <date-picker
                                        type="daterange"
                                        format="yyyy-MM-dd"
                                        clearable
                                        @on-change="selectDateRange"
                                        placeholder="选择起始时间"
                                        style="width: 200px"
                                ></date-picker>
                            </form-item>
                            <form-item class="br">
                                <input type="button" @click="handleSearch" class="ivu-btn ivu-btn-default" value="搜 索"/>
                            </form-item>
                        </i-form>
                     </div>
                    <i-table id="datatable1"
                             size="small"
                             :columns="columns"
                             :data="cartList"
                             stripe
                             highlight-row>
                    </i-table>
    
                    <Modal :title="modalTitle" v-model="modalDetailVisible" :mask-closable="false" :width="500">
                        <div style="border-bottom:1px dashed #aaa;padding:5px;font-weight:bold">
                            <div style="font-weight:bold">姓名：{{currentRecord.realName}}</div>
                            <div style="font-weight:bold">电话：{{currentRecord.phone}}</div>
                            <div style="font-weight:bold">账号：{{currentRecord.username}}</div>
                            <div style="font-weight:bold">Email：{{currentRecord.email}}</div>
                            <div style="font-weight:bold">建立时间：{{currentRecord.createTime}}</div>
                        </div>
                    </Modal>
                </div>
                `
});

Vue.component('menu-list', {
    data() {
        return {
            modalDetailVisible: false,//是否显示弹层
            cartList: [],//列表对象
            modalTitle: null,//当前条目标题
            currentRecord: {},//当前条目
            searchForm: {//检索表单
                pageNumber: 1,
                pageSize: 10,
                fromDate: "",
                toDate: "",
            },
            columns: [
                {
                    type: "selection",
                    width: 60,
                    align: "center",
                },
                {
                    title: '名称',
                    key: 'title'
                }, {
                    title: '建立时间',
                    key: 'createTime'
                },
                {
                    title: '操作',
                    render: (h, params) => {
                        return h('div', [
                            h('Button', {
                                props: {
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '5px'
                                },
                                on: {
                                    click: () => {
                                        this.detail(params.row);
                                    }
                                }
                            }, '查看'),
                            h('Button', {
                                props: {
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '5px'
                                },
                                on: {
                                    click: () => {
                                        this.edit(params.row);
                                    }
                                }
                            }, '编辑'),
                            h('Button', {
                                props: {
                                    type: 'error',
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '5px'
                                },
                                on: {
                                    click: () => {
                                        this.deleteItem(params.row.id);
                                    }
                                }
                            }, '删除')
                        ]);
                    }
                }
            ]
        }
    },
    methods: {
        selectDateRange(v) {

            if (v) {
                this.searchForm.fromDate = v[0];
                this.searchForm.toDate = v[1];
            }
        },
        handleSearch() {
            this.searchForm.pageNumber = 1;
            this.searchForm.pageSize = 10;
            this.getList();
        },
        getList() {
            getRequest('/permission/query', this.searchForm).then(res => {
                this.cartList = res.data.data.records;
            });
        },
        edit(v) {
            this.modalTitle = "编辑";
            this.$refs.searchForm.resetFields();
            this.detail(v);
        },
        detail(v) {
            this.modalTitle = "查看";
            for (let attr in v) {
                if (v[attr] == null) {
                    v[attr] = "";
                }
            }
            let str = JSON.stringify(v);
            let info = JSON.parse(str);
            axios.get('/permission/' + info.id).then(res => (this.currentRecord = res.data.data));
            this.modalDetailVisible = true;
        },
        deleteItem: function (id) {
            for (let i = 0; i < this.cartList.length; i++) {
                if (this.cartList[i].id === id) {
                    // 询问是否删除
                    this.$Modal.confirm({
                        title: '提示',
                        content: '确定要删除吗？',
                        onOk: () => {
                            axios.del('/permission/' + info.id).then(res => {
                                this.cartList.splice(i, 1);
                            });
                        },
                        onCancel: () => {
                            // 什么也不做
                        }
                    });
                }
            }
        }

    },
    mounted() {
        this.getList();
    },
    template: `<div>
                    <div class="search">
                        <i-form ref="searchForm" :model="searchForm" inline :label-width="70">
                              <form-item :label="name">
                                <date-picker
                                        type="daterange"
                                        format="yyyy-MM-dd"
                                        clearable
                                        @on-change="selectDateRange"
                                        placeholder="选择起始时间"
                                        style="width: 200px"
                                ></date-picker>
                            </form-item>
                            <form-item class="br">
                                <input type="button" @click="handleSearch" class="ivu-btn ivu-btn-default" value="搜 索"/>
                            </form-item>
                        </i-form>
                     </div>
                    <i-table id="datatable1"
                             size="small"
                             :columns="columns"
                             :data="cartList"
                             stripe
                             highlight-row>
                    </i-table>
    
                    <Modal :title="modalTitle" v-model="modalDetailVisible" :mask-closable="false" :width="500">
                        <div style="border-bottom:1px dashed #aaa;padding:5px;font-weight:bold">
                            <div style="font-weight:bold">姓名：{{currentRecord.realName}}</div>
                            <div style="font-weight:bold">电话：{{currentRecord.phone}}</div>
                            <div style="font-weight:bold">账号：{{currentRecord.username}}</div>
                            <div style="font-weight:bold">Email：{{currentRecord.email}}</div>
                            <div style="font-weight:bold">建立时间：{{currentRecord.createTime}}</div>
                        </div>
                    </Modal>
                </div>
                `
});

var routes = [];
$.ajax({
    url: "/permission/list?pageSize=10000",
    async: false,//这块需要是同步请求，将同步的结果赋值常量routerConfig
    success: function (res) {
        var item = res.data.records;
        for (var i in item) {
            if (item[i].path != null && item[i].path != '' && item[i].type == 0) {
                routes.push({path: item[i].path, component: {template: item[i].filePath}});
            }
        }
        return routes;
    }
})
export const routerConfig = routes;

//父组件调用子组件
Vue.component('tree', {
    template: `
    <div>大树<sons/></div>`

});
//子组件完成递归
Vue.component('sons', {

    props: {
        subList: Array
    },
    data: function () {
        return {
            list: this.subList ? this.subList : [{ //空值判断，递归时subList不为空
                name: "儿子",
                son: [
                    {name: "儿子1", son: [{name: "儿子1-孙子1"}]},
                    {name: "儿子2", son: [{name: "儿子2-孙子1"}, {name: "儿子2-孙子2"}]}]
            }]
        }
    },

    template: `
              <div><!-- 这个div标签是必须的 -->
                <ul v-for="item in list" :key="item.name">
                    <li>
                        {{item.name}}
                    </li>
                    <ul v-if="item.son">
                       <sons :subList="item.son"></sons>
                    </ul>
                </ul>
              </div>
            `

});






