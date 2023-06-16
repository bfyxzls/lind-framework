<#macro registrationLayout bodyClass="默认值，子页面没有设置会使用这个值，子页面设置后会覆盖它" mainCss="red">
<#--语法介绍：-->
<#--<#macro name param1 param2 ... paramN>--><!-- macro在模板里定义的变量，在子页面中可以重写变量的内容，它会反映的模板里-->
<#--<#nested loopvar1, loopvar2, ..., loopvarN>-->
<#--<p>${param1?cap_first}-->
    <!--
CDN上引用vueJS后，使用i开头的标签
Button	i-button
Col	i-col
Tabel	i-tabel
Input	i-input
Form	i-form
Menu	i-menu
Select	i-sekect
Option	i-option
Progress	i-progress
Switch	i-switch
-->
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">

        <title><#nested "head"></title>

        <meta name="renderer" content="webkit|ie-comp|ie-stand">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <meta http-equiv="Cache-Control" content="no-siteapp"/>
        <link rel="stylesheet" href="static/css/iview.css">
        <link rel="stylesheet" href="static/css/site.css">
        <style type="text/css">
            .ivu-form-item-content {
                margin-left: 0px !important;
            }
        </style>
        <script charset="UTF-8" src="static/js/vue.min.js"></script>
        <script charset="UTF-8" src="static/js/vue-router.js"></script>
        <script charset="UTF-8" src="static/js/vuex.min.js"></script>
        <script charset="UTF-8" src="static/js/jquery.min.js"></script>
        <script charset="UTF-8" src="static/js/axios.min.js"></script>
        <script charset="UTF-8" src="static/js/vuejs-datepicker.js"></script>
        <script charset="UTF-8" src="static/js/iview.min.js"></script>
        <script charset="UTF-8" src="static/lib/component.js" type="module"></script>
        <!-- 因为在component.js中引用了其它模块，所以需要添加type=module-->
        <#nested "head-js">
    </head>
    <body>
    <div id="app">
        <div class="layout" style="position: absolute;top: 0px;bottom: 0px;left: 0px;right: 0px;">

            <i-menu mode="horizontal" theme="dark" active-name="1">
                <span  style="font-size: 18px; font-weight: bolder;color:#ffffff;padding-left:10px;">后台管理系统</span>
                <div class="nav-left" style="float: right">
                    <menu-item name="1">
                        <Icon type="ios-navigate"></Icon>
                        系统介绍
                    </menu-item>
                    <menu-item name="2">
                        <Icon type="ios-keypad"></Icon>
                        关于大叔
                    </menu-item>
                    <menu-item name="3">
                        <Icon type="ios-analytics"></Icon>
                        个人中心
                    </menu-item>
                </div>
            </i-menu>
            <div class="layout-content">
                <Row>
                    <i-col span="5">
                        <nav-menu></nav-menu>
                    </i-col>
                    <i-col span="19">
                        <div class="layout-header"></div>
                        <breadcrumb></breadcrumb>
                        <div class="layout-content">
                            <#nested "form">
                    </i-col>
                </Row>
            </div>
            <div class="layout-copy">
                2011-2016 &copy; TalkingData
            </div>
        </div>

    </div>
    </body>
    </html>
</#macro>
