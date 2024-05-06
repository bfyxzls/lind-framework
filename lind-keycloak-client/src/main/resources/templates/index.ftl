<!DOCTYPE html>
<html>
<head>
    <h1>测试keycloak-check-session-status</h1>
</head>
<body>
<!-- 主域相同，可以执行iframe里关于cookie的代码 -->
<span id="username"></span>
<a href="https://testcas.pkulaw.com/auth/realms/fabao/protocol/openid-connect/auth?client_id=democlient&response_type=code&scope=openid&redirect_uri=http://lind.pkulaw.com:8811">登录</a>
<iframe src="https://testcas.pkulaw.com/auth/realms/fabao/protocol/openid-connect/login-status-iframe.html"
        id="keycloak-status-iframe" style="display:none"></iframe>

</iframe>
<script>
    function getCookieByName(name) {
        name = name + '=';
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i].trim();
            if (c.indexOf(name) === 0) return c.substring(name.length, c.length);
        }
        return '';
    }

    function clearCookieByName(name) {
        document.cookie = name + "=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/";
    }

    if (getCookieByName('session_state') != "") {
        document.getElementById("username").innerHTML = "欢迎您" + getCookieByName('session_state');
    }
    var iframe = document.getElementById('keycloak-status-iframe');
    iframe.onload = function () {
        var cookie = getCookieByName('session_state');

        val = "democlient " + cookie;//这里向iframe传的参数是"client_id session_state"

        iframe.contentWindow.postMessage(val, 'https://testcas.pkulaw.com:18081');
        window.addEventListener('message', function (event) {
            if (event.origin !== 'https://testcas.pkulaw.com:18081') {
                return;
            }
            if (event.data === 'unchanged') {
                // 用户会话未发生变化，登录状态未改变
                console.log('User session unchanged');
            } else if (event.data === 'changed') {
                // 用户会话状态发生变化，可能已经注销，需要同步清我的会话状态
                clearCookieByName("session_state");

                document.getElementById("username").innerHTML = "未登录";
                console.log("session_state=" + getCookieByName('session_state'));
                // location.href="https://testcas.pkulaw.com:18081/auth/realms/fabao/protocol/openid-connect/auth?client_id=democlient&response_type=code&scope=openid&redirect_uri=http://lind.pkulaw.com:8811"
            }
        }, false);


    }
</script>
</body>
</html>
