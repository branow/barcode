import java.util.Arrays;

public class BarcodeImage implements Cloneable {

    //The exact internal dimensions of 2D data
    public static final int MAX_HEIGHT = 30;
    public static final int MAX_WIDTH = 65;

    //Image data storage
    private final boolean[][] imageData;

    public BarcodeImage() {
        imageData = new boolean[MAX_HEIGHT][MAX_WIDTH]; //Instantiation of a blank array
    }


    public BarcodeImage(String[] strData) {
        this();
        fillImage(strData);
    }

    //Private constructor that is used for cloning
    private BarcodeImage(boolean[][] imageData) {
        this.imageData = imageData;
    }

    //Check if given coordinates in scope of image and if true returns value
    boolean getPixel(int row, int col) {
        return validateCoordinate(row, col) && imageData[row][col];
    }

    //Check if given coordinates in scope of image and if true set the given value
    boolean setPixel(int row, int col, boolean value) {
        boolean flag = false;
        if (validateCoordinate(row, col)) {
            imageData[row][col] = value;
            flag = true;
        }
        return flag;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        boolean[][] copy = new boolean[imageData.length][];
        for (int i=0; i<imageData.length; i++)
            copy[i] = Arrays.copyOf(imageData[i], imageData[i].length);
        return new BarcodeImage(copy);
    }


    //Check if the given coordinates is in scope of this image
    private boolean validateCoordinate(int row, int col) {
        return row >= 0 && row < MAX_HEIGHT && col >=0 && col < MAX_WIDTH;
    }

    private void fillImage(String[] strImage) {
        checkSize(strImage);
        for (int i = 0; i < strImage.length; i++) {
            for (int j = 0; j < strImage[i].length(); j++) {
                imageData[MAX_HEIGHT - strImage.length + i][j] = strImage[i].charAt(j) == '*';
            }
        }
    }

    private void checkSize(String[] data) {
        if (data == null) {
            throw new NullPointerException("Data must not be null!");
        }
        if (data.length > MAX_HEIGHT || (data.length > 0 && data[0].length() > MAX_WIDTH)) {
            throw new RuntimeException("The size fo data must be less or equal to the internal dimensions of image data");
        }
    }

}