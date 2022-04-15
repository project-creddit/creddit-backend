package com.creddit.credditmainserver.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwsS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    public String getImgUrl(String fileName){
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    public String upload(MultipartFile file, String dirName) {
        String oriFileName = file.getOriginalFilename();
        checkFilenameExtension(oriFileName);

        String fileName = dirName + "/" + UUID.randomUUID().toString() + oriFileName;
        String uploadUrl = putS3(file, fileName);

        return fileName;
    }

    public void deleteFile(String fileName){
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    private String putS3(MultipartFile file, String fileName) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try(InputStream inputStream = file.getInputStream()){
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            return getImgUrl(fileName);
        }catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }
    }

    private void checkFilenameExtension(String fileName) {
        String extension = FilenameUtils.getExtension(fileName);

        if(!(extension.equals("jpg") || extension.equals("png") || extension.equals("gif"))){
            log.info("확장자 : " + extension);
            throw new IllegalArgumentException("확장자가 적절하지 않습니다.");
        }
    }



}
