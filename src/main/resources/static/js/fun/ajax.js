function get(url, success, error) {
    let jwt = $.cookie("jwt");
    console.log("cookieJwt:  "+jwt);
    $.ajax({
        url: url,
        type: 'get',
        dateType: 'json',
        contentType: "application/json",
        cache: false,
        async: true,
        headers: {
            'jwt': jwt
        },
        success: function(data) {
            console.log("请求成功")
            if (success !== undefined) success(data.code, data.msg, data.data);
        },
        error: function(data) {
            console.log("请求失败")
            if (error !== undefined) error(data);
        }
    });
}

function post(url, data, success, error) {
    let jwt = $.cookie("jwt");
    console.log("cookieJwt:  "+jwt);
    $.ajax({
        url: url,
        type: 'post',
        dateType: 'json',
        contentType: "application/json",
        cache: false,
        async: true,
        headers: {
            'jwt': jwt
        },
        data: JSON.stringify(data),
        success: function(data) {
            console.log("请求成功")
            if (success !== undefined) success(data.code, data.msg, data.data);
        },
        error: function(data) {
            console.log("请求失败")
            console.log(data)
            if (error !== undefined) error(data);
        }
    });
}