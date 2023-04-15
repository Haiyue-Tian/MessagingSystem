package com.haiyue.messaging.factory.converter;

import com.haiyue.messaging.enums.MessageType;
import com.haiyue.messaging.enums.Status;
import com.haiyue.messaging.exception.MessageServiceException;

public class ConverterFactory {
    // not tested
    public static Converter getConverter(MessageType messageType) throws MessageServiceException {
        if (messageType == MessageType.IMAGE) {
            return new ImageConverter();
        } else if (messageType == MessageType.VOICE) {
            return new VoiceConverter();
        } else if (messageType == MessageType.VIDEO) {
            return new VideoConverter();
        } else if (messageType == MessageType.STICKER) {
            return new StickerConverter();
        } else {
            throw new MessageServiceException(Status.UNKNOWN_MESSAGE_TYPE);
        }
    }
}
