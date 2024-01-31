import java.util.ArrayList;
import java.util.List;

public class InfoBox implements BarcodeIO {

    public static final char BLACK_CHAR = '*';
    public static final char WHITE_CHAR = ' ';

    //a single internal copy of any image
    private BarcodeImage image;
    //a single internal copy of any text
    private String text;
    //actual portion of the BarcodeImage that has the real signal
    private int actualWidth, actualHeight;


    public InfoBox() {
        this.image = new BarcodeImage();
        text = "";
        actualHeight = 0;
        actualWidth = 0;
    }

    public InfoBox(BarcodeImage image) {
        this();
        scan(image);
    }

    public InfoBox(String text) {
        this();
        readText(text);
    }

    /**
     * Sets image value and actual computed height and width
     * */
    @Override
    public boolean scan(BarcodeImage bc) {
        try {
            image = (BarcodeImage) bc.clone();
        } catch (CloneNotSupportedException e) {
            return false;
        }
        actualHeight = computeSignalHeight();
        actualWidth = computeSignalWidth();
        return true;
    }

    @Override
    public boolean readText(String text) {
        boolean flag = false;
        if (text != null) {
            this.text = text;
            flag = true;
        }
        return flag;
    }

    /**
     * Generates image according to this text
     * */
    @Override
    public boolean generateImageFromText() {
        if (text == null)
            return false;
        clearImage();
        writeCharToCol(0, 255); //fill first column with black values (left border)
        for (int j = 0; j < text.length(); j++) {
            writeCharToCol(j + 1, text.charAt(j));
        }
        writeCharToCol(text.length() + 1, 85); //fill last column as dashed line (right border)
        actualHeight = computeSignalHeight();
        actualWidth = computeSignalWidth();
        return true;
    }

    /**
     * Translates this text to the image
     * */
    @Override
    public boolean translateImageToText() {
        char[] chars = new char[actualWidth - 2]; //-2 because image has top and bottom border
        for (int j = 0; j < chars.length; j++) {
            chars[j] = readCharFromCol(j + 1);
        }
        readText(String.valueOf(chars));
        return true;
    }

    @Override
    public void displayTextToConsole() {
        System.out.println(text);
    }

    /**
     * Displays only the relevant portion of the image,
     * clipping the excess blank/white from the top and right.
     * Also, show a border.
     */
    @Override
    public void displayImageToConsole() {
        List<String> lines = new ArrayList<>();
        int borderWidth = actualWidth + 2;

        lines.add("-".repeat(borderWidth)); //add top border
        int startOfImageData = BarcodeImage.MAX_HEIGHT - actualHeight;
        for (int i = startOfImageData; i < BarcodeImage.MAX_HEIGHT; i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < actualWidth; j++) {
                char c = image.getPixel(i, j) ? BLACK_CHAR : WHITE_CHAR;
                line.append(c);
            }
            lines.add('|' + line.toString() + "|");
        }
        lines.add("-".repeat(borderWidth)); //add bottom border
        System.out.println(String.join("\n", lines));
    }

    public int getActualWidth() {
        return actualWidth;
    }

    public int getActualHeight() {
        return actualHeight;
    }

    /**
     * Reads every bit from the given image column and decrypt it into char value.
     */
    private char readCharFromCol(int col) {
        StringBuilder sb = new StringBuilder();
        int startOfImageData = BarcodeImage.MAX_HEIGHT - actualHeight + 1; //+1 to avoid reading top border
        for (int i = startOfImageData; i < BarcodeImage.MAX_HEIGHT - 1; i++) { //-1 to avoid reading bottom border
            if (image.getPixel(i, col)) {
                sb.append(1);
            } else {
                sb.append(0);
            }
        }
        int val = Integer.valueOf(sb.toString(), 2);
        return (char) val;
    }

    /**
     * Crypts the given value into binary string and set it to the given image column.
     */
    private boolean writeCharToCol(int col, int code) {
        String binary = Integer.toBinaryString(code);
        String topBorder = (col % 2 == 0 ? "1" : "0");
        String innerCode = "0".repeat(8 - binary.length()) + binary;
        String barcode =  topBorder + innerCode + "1"; //"1" - the bottom border
        for (int i = 0; i < barcode.length(); i++) {
            int row = BarcodeImage.MAX_HEIGHT - 10 + i; //10 consists of 8 bit that codes char and 2 bits for border
            image.setPixel(row, col, barcode.charAt(i) == '1');
        }
        return true;
    }

    //Determine the actual width of image using the "spine" of the array
    private int computeSignalWidth() {
        int width = 0;
        int i = BarcodeImage.MAX_HEIGHT - 1;
        for (int j = 0; j < BarcodeImage.MAX_WIDTH; j++) {
            if (!image.getPixel(i, j)) {
                width = j;
                break;
            }
        }
        return width;
    }

    //Determine the actual height of image using the "spine" of the array
    private int computeSignalHeight() {
        int height = 0;
        int j = 0;
        for (int i = BarcodeImage.MAX_HEIGHT - 1; i >= 0; i--) {
            if (!image.getPixel(i, j)) {
                height = BarcodeImage.MAX_HEIGHT - i;
                break;
            }
        }
        return height - 1;
    }


    private void clearImage() {
        for (int i = 0; i < BarcodeImage.MAX_HEIGHT; i++) {
            for (int j = 0; j < BarcodeImage.MAX_WIDTH; j++) {
                image.setPixel(i, j, false);
            }
        }
    }

}
