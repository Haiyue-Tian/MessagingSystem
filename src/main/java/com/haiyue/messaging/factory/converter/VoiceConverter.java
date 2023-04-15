package com.haiyue.messaging.factory.converter;

import com.haiyue.messaging.exception.MessageServiceException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

public class VoiceConverter implements Converter{
    @Override
    public ByteArrayInputStream converter(MultipartFile multipartFile) throws MessageServiceException {
        return null;
    }
}
