package org.example.web_service_v2.global.storage;

import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.global.apiPayload.exception.BusinessException;
import org.example.web_service_v2.global.apiPayload.exception.ErrorCode;
import org.example.web_service_v2.global.config.ImageUploadProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ImageValidator {
    private final ImageUploadProperties props;

    public void validate(MultipartFile file){
        if (file == null || file.isEmpty()){
            throw new BusinessException(ErrorCode.IMAGE_EMPTY);
        }

        if (file.getSize() > props.getMaxSizeBytes()){
            throw new BusinessException(ErrorCode.IMAGE_TOO_LARGE);
        }

        String contentType = file.getContentType();
        if (contentType == null || props.getAllowedContentTypes().stream().noneMatch(contentType::equalsIgnoreCase)){
            throw new BusinessException(ErrorCode.IMAGE_UNSUPPORTED_TYPE);
        }

        if (props.isVerifyImageContent()) {
            try {
                if (ImageIO.read(file.getInputStream()) == null) {
                    throw new BusinessException(ErrorCode.IMAGE_INVALID_CONTENT);
                }
            } catch (IOException e) {
                throw new BusinessException(ErrorCode.IMAGE_INVALID_CONTENT);
            }
        }
    }
}
