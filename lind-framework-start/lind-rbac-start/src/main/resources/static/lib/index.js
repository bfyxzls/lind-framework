import {getStore, setStore} from "/static/lib/storage.js"
// export和export default都是导出，export default只能有一个，在使用import导入时export的需要加{}，而export default的不需要加{}

// get
export function getRequest(url, params) {
    return axios({
        method: 'get',
        url: url,
        params: params,
        headers: {
            'Authorization': getStore("auth")
        }
    }).catch((err) => {
        if (err.response.status == 401) {
            location.href = "/login";
        }
    });
}

// post
export function postRequest(url, params) {
    return axios({
        method: 'post',
        url: url,
        data: params,
        headers: {
            'Authorization': getStore("auth")
        }
    }).catch((err) => {
        if (err.response.status == 401) {
            location.href = "/login";
        }
    });
}

export function putRequest(url, params) {
    return axios({
        method: 'put',
        url: url,
        data: params,
        headers: {
            'Authorization': getStore("auth")
        }
    }).catch((err) => {
        if (err.response.status == 401) {
            location.href = "/login";
        }
    });
}

// 认证
export function auth(params) {
    return axios({
        method: 'post',
        url: '/login',
        data: params
    }).then(res => {
        setStore("auth", res.data.token);
        location.href = "/index";
    });
}

