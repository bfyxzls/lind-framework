<#import "../template_login.ftl" as layout>
<@layout.registrationLayout bodyClass="<span style='color:red'>修改模板里的变量</span>";section>
    <#if section = "head">
        登录【admin,abcd1234】
    <#elseif section = "form">
        <div id="login" class="flex" style="padding: 10% 10%;">
            <!-- 实例化对象里面可以开始使用iview标签了（注意更改） -->
            <Card style="width:350px">
                <h1 style="color: #2d8cf0;">Login</h1>
                <br>
                <!-- 下面的:model和:rules要修改的话记得修改vue中的data() -->
                <i-form ref="form" :model="form" :rules="formValidate">
                    <poptip trigger="focus" placement="right">
                        <i-input type="text" clearable v-model="form.username" placeholder="Username" size="large">
                            <icon type="ios-person-outline" slot="prepend"></icon>
                        </i-input>
                        <div slot="content">请输入用户名</div>
                    </poptip>
                    </form-item>
                    <br>
                    <form-item prop="password">
                        <poptip trigger="focus" placement="right">
                            <i-input type="password" password v-model="form.password" placeholder="Password"
                                     size="large">
                                <icon type="ios-lock-outline" slot="prepend"></icon>
                            </i-input>
                            <div slot="content">请输入密码</div>
                        </poptip>
                    </form-item>
                    <span style="color:red" :text="error"></span>
                    <br>
                    <form-item>
                        <i-button type="primary" size="large" @click="submit">登录</i-button>
                    </form-item>
                </i-form>
            </card>
        </div>
         <script type="module">
            import {auth} from "/lib/index.js";

            new Vue({
                el: '#login',
                data() {
                    return {
                        form: {
                            username: '',
                            password: ''
                        },
                        formValidate: {
                            // 表单验证规则
                            username: [{required: true, message: "不能为空", trigger: "blur"}],
                            password: [{required: true, message: "不能为空", trigger: "blur"}]
                        },
                        error: ""
                    }
                },
                methods: {

                    handleReset() {
                        // 复位表单
                        this.$refs.form.resetFields();
                    },
                    submit() {
                        // 这里使用axios将数据传到后台
                        this.$refs.form.validate(valid => {
                            if (valid) {
                                auth(this.form).catch(function (error) {
                                    alert('测试')
                                });
                            }
                        });
                    }
                }
            })
            // const Test={
            //     el: '#test',
            //     data(){
            //         return {
            //             message:"世界，你好"
            //         }
            //     }
            // }
            // new Vue(Test);
        </script>
    </#if>
</@layout.registrationLayout>
