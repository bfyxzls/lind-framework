<!DOCTYPE html>
<html>
<head>
</head>
<body>
<h1>测试keycloak-check-session-status</h1>
<ul>
    <li>Content-Security-Policy中的frame-ancestors影响它</li>
    <li>X-Frame-Options的配置影响它</li>
</ul>
<iframe width="100%" height="100%"
        src="http://test.pkulaw.com:8080/auth/realms/fabao/protocol/openid-connect/auth?scope=openid&response_type=code&client_id=pkulaw&redirect_uri=https://test1.pkulaw.com/">
</iframe>
</body>
</html>

