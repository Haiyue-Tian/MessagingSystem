package com.haiyue.messaging.factory.converter;

import com.haiyue.messaging.enums.Status;
import com.haiyue.messaging.exception.MessageServiceException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageConverter implements Converter{
    @Override
    public ByteArrayInputStream converter(MultipartFile multipartFile) throws MessageServiceException {
        try {
            // Read the input image
            BufferedImage inputImage = ImageIO.read(multipartFile.getInputStream());

            // Create a ByteArrayOutputStream to store the converted JPEG data
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Convert the input image to JPEG and store it in the outputStream
            ImageIO.write(inputImage, "JPEG", outputStream);

            // Return the converted JPEG data as a ByteArrayInputStream
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new MessageServiceException(Status.UNKNOWN_ERROR);
        }
    }
}
