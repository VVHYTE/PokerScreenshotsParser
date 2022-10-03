package controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import model.BufferedImageWrapper;
import model.repository.implementation.ResourceRepositoryImpl;

public class Controller {

    private final ResourceRepositoryImpl repository = new ResourceRepositoryImpl();

    @SuppressWarnings("resource")
    public void start(String[] args) {
        try {
            Files.walk(Paths.get(args[0]))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".png"))
                    .forEach(this::parse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parse(Path path) {
        List<BufferedImageWrapper> ranks;
        List<BufferedImageWrapper> suits;

        try {
            ranks = repository.getRanks();
            suits = repository.getSuits();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read an image by given path.", e);
        }

        int[][][] rankRegions = repository.getRangRegions();
        int[][][] suitRegions = repository.getSuitRegions();


        BufferedImage image;

        try {
            image = ImageIO.read(path.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read an image by given path.", e);
        }

        String result = path.getFileName().toString() + " - ";

        for (int i = 0; i < 5; i++) {

            BufferedImage rankImage = cropImage(image, rankRegions[i]);
            BufferedImage suitImage = cropImage(image, suitRegions[i]);

            int background = image.getRGB(rankRegions[i][0][0], rankRegions[i][0][1]);

            if (background != -1 && background != -8882056) {
                break;
            }

            clean(rankImage);
            clean(suitImage);

            Optional<BufferedImageWrapper> currentRank = match(rankImage, ranks);
            Optional<BufferedImageWrapper> currentSuit = match(suitImage, suits);

            if (currentRank.isPresent()) {
                result = result + currentRank.get().name();
            }
            if (currentSuit.isPresent()) {
                result = result + currentSuit.get().name();
            }
        }

        System.out.println(result);
    }

    private static BufferedImage clean(BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();

        int white = Color.WHITE.getRGB();
        int black = Color.BLACK.getRGB();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                int color = image.getRGB(col, row);

                if (color == -8882056) {
                    image.setRGB(col, row, white);
                } else if (color != -1) {
                    image.setRGB(col, row, black);
                }
            }
        }

        return image;
    }

    private static double getDifference(BufferedImage sample, BufferedImage original) {
        int originalWidth = original.getWidth();
        int originalHeight = original.getHeight();

        int sampleWidth = sample.getWidth();
        int sampleHeight = sample.getHeight();

        if (sampleWidth != originalWidth || sampleHeight != originalHeight) {
            return 100;
        }

        int totalPixels = originalHeight * originalWidth;
        int matchedPixels = 0;

        for (int row = 0; row < originalHeight; row++) {
            for (int col = 0; col < originalWidth; col++) {
                int originalColor = original.getRGB(col, row);
                int sampleColor = sample.getRGB(col, row);

                if (originalColor == sampleColor) {
                    matchedPixels++;
                }
            }
        }

        return 100 - (((double) matchedPixels / (double) totalPixels) * 100);
    }

    private static Optional<BufferedImageWrapper> match(BufferedImage sample, List<BufferedImageWrapper> originals) {

        Optional<BufferedImageWrapper> result = Optional.empty();
        double limit = 100;

        for (BufferedImageWrapper bufferedImageWrapper : originals) {
            double difference = getDifference(sample, bufferedImageWrapper.original());

            if (difference < limit) {
                limit = difference;
                result = Optional.of(bufferedImageWrapper);
            }
        }

        return result;
    }

    private static BufferedImage cropImage(BufferedImage image, int[][] areaCoordinates) {
        int x1Point = areaCoordinates[0][0];
        int y1Point = areaCoordinates[0][1];

        int x2Point = areaCoordinates[1][0];
        int y2Point = areaCoordinates[1][1];

        return image.getSubimage(x1Point, y1Point, x2Point - x1Point + 1, y2Point - y1Point + 1);
    }

}
