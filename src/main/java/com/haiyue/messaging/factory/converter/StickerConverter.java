package com.haiyue.messaging.factory.converter;

import com.haiyue.messaging.enums.Status;
import com.haiyue.messaging.exception.MessageServiceException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;

public class StickerConverter implements Converter{
    @Override
    public ByteArrayInputStream converter(MultipartFile multipartFile) throws MessageServiceException {
        try {
            // Read the input image
            BufferedImage inputImage = ImageIO.read(multipartFile.getInputStream());

            // Create a ByteArrayOutputStream to store the converted GIF data
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Obtain a GIF writer
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("GIF");
            ImageWriter writer = writers.next();

            // Configure the GIF output
            ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream);
            writer.setOutput(ios);

            // Write the input image to GIF format
            writer.write(inputImage);

            // Cleanup resources
            ios.close();
            writer.dispose();

            // Return the converted GIF data as a ByteArrayInputStream
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (Exception e) {
            throw new MessageServiceException(Status.UNKNOWN_ERROR);
        }
    }
}
