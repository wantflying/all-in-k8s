console.log('开始抓取京东ck');
const url = $request.url;
const headers = $request.headers;
const cookie = headers["Cookie"] || headers["cookie"]; // 获取 Cookie

// 检查请求的路径
//if (!url.includes("mllog.jd.com")) {
    // 不是目标路径，直接返回
  //  $done();
//}

// 检查 Cookie 是否存在
if (!cookie) {
    console.log('Cookie 未找到');
    $notify('错误', 'Cookie 未找到', '');
    $done();
}

// 将 Cookie 拆分为行并解析 pt_pin 和 pt_key
let cookieLines = cookie.split(';');
let cookieData = {};

cookieLines.forEach(line => {
    if (line.includes('pt_pin=')) {
        const ptPinMatch = line.match(/pt_pin=([^;]+)/);
        if (ptPinMatch) {
            cookieData.ptPin = ptPinMatch[1]; // 提取 pt_pin
        }
    }
    if (line.includes('pt_key=')) {
        const ptKeyMatch = line.match(/pt_key=([^;]+)/);
        if (ptKeyMatch) {
            cookieData.ptKey = ptKeyMatch[1]; // 提取 pt_key
        }
    }
});

// 检查是否成功解析 pt_pin 和 pt_key
if (!cookieData.ptPin || !cookieData.ptKey) {
    console.log('ptPin 或 ptKey 未找到');
    $notify('错误', 'ptPin 或 ptKey 未找到', '');
    $done();
}
console.log('抓取到ck:');
console.log('ptPin:'+cookieData.ptPin);
console.log('ptKey:'+cookieData.ptKey);
// 拼接参数到 URL
const apiUrl = `http://192.168.123.136:8080/qlapi/updateJDCookie?ptPin=${encodeURIComponent(cookieData.ptPin)}&ptKey=${encodeURIComponent(cookieData.ptKey)}`;

// 调用接口，使用 GET 请求
$task.fetch({
    url: apiUrl,
    method: 'GET'
    // headers: {
    //     'Content-Type': 'application/json'
    // }
}).then(response => {
    console.log('接口请求正常，响应:', response);
    // 弹出成功通知
    $notify('成功', '接口请求成功', response.body);
}).catch(error => {
    console.log('请求失败:', error);
    // 弹出失败通知
    $notify('错误', '请求失败: ' + error.message, '');
}).finally(() => {
    $done();
});
