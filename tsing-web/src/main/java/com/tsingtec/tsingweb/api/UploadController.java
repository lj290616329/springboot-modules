package com.tsingtec.tsingweb.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.tsingtec.tsingweb.config.qiniu.ConstantQiniu;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * @Author lj
 * @Date 2020/3/9 16:42
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    private ConstantQiniu constantQiniu;

    @Value("${file-path}")
    private String docBase;

    @PostMapping(value = "/file")
    public JSONObject uploadCover(@RequestParam("file") MultipartFile multipartFile) {
        System.out.println(new Date().getTime());
        /**
         * 文件保存路径按照日期进行保存
         */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        //当前文件加
        String savePath = sdf.format(new Date());

        File saveFile = new File(docBase+"/"+savePath);
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","上传成功");

        String fileName = "";
        String fileRandomName = "";
        try {

            JSONObject result = new JSONObject();

            fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            fileRandomName = UUID.randomUUID().toString() + fileType;
            //使用绝对路径进行文件保存
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), new File(docBase+"/"+savePath +"/"+ fileRandomName));

            result.put("src","/" + savePath + "/" + fileRandomName);
            result.put("title",fileName);
            jsonObject.put("data",result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Message:文件:{}，保存失败", fileName);
            jsonObject.put("code",-1);
            jsonObject.put("msg","文件上传失败");
        }
        return jsonObject;
    }

    /**
     * base64 格式为:
     * @param json
     * @return
     */
    @PostMapping("/base64")
    public JSONObject base64(@RequestBody String json){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","上传成功");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String savePath = sdf.format(new Date());

        File saveFile = new File(docBase+"/"+savePath);

        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }

        String[] strs = json.split(",");

        String fileRandomName = UUID.randomUUID().toString()+strs[2];

        File file = new File(docBase+"/"+saveFile, fileRandomName);
        byte[] fileBytes = Base64.getDecoder().decode(strs[1]);
        try {

            JSONObject result = new JSONObject();

            FileUtils.writeByteArrayToFile(file, fileBytes);

            result.put("src","/" + savePath +"/"+ fileRandomName);
            result.put("title",fileRandomName);
            jsonObject.put("data",result);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Message:文件:{}，保存失败", fileRandomName);
            jsonObject.put("code",-1);
            jsonObject.put("msg","文件上传失败");
        }
        return jsonObject;
    }


    /**
     * 上传文件到七牛云存储
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @PostMapping("/qiniu")
    public JSONObject uploadImgQiniu(@RequestParam("file") MultipartFile multipartFile){
        FileInputStream inputStream = null;
        try {
            inputStream = (FileInputStream) multipartFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject result = uploadQNImg(inputStream, UUID.randomUUID().toString()); // KeyUtil.genUniqueKey()生成图片的随机名
        return result;
    }

    /**
     * 将图片上传到七牛云
     */
    private JSONObject uploadQNImg(FileInputStream file, String key) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","上传成功");
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        // 其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // 生成上传凭证，然后准备上传
        try {
            Auth auth = Auth.create(constantQiniu.getAccessKey(), constantQiniu.getSecretKey());
            String upToken = auth.uploadToken(constantQiniu.getBucket());
            try {
                Response response = uploadManager.put(file, key, upToken, null, null);
                // 解析上传成功的结果
                DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
                String returnPath = constantQiniu.getPath() + "/" + putRet.key;

                JSONObject result = new JSONObject();
                result.put("src",returnPath);
                result.put("title","");
                jsonObject.put("data",result);
            } catch (QiniuException ex) {
                jsonObject.put("code",-1);
                jsonObject.put("msg","图片上传失败");
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception e) {
            jsonObject.put("code",-1);
            jsonObject.put("msg","图片上传失败");
            e.printStackTrace();
        }
        return jsonObject;
    }

}
