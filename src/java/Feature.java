import java.awt.image.BufferedImage;

public class Feature {

    private String name;
    private double limit;
    private BufferedImage original;

    public Feature(String name, double limit, BufferedImage original) {
        this.name = name;
        this.limit = limit;
        this.original = original;
    }

    public BufferedImage getOriginal() { return original; }

    public String getName() { return name; }
}
