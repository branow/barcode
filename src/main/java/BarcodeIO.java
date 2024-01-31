public interface BarcodeIO {

    boolean scan(BarcodeImage bc);
    boolean readText(String text);
    boolean generateImageFromText();
    boolean translateImageToText();
    void displayTextToConsole();
    void displayImageToConsole();

}
