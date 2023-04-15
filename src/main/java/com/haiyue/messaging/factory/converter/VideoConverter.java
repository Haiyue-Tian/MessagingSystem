package com.haiyue.messaging.factory.converter;

import com.haiyue.messaging.enums.Status;
import com.haiyue.messaging.exception.MessageServiceException;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class VideoConverter implements Converter {
    @Override
    public ByteArrayInputStream converter(MultipartFile multipartFile) throws MessageServiceException{
        try {
            // Create a new FFmpegFrameGrabber using the input file
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(multipartFile.getInputStream());

            // Create a new ByteArrayOutputStream to store the converted video data
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Initialize and start the grabber
            grabber.start();

            // Create a new FFmpegFrameRecorder using the outputStream
            FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(
                    outputStream,
                    grabber.getImageWidth(),
                    grabber.getImageHeight(),
                    grabber.getAudioChannels());

            // Configure the recorder settings
            recorder.setFormat("mp4");
            recorder.setFrameRate(grabber.getFrameRate());
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
            recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
            recorder.setSampleRate(grabber.getSampleRate());

            // Initialize and start the recorder
            recorder.start();

            // Process each frame
            Frame frame;
            while ((frame = grabber.grab()) != null) {
                recorder.record(frame);
            }

            // Stop and release the grabber and recorder
            recorder.stop();
            recorder.release();
            grabber.stop();
            grabber.release();

            // Return the converted video data as an InputStream
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (Exception e) {
            throw new MessageServiceException(Status.UNKNOWN_ERROR);
        }
    }
}
