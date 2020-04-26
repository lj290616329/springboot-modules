/*CoreUtil*/
/*工具类，类似java静态工具类*/
layui.define('jquery', function (exports) {
var CoreUtil = {
    /**
     *
     * @param url  地址
     * @param params 参数
     * @param ft 成功时执行的方法
     * @param method 方法类型
     * @param contentType 参数类型
     * @param async 同步异步
     */
    sendAjax :function (url, params, ft, method, contentType, async) {
        var roleSaveLoading = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        var $ = layui.jquery;
        $.ajax({
            url: url,
            cache: false,
            async: async == undefined ? true : async,
            data: params,
            type: method == undefined ? "POST" : method,
            contentType: contentType == undefined ? 'application/json; charset=UTF-8': contentType ,
            dataType: "json",
            success: function (res) {
                console.log(res);
                top.layer.close(roleSaveLoading);
                if (typeof ft == "function") {
                    if(res.code == 200){
                        if(ft!=null && ft!=undefined){
                            ft(res)
                        }
                    }else if(res.code == 400){
                        top.layer.msg(res.msg, {
                            offset: 't',
                            anim: 6,
                        });
                    }else if(res.code == 403){
                        window.location.href = "/index/403"
                    }else if(res.code == -1){
                        top.layer.msg(res.msg, {
                            offset: 't',
                            anim: 6,
                        }, function(){
                            top.window.location.href = "/index/login"
                        });
                    }else if(res.code == 500){
                        top.layer.msg(res.msg, {
                            offset: 't',
                            anim: 6,
                        }, function(){
                            window.location.href = "/index/500"
                        });
                    }else{
                        top.layer.msg(res.msg, {
                            offset: 't',
                            anim: 6,
                        }, function(){
                            window.location.href = "/index/500"
                        });
                    }
                }
            },
            error:function (XMLHttpRequest, textStatus, errorThrown) {
               top.layer.close(roleSaveLoading);
               if(XMLHttpRequest.status==404){
                    location.href="/index/404";
               }else{
                   layer.msg("服务器好像除了点问题！请稍后试试", function(){
                       window.location.href = "/index/500"
                   });
               }
            }
        });
    },
    /*表单数据封装成 json String*/
    formJson : function (frm) {  //frm：form表单的id
        var o = {};
        var a = $("#"+frm).serializeArray();
        $.each(a, function() {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [ o[this.name] ];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return JSON.stringify(o);
    },
    /*存入本地缓存*/
    setData : function(key, value){
        layui.data('LocalData',{
            key :key,
            value: value
        })
    },
    /*从本地缓存拿数据*/
    getData : function(key){
        var localData = layui.data('LocalData');
        return localData[key];
    },
    formattime:function (val) {
        var date=new Date(val);
        var year=date.getFullYear();
        var month=date.getMonth()+1;
        month=month>9?month:('0'+month);
        var day=date.getDate();
        day=day>9?day:('0'+day);
        var hh=date.getHours();
        hh=hh>9?hh:('0'+hh);
        var mm=date.getMinutes();
        mm=mm>9?mm:('0'+mm);
        var ss=date.getSeconds();
        ss=ss>9?ss:('0'+ss);
        var time=year+'-'+month+'-'+day+' '+hh+':'+mm+':'+ss;
        return time;
    }
};
exports('CoreUtil', CoreUtil);
});