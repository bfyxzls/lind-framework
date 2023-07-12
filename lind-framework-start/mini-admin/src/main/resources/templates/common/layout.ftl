<#macro mainLayout>
    <!DOCTYPE html>
    <html>
    <head>

        <#import "./common.macro.ftl" as netCommon>
        <@netCommon.commonStyle />
        <!-- daterangepicker -->
        <link rel="stylesheet"
              href="${request.contextPath}/static/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.css">
        <@netCommon.commonScript />
        <!-- daterangepicker -->
        <script src="${request.contextPath}/static/adminlte/bower_components/moment/moment.min.js"></script>
        <script src="${request.contextPath}/static/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.js"></script>
        <#-- echarts -->
        <script src="${request.contextPath}/static/plugins/echarts/echarts.common.min.js"></script>
        <!-- DataTables -->
        <script src="/static/adminlte/bower_components/datatables.net/js/jquery.dataTables.min.js"></script>
        <script src="/static/adminlte/bower_components/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
        <title>${I18n.admin_name}</title>

    </head>
    <body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && cookieMap["xxljob_adminlte_settings"]?exists && "off" == cookieMap["xxljob_adminlte_settings"].value >sidebar-collapse</#if> ">
    <div class="wrapper">
        <!-- header -->
        <@netCommon.commonHeader />
        <!-- left -->
        <@netCommon.commonLeft "index" />

        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper">
            <#nested "form">
        </div>
        <!-- /.content-wrapper -->

        <!-- footer -->
        <@netCommon.commonFooter />
    </div>

    </body>
    </html>
</#macro>
