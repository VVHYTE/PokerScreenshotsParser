package model.repository;

import java.io.IOException;
import java.util.List;

import model.BufferedImageWrapper;

public interface ResourceRepository {

    List<BufferedImageWrapper> getRanks() throws IOException;

    List<BufferedImageWrapper> getSuits() throws IOException;

    int[][][] getRangRegions();

    int[][][] getSuitRegions();
}
