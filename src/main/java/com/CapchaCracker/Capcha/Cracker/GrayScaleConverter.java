package com.CapchaCracker.Capcha.Cracker;

import net.coobird.thumbnailator.Thumbnails;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GrayScaleConverter {

    public static void main(String... args) {

        try {
            File folder = new File("D:\\Capcha\\newSources");
            File[] listOfFolders = folder.listFiles();
            for (File parentFolder : listOfFolders) {
                folder = new File(parentFolder.getPath());
                System.out.println("Folder: "+folder.getName());
                String directory = folder.getName();
                File[] listOfFiles = folder.listFiles();

                for (File currentFile : listOfFiles) {
                    BufferedImage image = ImageIO.read(currentFile);
                    image = blackWhite(image);
                    crop(image, directory.charAt(0), directory.charAt(1), directory.charAt(2));
                }
            }
        } catch (IOException e) {
        e.printStackTrace();
    }
    }

        public static BufferedImage greyScale(BufferedImage image) {
            BufferedImage result = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_INT_RGB);

            Graphics2D graphic = result.createGraphics();
            graphic.drawImage(image, 0, 0, Color.WHITE, null);

            for (int i = 0; i < result.getHeight(); i++) {
                for (int j = 0; j < result.getWidth(); j++) {
                    Color c = new Color(result.getRGB(j, i));
                    int red = (int) (c.getRed() * 0.299);
                    int green = (int) (c.getGreen() * 0.587);
                    int blue = (int) (c.getBlue() * 0.114);
                    Color newColor = new Color(
                            red + green + blue,
                            red + green + blue,
                            red + green + blue);
                    result.setRGB(j, i, newColor.getRGB());
                }
            }
    return result;
    }

    public static BufferedImage blackWhite(BufferedImage image) {
        BufferedImage result = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        Graphics2D graphic = result.createGraphics();
        graphic.drawImage(image, 0, 0, Color.WHITE, null);
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(new Color(58,49,49));
        colors.add(new Color(90,90,82));
        colors.add(new Color(99,90,90));
        colors.add(new Color(82,74,58));
        colors.add(new Color( 115,115,107));
        colors.add(new Color(148,148,140));
        colors.add(new Color( 132,123,123));

        for (int i = 0; i < result.getHeight(); i++) {
            for (int j = 0; j < result.getWidth(); j++) {
                Color c = new Color(result.getRGB(j, i));
                boolean blackened = false;
                for (Color currentColor : colors) {
                    if (c.getRGB() == currentColor.getRGB()) {
                        result.setRGB(j, i, Color.BLACK.getRGB());
                        blackened = true;
                    }
                }
                if (!blackened)
                    result.setRGB(j, i, Color.WHITE.getRGB());
            }
        }
        return result;
    }


    public static void crop(BufferedImage image, char a, char b, char c) throws IOException {

            BufferedImage resultA = cropIt(image, 62, 90);
            resultA = resize(resultA, 20, 20);
            File output = new File("D:\\Capcha\\done\\"+a+"_"+getRandomInt(1000,9999)+".jpg");
            ImageIO.write(resultA, "jpg", output);


            BufferedImage resultB = cropIt(image, 62, 218);
            resultB = resize(resultB, 20, 20);
            output = new File("D:\\Capcha\\done\\"+b+"_"+getRandomInt(1000,9999)+".jpg");
            ImageIO.write(resultB, "jpg", output);


            BufferedImage resultC = cropIt(image, 62, 346);
            resultC = resize(resultC, 20, 20);
            output = new File("D:\\Capcha\\done\\"+c+"_"+getRandomInt(1000,9999)+".jpg");
            ImageIO.write(resultC, "jpg", output);

    }

    public static double getRandomInt(double min, double max){
        double x = (int)(Math.random()*((max-min)+1))+min;
        return x;
    }
    public static BufferedImage resize(BufferedImage img, int newW, int newH) throws IOException {
        return Thumbnails.of(img).size(newW, newH).asBufferedImage();
    }

    public static BufferedImage cropIt(BufferedImage originalImage, int xc, int yc) throws IOException {
        int targetWidth = 90;
        int targetHeight = 90;
        // Crop
        BufferedImage croppedImage = originalImage.getSubimage(
                xc,
                yc,
                targetWidth,
                targetHeight
        );
        return croppedImage;
    }

    public static double getDiff(BufferedImage img1, BufferedImage img2, int size){
        int width = img1.getWidth();
        int height = img1.getHeight();
        int width2 = img2.getWidth();
        int height2 = img2.getHeight();
        if (width != width2 || height != height2) {
            throw new IllegalArgumentException(String.format("Images must have the same dimensions: (%d,%d) vs. (%d,%d)", width, height, width2, height2));
        }
        long diff = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                diff += pixelDiff(img1.getRGB(x, y), img2.getRGB(x, y));
            }
        }
        long maxDiff = 3L * 255 * width * height;
        return 100.0 * diff / maxDiff;
    }

    private static int pixelDiff(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xff;
        int g1 = (rgb1 >>  8) & 0xff;
        int b1 =  rgb1        & 0xff;
        int r2 = (rgb2 >> 16) & 0xff;
        int g2 = (rgb2 >>  8) & 0xff;
        int b2 =  rgb2        & 0xff;
        return Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
    }
    }

