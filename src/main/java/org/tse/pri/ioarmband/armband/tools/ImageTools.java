package org.tse.pri.ioarmband.armband.tools;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class ImageTools {
	
	private static final Logger logger = Logger.getLogger(ImageTools.class);
	
	public static String encodeBase64(Image image, String imageType){
		String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write((RenderedImage) image, imageType, bos);
            byte[] imageBytes = bos.toByteArray();
            imageString = Base64.encodeBase64String(imageBytes);

            bos.close();
        } catch (IOException e) {
        	logger.error("encodeBase64() : Unable to encode image.", e);
        }
        return imageString;
	}

	public static Image decodeBase64(String base64String){
		BufferedImage image = null;
		byte[] imageByte;
		try {
			imageByte = Base64.decodeBase64(base64String);
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			image = ImageIO.read(bis);
			bis.close();
		} catch (Exception e) {
        	logger.error("decodeBase64() : Unable to decode image.", e);
		}
		return image;

	}
	
	public static Image resize(Image image, int width, int height) {
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
	    Graphics2D g2d = (Graphics2D) bi.createGraphics();
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(image, 0, 0, width, height, null);
	    g2d.dispose();
	    return bi;
	}
}
