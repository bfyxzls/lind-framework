<#import "../template.ftl" as layout>
<@layout.registrationLayout ;section>
    <#if section = "head">
        用户列表
    <#elseif section="head-js">
        <script type="module">
            import {routerConfig} from "/lib/component.js"

            new Vue({
                router: new VueRouter({
                    //mode: 'history',  //  访问路径不带#号
                    routes: routerConfig
                }),
                el: '#app'
            });
        </script>
    <#elseif section = "form">
        <router-view></router-view>
    </#if>
</@layout.registrationLayout>
