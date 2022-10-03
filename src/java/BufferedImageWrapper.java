import java.awt.image.BufferedImage;

public class BufferedImageWrapper {

    private final String name;
    private final BufferedImage original;

    public BufferedImageWrapper(String name, BufferedImage original) {
        this.name = name;
        this.original = original;
    }

    public BufferedImage getOriginal() { return original; }

    public String getName() { return name; }
}
