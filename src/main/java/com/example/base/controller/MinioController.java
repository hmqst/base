package com.example.base.controller;

import com.example.base.utils.ResultBean;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("minio")
@Api(tags = "文件相关")
@Slf4j
public class MinioController {

    @Resource
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    @ApiOperation("上传文件")
    @PostMapping("upload")
    public ResultBean upload(MultipartFile file) {
        if (file == null) {
            return ResultBean.fail("请选择要上传的文件");
        }
        try {
            InputStream inputStream = file.getInputStream();
            // 文件名
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            // 类型
            String contentType = file.getContentType();
            if (StringUtils.isEmpty(contentType)) {
                return ResultBean.fail("获取文件类型失败，未知的文件类型。");
            }
            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucketName);
            if (!isExist) {
                minioClient.makeBucket(bucketName);
            }
            PutObjectOptions putObjectOptions = new PutObjectOptions(inputStream.available(), -1);
            putObjectOptions.setContentType(contentType);
            minioClient.putObject(bucketName, fileName, inputStream, putObjectOptions);
            return ResultBean.success("上传成功");
        } catch (Exception e) {
            log.error("上传文件失败，错误信息：" + e.getMessage());
            return ResultBean.fail("上传失败" + e.getMessage());
        }
    }

    @ApiOperation("下载文件")
    @GetMapping("download")
    public ResultBean download(String fileName, HttpServletResponse response) throws IOException {
        try {
            InputStream inputStream = minioClient.getObject(bucketName, fileName);
            IOUtils.copy(inputStream, response.getOutputStream());
            inputStream.close();
            return null;
        } catch (Exception e) {
            log.error("文件下载失败，错误原因：" + e.getMessage());
            return ResultBean.fail("下载失败");
        }
    }

    @ApiOperation("获取文件下载链接")
    @GetMapping("getFileUrl")
    public ResultBean getFileUrl(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return ResultBean.fail("文件名不可为空");
        }
        try {
            return ResultBean.success(minioClient.presignedGetObject(bucketName, fileName));
        } catch (Exception e) {
            log.error("获取文件下载链接失败，错误信息" + e.getMessage());
            return ResultBean.fail("获取失败");
        }
    }
}
