import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

public class Main {

    private static List<BufferedImageWrapper> ranks;
    private static List<BufferedImageWrapper> suits;

    private static int[][][] rankRegions;
    private static int[][][] suitRegions;

    public static void main(String[] args) throws Exception {

        ranks = new ArrayList<>();
        suits = new ArrayList<>();

        File resources = new File(Main.class.getClassLoader().getResource(".").getFile());

        ranks.add(new BufferedImageWrapper("A", ImageIO.read(new File(resources, "ranks/A.png"))));
        ranks.add(new BufferedImageWrapper("2", ImageIO.read(new File(resources, "ranks/2.png"))));
        ranks.add(new BufferedImageWrapper("3", ImageIO.read(new File(resources, "ranks/3.png"))));
        ranks.add(new BufferedImageWrapper("4", ImageIO.read(new File(resources, "ranks/4.png"))));
        ranks.add(new BufferedImageWrapper("5", ImageIO.read(new File(resources, "ranks/5.png"))));
        ranks.add(new BufferedImageWrapper("6", ImageIO.read(new File(resources, "ranks/6.png"))));
        ranks.add(new BufferedImageWrapper("7", ImageIO.read(new File(resources, "ranks/7.png"))));
        ranks.add(new BufferedImageWrapper("8", ImageIO.read(new File(resources, "ranks/8.png"))));
        ranks.add(new BufferedImageWrapper("9", ImageIO.read(new File(resources, "ranks/9.png"))));
        ranks.add(new BufferedImageWrapper("10", ImageIO.read(new File(resources, "ranks/10.png"))));
        ranks.add(new BufferedImageWrapper("J", ImageIO.read(new File(resources, "ranks/J.png"))));
        ranks.add(new BufferedImageWrapper("Q", ImageIO.read(new File(resources, "ranks/Q.png"))));
        ranks.add(new BufferedImageWrapper("K", ImageIO.read(new File(resources, "ranks/K.png"))));
        suits.add(new BufferedImageWrapper("c", ImageIO.read(new File(resources, "suits/c.png"))));
        suits.add(new BufferedImageWrapper("d", ImageIO.read(new File(resources, "suits/d.png"))));
        suits.add(new BufferedImageWrapper("h", ImageIO.read(new File(resources, "suits/h.png"))));
        suits.add(new BufferedImageWrapper("s", ImageIO.read(new File(resources, "suits/s.png"))));

        rankRegions = new int[][][]{
                {{148, 591}, {177, 615}},
                {{219, 591}, {248, 615}},
                {{291, 591}, {320, 615}},
                {{362, 591}, {391, 615}},
                {{434, 591}, {463, 615}}
        };

        suitRegions = new int[][][]{
                {{169, 634}, {201, 667}},
                {{240, 634}, {272, 667}},
                {{312, 634}, {344, 667}},
                {{383, 634}, {415, 667}},
                {{455, 634}, {487, 667}}
        };

        Files.walk(Paths.get(args[0]))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".png"))
                .forEach(Main::parse);
    }

    private static void parse(Path path) {

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

            rankImage = clean(rankImage);
            suitImage = clean(suitImage);

            Optional<BufferedImageWrapper> currentRank = match(rankImage, ranks);
            Optional<BufferedImageWrapper> currentSuit = match(suitImage, suits);

            if (currentRank.isPresent()) {
                result = result + currentRank.get().getName();
            }
            if (currentSuit.isPresent()) {
                result = result + currentSuit.get().getName();
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

    private static BufferedImage cropImage(BufferedImage image, int[][] areaCoordinates) {
        int x1Point = areaCoordinates[0][0];
        int y1Point = areaCoordinates[0][1];

        int x2Point = areaCoordinates[1][0];
        int y2Point = areaCoordinates[1][1];

        return image.getSubimage(x1Point, y1Point, x2Point - x1Point + 1, y2Point - y1Point + 1);
    }

    private static Optional<BufferedImageWrapper> match(BufferedImage sample, List<BufferedImageWrapper> originals) {

        Optional<BufferedImageWrapper> result = null;
        double limit = 100;

        for (int i = 0; i < originals.size(); i++) {
            BufferedImageWrapper bufferedImageWrapper = originals.get(i);
            double difference = getDifference(sample, bufferedImageWrapper.getOriginal());

            if (difference < limit) {
                limit = difference;
                result = Optional.of(bufferedImageWrapper);
            }
        }

        return result;
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
}
