function run (data) {
  postMessage(data);      //向主线程返回数据
}

onmessage = function (event) {   //接收主线程的参数
  run(event.data)
}