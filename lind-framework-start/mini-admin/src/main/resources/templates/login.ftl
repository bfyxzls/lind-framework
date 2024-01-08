<!DOCTYPE html>
<html>
<head>
    <#import "./common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/iCheck/square/blue.css">
    <title>${I18n.admin_name}</title>
    <#--    <script>-->
    <#--        <!-- 为了测试iframe与主页面之间传递数据添加的 &ndash;&gt;-->
    <#--        window.addEventListener('message', function (event) {-->
    <#--            alert(event.origin);-->
    <#--            if (event.origin !== 'http://localhost:9090') { //http://localhost:9090是主页面的地址-->
    <#--                return;-->
    <#--            }-->
    <#--            // 处理从父页面发送过来的消息-->
    <#--            alert('Received message: ' + event.data);-->
    <#--            event.source.postMessage('Hello from iframe!', event.origin);-->
    <#--        }, false);-->
    <#--    </script>-->
</head>
<body class="hold-transition login-page">
<div class="login-box">
    <div class="login-logo">
        <a><b>LIND</b>-ADMIN</a>
    </div>
    <form id="loginForm" method="post">
        <div class="login-box-body">
            <p class="login-box-msg">${I18n.admin_name}</p>
            <div class="form-group has-feedback">
                <input type="text" id="userName" name="userName" class="form-control"
                       placeholder="${I18n.login_username_placeholder}" maxlength="18">
                <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
            </div>
            <div class="form-group has-feedback">
                <input type="password" name="password" class="form-control"
                       placeholder="${I18n.login_password_placeholder}" maxlength="18">
                <span class="glyphicon glyphicon-lock form-control-feedback"></span>
            </div>
            <div class="row">
                <div class="col-xs-8">
                    <div class="checkbox icheck">
                        <label>
                            <input type="checkbox" name="ifRemember"> &nbsp; ${I18n.login_remember_me}
                        </label>
                    </div>
                </div><!-- /.col -->
                <div class="col-xs-4">
                    <button type="submit" class="btn btn-primary btn-block btn-flat">${I18n.login_btn}</button>
                </div>
            </div>
        </div>
    </form>
</div>
<@netCommon.commonScript />
<script src="${request.contextPath}/static/adminlte/plugins/iCheck/icheck.min.js"></script>
<script src="${request.contextPath}/static/js/login.1.js"></script>
</body>
</html>
