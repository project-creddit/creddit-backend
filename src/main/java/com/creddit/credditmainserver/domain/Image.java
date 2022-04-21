package com.creddit.credditmainserver.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

@Slf4j
@Getter
public class Image {

    private String imgName;

    private String imgUrl;

    @Builder
    public Image(String imgName, String imgUrl){
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

    public void checkFilenameExtension(String fileName) {
        String extension = FilenameUtils.getExtension(fileName);

        if(!extension.equals(ImageFormatType.JPG) ||
                !extension.equals(ImageFormatType.PNG) ||
                !extension.equals(ImageFormatType.GIF)
        ){
            log.info("확장자 : " + extension);
            throw new IllegalArgumentException("확장자가 적절하지 않습니다.");
        }
    }
}
