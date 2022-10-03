package model.repository.implementation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;

import model.BufferedImageWrapper;
import model.repository.ResourceRepository;
import view.Main;

public class ResourceRepositoryImpl implements ResourceRepository {

    @Override
    public List<BufferedImageWrapper> getRanks() throws IOException {

        return getFileByName(new String[]{"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"});
    }

    @Override
    public List<BufferedImageWrapper> getSuits() throws IOException {

        return getFileByName(new String[]{"c", "d", "h", "s"});
    }

    private List<BufferedImageWrapper> getFileByName(final String[] namesArray) throws IOException {
        List<BufferedImageWrapper> result = new ArrayList<>();

        File resources = new File(
                Objects.requireNonNull(Main.class.getClassLoader().getResource("."))
                        .getFile()
        );

        for (String name : namesArray) {
            String path = String.format("ranks/%s.png", name);

            BufferedImageWrapper item = new BufferedImageWrapper(
                    name,
                    ImageIO.read(new File(resources, path))
            );

            result.add(item);
        }
        return result;
    }

    @Override
    public int[][][] getRangRegions() {
        return new int[][][]{
                {{148, 591}, {177, 615}},
                {{219, 591}, {248, 615}},
                {{291, 591}, {320, 615}},
                {{362, 591}, {391, 615}},
                {{434, 591}, {463, 615}}
        };
    }

    @Override
    public int[][][] getSuitRegions() {
        return new int[][][]{
                {{169, 634}, {201, 667}},
                {{240, 634}, {272, 667}},
                {{312, 634}, {344, 667}},
                {{383, 634}, {415, 667}},
                {{455, 634}, {487, 667}}
        };
    }
}
