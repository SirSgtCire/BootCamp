package com.learning;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Imaghur {
    protected static final Config properties = new Config(System.getProperty("prop.file", "src/main/resources/config.properties"));
    private static final Logger logger = LoggerFactory.getLogger(Imaghur.class);

    public static void main(String[] args) {
        String inputFilePath = "src/main/resources/input.png";
        String outputFilePath = "src/main/resources/output.png";
        boolean sortDescending = true;
        try {
            processImage(inputFilePath, outputFilePath, sortDescending);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void processImage(String inputFile, String outputFile, boolean sortDescending) throws Exception {
        // Analyze input image and count colors
        logger.info("Identify how many colors we find in our input image.\n");
        Map<Color, Integer> colorsInImage = analyzeColors(inputFile);

        // Sort colors by count in either ascending or descending order
        logger.info("Sort colors in descending or ascending order by pixel count.\n");
        List<Map.Entry<Color, Integer>> sortedColors = new ArrayList<>(colorsInImage.entrySet());
        sortedColors.sort(sortDescending ? Map.Entry.comparingByValue(Comparator.reverseOrder()) : Map.Entry.comparingByValue());

        // Create output image
        logger.info("Create new image file for output.\n");
        createOutputImage(inputFile, outputFile);

        // Gather a varying number of connected pixels based on the color percentage provided
        logger.info("Given descending order is set to {}, we gather a varying number of connected pixels to color in.\n", sortDescending);
        for (Map.Entry<Color, Integer> givenColor : sortedColors) {
            // Gather pixel and its neighbors in one list
            BufferedImage outputImage = ImageIO.read(new File(outputFile));
            ArrayList<Point> pixelsToPaint = gatherPixelNeighbors(givenColor.getValue(), outputImage);

            // Now paint each gathered pixel the given color
            logger.info("Paint color {} onto the new image given descending order is set to {}.\n", givenColor, sortDescending);
            Color color = givenColor.getKey();
            for (Point q : pixelsToPaint) {
                outputImage.setRGB(q.x, q.y, color.getRGB());
            }

            // Save output image, since we are creating Art
            logger.info("Save generated image to {}.\n", outputFile);
            File Art = new File(outputFile);
            ImageIO.write(outputImage, "png", Art);
        }

        logger.info("We have completed processing of {} and have saved our work to {}.\n", inputFile, outputFile);
    }

    public static Color reduceColors(Color foundColor) {
        // NOTE: ONLY uses values 0, 51, 102, 153, 204, 255 for red, green, blue
        // Create RGB values to store updated RGB values
        int r = (((foundColor.getRed() * 6) / 256) * 51);
        int g = (((foundColor.getGreen() * 6) / 256) * 51);
        int b = (((foundColor.getBlue() * 6) / 256) * 51);

        // Create color variable containing reduced colors
        return new Color(r,g,b);
    }

    public static Map<Color, Integer> analyzeColors(String imageInput) throws Exception {
        logger.info("The image we were given and will analyze: {}\n", imageInput);
        Map<Color, Integer> colorCounts = new HashMap<>();
        BufferedImage inputImage = ImageIO.read(new File(imageInput));
        int imageWidth = inputImage.getWidth();
        int imageHeight = inputImage.getHeight();

        // For each pixel, we reduce its color to a more common color and see if that color is already accounted for
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                Color temperedPixelColor = reduceColors(new Color(inputImage.getRGB(x, y)));
                if (!colorCounts.containsKey(temperedPixelColor)) {
                    colorCounts.put(temperedPixelColor, 1);
                } else {
                    colorCounts.put(temperedPixelColor, colorCounts.get(temperedPixelColor) + 1);
                }
            }
        }
        logger.info("We found a total of {} colors in our input image.\n", colorCounts.size());
        return colorCounts;
    }

    public static void createOutputImage(String imageInput, String imageOutput) throws Exception {
        // Identify input image variables
        BufferedImage inputImage = ImageIO.read(new File(imageInput));
        int imageWidth = inputImage.getWidth();
        int imageHeight = inputImage.getHeight();

        // Create output image based on input size, and color it all white
        BufferedImage outputImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, imageWidth, imageHeight);

        // Save the created output image to the given output location
        File Canvas = new File(imageOutput);
        ImageIO.write(outputImage, "png", Canvas);
    }



    public static ArrayList<Point> gatherPixelNeighbors(Integer threshold, BufferedImage imageTemplate) {
        // Create list containing initial pixel and its immediate neighbors
        ArrayList<Point> pixelNeighbors = new ArrayList<>();
        int gatheredPixels = 0;
        int neighbourDistance = 1;

        // Pick a random pixel from the input image
        int x1 = (int) (Math.random() * imageTemplate.getWidth());
        int y1 = (int) (Math.random() * imageTemplate.getHeight());
        Point point = new Point(x1, y1);
        while (imageTemplate.getRGB(point.x, point.y) != Color.WHITE.getRGB()) {
            logger.info("Recreating point {} since we found it already added to pixelTracker", point);
            x1 = (int) (Math.random() * imageTemplate.getWidth());
            y1 = (int) (Math.random() * imageTemplate.getHeight());
            point = new Point(x1, y1);
        }

        // Add original point to our neighbors list, since the point is a neighbor to itself
        pixelNeighbors.add(point);
        logger.info("Added pixel {} to our list of neighbors to paint.\n", point);

        // Add pixels to neighbors list if adjacent to original pixel and not already in the list until we reach our threshold
        while (gatheredPixels < threshold) {
            // pixr = pixel to the right of point
            Point pixr = new Point(point.x+neighbourDistance, point.y);
            if (pixr.x >= 0 && pixr.x < imageTemplate.getWidth() && pixr.y >= 0 && pixr.y < imageTemplate.getHeight()
                    && !pixelNeighbors.contains(pixr) && (imageTemplate.getRGB(pixr.x, pixr.y) == Color.WHITE.getRGB())) {
                pixelNeighbors.add(pixr);
                gatheredPixels++;
            }
            // pixl = pixel to the left of point
            Point pixl = new Point(point.x-neighbourDistance, point.y);
            if (pixl.x >= 0 && pixl.x < imageTemplate.getWidth() && pixl.y >= 0 && pixl.y < imageTemplate.getHeight()
                    && !pixelNeighbors.contains(pixl) && (imageTemplate.getRGB(pixl.x, pixl.y) == Color.WHITE.getRGB())) {
                pixelNeighbors.add(pixl);
                gatheredPixels++;
            }
            // pixu = pixel above the point
            Point pixu = new Point(point.x, point.y+neighbourDistance);
            if (pixu.x >= 0 && pixu.x < imageTemplate.getWidth() && pixu.y >= 0 && pixu.y < imageTemplate.getHeight()
                    && !pixelNeighbors.contains(pixu) && (imageTemplate.getRGB(pixu.x, pixu.y) == Color.WHITE.getRGB())) {
                pixelNeighbors.add(pixu);
                gatheredPixels++;
            }
            // pixd = pixel below the point
            Point pixd = new Point(point.x, point.y-neighbourDistance);
            if (pixd.x >= 0 && pixd.x < imageTemplate.getWidth() && pixd.y >= 0 && pixd.y < imageTemplate.getHeight()
                    && !pixelNeighbors.contains(pixd) && (imageTemplate.getRGB(pixd.x, pixd.y) == Color.WHITE.getRGB())) {
                pixelNeighbors.add(pixd);
                gatheredPixels++;
            }
            // pixur = pixel to the upper right of point
            Point pixur = new Point(point.x+neighbourDistance, point.y+neighbourDistance);
            if (pixur.x >= 0 && pixur.x < imageTemplate.getWidth() && pixur.y >= 0 && pixur.y < imageTemplate.getHeight()
                    && !pixelNeighbors.contains(pixur) && (imageTemplate.getRGB(pixur.x, pixur.y) == Color.WHITE.getRGB())) {
                pixelNeighbors.add(pixur);
                gatheredPixels++;
            }
            // pixul = pixel to the upper left of point
            Point pixul = new Point(point.x+neighbourDistance, point.y-neighbourDistance);
            if (pixul.x >= 0 && pixul.x < imageTemplate.getWidth() && pixul.y >= 0 && pixul.y < imageTemplate.getHeight()
                    && !pixelNeighbors.contains(pixul) && (imageTemplate.getRGB(pixul.x, pixul.y) == Color.WHITE.getRGB())) {
                pixelNeighbors.add(pixul);
                gatheredPixels++;
            }
            // pixdr = pixel to the lower right of point
            Point pixdr = new Point(point.x-neighbourDistance, point.y+neighbourDistance);
            if (pixdr.x >= 0 && pixdr.x < imageTemplate.getWidth() && pixdr.y >= 0 && pixdr.y < imageTemplate.getHeight()
                    && !pixelNeighbors.contains(pixdr) && (imageTemplate.getRGB(pixdr.x, pixdr.y) == Color.WHITE.getRGB())) {
                pixelNeighbors.add(pixdr);
                gatheredPixels++;
            }
            // pixdl = pixel to the lower left of point
            Point pixdl = new Point(point.x-neighbourDistance, point.y-neighbourDistance);
            if (pixdl.x >= 0 && pixdl.x < imageTemplate.getWidth() && pixdl.y >= 0 && pixdl.y < imageTemplate.getHeight()
                    && !pixelNeighbors.contains(pixdl) && (imageTemplate.getRGB(pixdl.x, pixdl.y) == Color.WHITE.getRGB())) {
                pixelNeighbors.add(pixdl);
                gatheredPixels++;
            }
            neighbourDistance++;
        }

        logger.info("We reached a blob radius of {} pixels, in which contains {} number of pixels.\n",
                neighbourDistance, pixelNeighbors.size());
        return pixelNeighbors;
    }
}


