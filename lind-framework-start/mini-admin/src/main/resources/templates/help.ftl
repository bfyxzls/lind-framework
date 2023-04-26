<!DOCTYPE html>
<html>
<head>
    <#import "./common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
    <title>${I18n.admin_name}</title>
</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && cookieMap["xxljob_adminlte_settings"]?exists && "off" == cookieMap["xxljob_adminlte_settings"].value >sidebar-collapse</#if> ">
<div class="wrapper">
    <!-- header -->
    <@netCommon.commonHeader />
    <!-- left -->
    <@netCommon.commonLeft "help" />

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>${I18n.job_help}</h1>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="callout callout-info">
                <h4>${I18n.admin_name_full}</h4>
                <br>
                <p>
                    mini-admin依赖框架
                </p>
                <ul>
                    <li>springboot:2.6.7</li>
                    <li>org.springframework.boot:spring-boot-starter-freemarker:2.6.7</li>
                    <li>com.baomidou:mybatis-plus-boot-starter:3.5.2</li>
                    <li>cn.hutool:hutool-all:5.5.9</li>
                </ul>
                <p></p>
            </div>
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <!-- footer -->
    <@netCommon.commonFooter />
</div>
<@netCommon.commonScript />
</body>
</html>
