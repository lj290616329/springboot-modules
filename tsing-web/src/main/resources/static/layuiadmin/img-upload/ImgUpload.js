layui.define(['jquery','upload'], function (exports) {
    var ImgUpload = function () {
        var $ = jQuery = layui.jquery;
        var upload = layui.upload;
        var form = layui.form;
        /*
        *  头像上传
        **/
        upload.render({
            elem: '.head'
            ,url: '/api/upload/qiniu'//接口url
            ,acceptMime: 'image/*'
            ,done: function(res){
                var item = this.item;
                item[0].src = res.data.src;
                $("#pic").val(res.data.src);
            }
        });
        /**
         * 头像移除
         */
        $(".removeHead").click(function(){
            console.log(1)
            $('.head').attr("src", '/images/add.png');
            $("#pic").val('');
        });

        /**
         * 多图上传
         */
        upload.render({
            elem: '.addImg'
            ,url: '/api/upload/qiniu'//接口url
            ,acceptMime: 'image/*'
            ,done: function(res){
                console.log(res);
                if(res.code==0){
                    var item = this.item;
                    console.log(item);
                    item.before("<div class=\"pic_box\">\n" +
                        "                            <i class=\"layui-icon layui-icon-close-fill  removeImg\" data-src=\""+res.data.src+"\"></i>\n" +
                        "                            <img class=\"background\" src='"+res.data.src+"'>\n" +
                        "                        </div>");
                    var input = item.siblings('.value-input');

                    console.log(input);
                    var val = $(input).val();
                    console.log(val);
                    if(val==''){
                        $(input).val(res.data.src);
                    }else{
                        $(input).val(val+','+res.data.src);
                    }
                    var numBox = item.siblings('.pic-num');
                    var num_spans = numBox.children("span");
                    var num_max = num_spans.eq(1).html();
                    var num_min = num_spans.eq(0).html();
                    if(++num_min>=num_max){
                        item.hide()
                    }
                    num_spans.eq(0).html(num_min);
                }
            }
        });

        var checkNum = function(obj){
            var num_spans = obj.children("span");
            var num_max = num_spans.eq(1).html();
            console.log(num_max)
            var num_min = num_spans.eq(0).html();
            console.log(num_min)

        }
        /**
         * 多图删除
         */

        $('body').delegate(".removeImg","click",function(){
            var src = this.getAttribute("data-src")
            var picBox = $(this).parent('.pic_box');

            console.log(picBox)
            var input = $(picBox).siblings('.value-input');
            var val = $(input).val();
            var val_url = val.split(",");
            var i = val_url.indexOf(src);
            val_url.splice(i, 1);

            var addImg = $(picBox).siblings('.addImg');
            var numBox = $(picBox).siblings('.pic-num');
            var num_spans = numBox.children("span");
            var num_max = num_spans.eq(1).html();
            var num_min = num_spans.eq(0).html();
            num_spans.eq(0).html(--num_min)
            $(addImg).show()
            $(picBox).remove();
            $(input).val(val_url.join(","))
        })
    };
    exports('ImgUpload', ImgUpload);
})