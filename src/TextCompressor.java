/******************************************************************************
 *  Compilation:  javac TextCompressor.java
 *  Execution:    java TextCompressor - < input.txt   (compress)
 *  Execution:    java TextCompressor + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   abra.txt
 *                jabberwocky.txt
 *                shakespeare.txt
 *                virus.txt
 *
 *  % java DumpBinary 0 < abra.txt
 *  136 bits
 *
 *  % java TextCompressor - < abra.txt | java DumpBinary 0
 *  104 bits    (when using 8-bit codes)
 *
 *  % java DumpBinary 0 < alice.txt
 *  1104064 bits
 *  % java TextCompressor - < alice.txt | java DumpBinary 0
 *  480760 bits
 *  = 43.54% compression ratio!
 ******************************************************************************/

/**
 *  The {@code TextCompressor} class provides static methods for compressing
 *  and expanding natural language through textfile input.
 *
 *  @author Zach Blick, Benjamin Chock
 */
public class TextCompressor {

    //magic numbers
    private static final int MAXSIZE = 16384;
    private static final int EOF = 256;
    private static final int BITSIZE = 14;
    private static void compress() {
        String toBeIn = "";
        char ascii;
        // TODO: Complete the compress() method
        TST codes = new TST();
        //add all the characters in extended ascii to tst
        for (int i = 0; i < 256; i++){
            ascii = (char) i;
            toBeIn += ascii;
            codes.insert(toBeIn, i);
            toBeIn = "";
        }
        //read in the text meant to be compressed
        String text = BinaryStdIn.readString();
        String currentString = "";
        int code;
        int position = 0;
        int newCodes = 257;
        //while there is text to be compressed
        while (position < text.length()){
            //get the longest prefix
            currentString = codes.getLongestPrefix(text, position);
            //get the code asociated with the longest prefix
            code = codes.lookup(currentString);
            //if there is still space in my tst and it's not the last character in the string add it to my codes
            if (newCodes < MAXSIZE && position+currentString.length() < text.length()){
                codes.insert(currentString + text.charAt(position + currentString.length()), newCodes);
                newCodes++;

            }
            //write out the code of the string
            BinaryStdOut.write(code, BITSIZE);
            //move to the next character
            position += currentString.length();
        }
        //indicate the file is ended
        BinaryStdOut.write(EOF, BITSIZE);

        BinaryStdOut.close();
    }

    private static void expand() {
        // TODO: Complete the expand() method
        int code;
        String strCode;
        String nextLet;
        String[] codes = new String[MAXSIZE];
        char ascii;
        int oldCodes = 257;
        String toBeIn = "";
        int nextCode;
        //add all of extended ascii to map
        for (int i = 0; i < 256; i++){
            ascii = (char) i;
            toBeIn += ascii;
            codes[i] = toBeIn;
            toBeIn = "";
        }
        //read in the first code
        code = BinaryStdIn.readInt(BITSIZE);
        String lookAhead = "";
        //while we haven't reached end of file keep taking in new codes
        while (code != EOF){
            //get the string that corresponds to the code
            strCode = codes[code];
            //write out that string
            BinaryStdOut.write(strCode);
            //get the next code so we can start to add the letter to make the tst codes for the map
            nextCode = BinaryStdIn.readInt(BITSIZE);
            //if the next code isn't the end of the file
            if (nextCode != EOF) {
                //edge case: if next code is being made in thsi run we know that next code is just current string + it's first char
                if (oldCodes == nextCode){
                    lookAhead = codes[oldCodes] = strCode + strCode.charAt(0);
                }
                //otherwise set look ahead as currentstring + next strings first letter
                else{
                    nextLet = codes[nextCode];
                    lookAhead = strCode + nextLet.charAt(0);
                }

            }
            //only if codes is smaller than maxsize then add to map
            if (oldCodes < MAXSIZE){
                codes[oldCodes] = lookAhead;
                oldCodes++;
            }
            //reset lookahead
            lookAhead = "";
            //get the next code
            code = nextCode;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
