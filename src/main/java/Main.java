public class Main {

    public static void main(String[] args) {
        String[] sImageIn =
                {
                        "* * * * * * * * * * * * * * *",
                        "*                           *",
                        "**********  *** *** *******  ",
                        "* ***************************",
                        "**    * *   * *  *   * *     ",
                        "* **     ** **          **  *",
                        "****** ****  **   *  ** ***  ",
                        "****  **     *   *   * **   *",
                        "***  *  *   *** * * ******** ",
                        "*****************************"
                };

        String[] sImageIn_2 =
                {
                        "* * * * * * * * * * * * * * *",
                        "*                           *",
                        "*** ** ******** ** ***** *** ",
                        "*  **** ***************** ***",
                        "* *  *    *      *  *  *  *  ",
                        "*       ** **** *          **",
                        "*    * ****  **    * * * *** ",
                        "***    ***       * **    * **",
                        "*** *   **  *   ** * **   *  ",
                        "*****************************"
                };



        BarcodeImage bc = new BarcodeImage(sImageIn);
        InfoBox dm = new InfoBox(bc);

        // First secret message
        dm.translateImageToText();
        dm.displayTextToConsole();
        dm.displayImageToConsole();


        // second secret message
        bc = new BarcodeImage(sImageIn_2);
        dm.scan(bc);
        dm.translateImageToText();
        dm.displayTextToConsole();
        dm.displayImageToConsole();

        // create your own message
        dm.readText("What a great resume builder this is!");
        dm.generateImageFromText();
        dm.displayTextToConsole();
        dm.displayImageToConsole();

    }
}
