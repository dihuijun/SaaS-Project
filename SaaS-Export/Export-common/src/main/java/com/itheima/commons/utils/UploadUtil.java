package com.itheima.commons.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.io.ByteArrayInputStream;

public class UploadUtil {

    private static final String accessKey = "in1PI6uSce9R9-ZoiJZ_AjH3DRUdPwgpDh7YlDfC";
    private static final String secretKey = "fVy6qph1sENVMyW8z6A1dumUgd3hfitok-JyT3Ef";
    private static final String bucket = "heima111";
    private static final String url = "http://q0sszo53y.bkt.clouddn.com/";


    public String upLoad(byte[] uploadBytes){
        //调用七牛云进行上传

        //构造一个带指定Region对象的配置类
        Configuration cfg = new Configuration(Region.region0());

        UploadManager uploadManager = new UploadManager(cfg);
        //生成上传凭证然后准备上传
        String key = null;

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(uploadBytes);
        Auth auth = Auth.create(accessKey,secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(byteArrayInputStream,key,upToken,null,null);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(),DefaultPutRet.class);
            key = putRet.key;
        }catch (QiniuException ex){
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException e) {
                e.printStackTrace();
            }
        }
    return url+key;

    }


}
