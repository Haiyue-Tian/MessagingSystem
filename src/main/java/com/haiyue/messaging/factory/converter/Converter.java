package com.haiyue.messaging.factory.converter;

import com.haiyue.messaging.exception.MessageServiceException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

public interface Converter {
    ByteArrayInputStream converter(MultipartFile multipartFile) throws MessageServiceException;
}
