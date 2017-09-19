package main.extra;

/**
 * Created by ������ on 15.09.2017.
 */
public class ForCirculating {

    public static int count = 2;

    private boolean pixelIsNotEmpty;
    private boolean pixelTrue;
    private boolean pixelProcessed;

    private int pixelEntersAnotherPath;

    public ForCirculating() {
    }

    public ForCirculating(boolean pixelIsNotEmpty, boolean pixelProcessed, int pixelEntersAnotherPath) {

        this.pixelIsNotEmpty = pixelIsNotEmpty;
        this.pixelProcessed = pixelProcessed;
        this.pixelTrue = false;
        this.pixelEntersAnotherPath = pixelEntersAnotherPath;
    }

    public boolean isPixelIsNotEmpty() {
        return pixelIsNotEmpty;
    }
    public void setPixelIsNotEmpty(boolean pixelIsNotEmpty) {
        this.pixelIsNotEmpty = pixelIsNotEmpty;
    }

    public boolean isPixelProcessed() {
        return pixelProcessed;
    }
    public void setPixelProcessed(boolean pixelProcessed) {
        this.pixelProcessed = pixelProcessed;
    }

    public int getPixelEntersAnotherPath() {
        return pixelEntersAnotherPath;
    }
    public void setPixelEntersAnotherPath(int pixelEntersAnotherPath) {
        this.pixelEntersAnotherPath = pixelEntersAnotherPath;
    }

    public boolean isPixelTrue() {
        return pixelTrue;
    }
    public void setPixelTrue(boolean pixelTrue) {
        this.pixelTrue = pixelTrue;
    }
}
