package com.titxu.blog.controller;


import com.titxu.blog.utils.QiniuUtils;
import com.titxu.blog.utils.result.CodeMsg;
import com.titxu.blog.utils.result.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author lxue
 * @date 2021/8/14
 * @apiNate
 */
@RestController
@RequestMapping("upload")
public class UploadController {
    @Autowired
    private QiniuUtils qiniuUtils;

    @PostMapping("qiniu")
    public Result uoload(@RequestParam("image") MultipartFile file) {
        //上传原始文件名
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString()+"."+ StringUtils.substringAfter(originalFilename,".");
        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload) {
            return Result.success(QiniuUtils.url+fileName);
        }

        return Result.fail(CodeMsg.IMAGE_UPLOAD_ERROR.getRetCode(),CodeMsg.IMAGE_UPLOAD_ERROR.getMessage());


    }
}
