package vn.com.ids.myachef.business.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core.IplImage;
//import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.jcodec.api.FrameGrab;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VideoThumbnailUtils {

    public static void createThumbnailFromVideo(String videoPath, String imagePath) {

        try {
            FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(videoPath);
            frameGrabber.start();
            frameGrabber.setFrameNumber(50);
        } catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
            log.error(e.getMessage(), e);
        }
        // Frame frame;
        // try {
        // Java2DFrameConverter converter = new Java2DFrameConverter();
        // frame = frameGrabber.grab();
        // BufferedImage bufferedImage = converter.convert(frame);
        // ImageIO.write(bufferedImage, "png", new File(imagePath));
        // } catch (IOException ex) {
        // log.error(ex.getMessage(), ex);
        // } finally {
        // frameGrabber.stop();
        // frameGrabber.close();
        // }

        log.info("Create thumnail Done");
    }

    public static void createThumbnailFromVideoV2(String videoPath, String imagePath) throws org.bytedeco.javacv.FrameGrabber.Exception {
        // Applied only javacv -> version 0.8
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(videoPath);
        frameGrabber.start();
        frameGrabber.setFrameNumber(50);
        File thumbnail = new File(imagePath);
        try {
            IplImage iplImage = frameGrabber.grab();
            BufferedImage bufferedImage = iplImage.getBufferedImage();
            ImageIO.write(bufferedImage, "png", thumbnail);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
        frameGrabber.stop();

        log.info("Create thumnail Done");
    }

    public static void createThumbnailFromVideoV3(String videoPath, String imagePath) throws org.bytedeco.javacv.FrameGrabber.Exception {
        final int frameNumber = 0;
        try {
            Picture picture = FrameGrab.getFrameFromFile(new File(videoPath), frameNumber);
            BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
            ImageIO.write(bufferedImage, "png", new File(imagePath));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        log.info("Create thumnail Done");
    }

    public static void main(String[] args) throws Exception, IOException {

        String videoPath = "C:\\Users\\khoa.nguyen\\Desktop\\images\\video_1.mp4";
        String imagePath = "C:\\Users\\khoa.nguyen\\Desktop\\images\\New folder\\video-frame-" + System.currentTimeMillis() + ".png";

        createThumbnailFromVideoV2(videoPath, imagePath);

    }
}