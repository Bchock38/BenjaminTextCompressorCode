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

import java.util.ArrayList;

/**
 *  The {@code TextCompressor} class provides static methods for compressing
 *  and expanding natural language through textfile input.
 *
 *  @author Zach Blick, Benjamin Chock
 */
public class TextCompressor {

    private static void compress() {

        // TODO: Complete the compress() method
        String[] knownWords = new String[31];
        knownWords[0] = " ";
        knownWords[1] = "the";
        knownWords[2] = "and";
        knownWords[3] = "i";
        knownWords[4] = "to";
        knownWords[5] = "of";
        knownWords[6] = "a";
        knownWords[7] = "you";
        knownWords[8] = "my";
        knownWords[9] = "in";
        knownWords[10] = "that";
        knownWords[11] = "is";
        knownWords[12] = "not";
        knownWords[13] = "with";
        knownWords[14] = "me";
        knownWords[15] = "it";
        knownWords[16] = "she";
        knownWords[17] = "he";
        knownWords[18] = "alice";
        knownWords[19] = "said";
        knownWords[20] = "her";
        knownWords[21] = "so";
        knownWords[22] = "as";
        knownWords[23] = "on";
        knownWords[24] = "all";
        knownWords[25] = "had";
        knownWords[26] = "but";
        knownWords[27] = "for";
        knownWords[28] = "very";
        knownWords[29] = "be";
        knownWords[30] = "do";
        int location;
        String currentChar;
        String currentWord = "";

        while (!BinaryStdIn.isEmpty()){
            currentChar = String.valueOf(BinaryStdIn.readChar(8));
            while (currentChar != " ") {
                currentWord = currentWord + currentChar;
                currentChar = String.valueOf(BinaryStdIn.readChar(8));
            }
            location = contains(knownWords,currentWord);
            if (location != -1){
                BinaryStdOut.write(knownWords[location],5);
            }
            else {
                for (int i = 0; i < currentWord.length();i++){
                    BinaryStdOut.write(currentWord.charAt(i),8);
                }
            }


        }

        BinaryStdOut.close();
    }

    public static int contains(String[] words, String target){

        for (int i = 0; i < words.length; i++){
            if (words[i].equals(target)){
                return i;
            }
        }
        return -1;
    }

    private static void expand() {
        String[] knownWords = new String[31];
        knownWords[0] = " ";
        knownWords[1] = "the";
        knownWords[2] = "and";
        knownWords[3] = "i";
        knownWords[4] = "to";
        knownWords[5] = "of";
        knownWords[6] = "a";
        knownWords[7] = "you";
        knownWords[8] = "my";
        knownWords[9] = "in";
        knownWords[10] = "that";
        knownWords[11] = "is";
        knownWords[12] = "not";
        knownWords[13] = "with";
        knownWords[14] = "me";
        knownWords[15] = "it";
        knownWords[16] = "she";
        knownWords[17] = "he";
        knownWords[18] = "alice";
        knownWords[19] = "said";
        knownWords[20] = "her";
        knownWords[21] = "so";
        knownWords[22] = "as";
        knownWords[23] = "on";
        knownWords[24] = "all";
        knownWords[25] = "had";
        knownWords[26] = "but";
        knownWords[27] = "for";
        knownWords[28] = "very";
        knownWords[29] = "be";
        knownWords[30] = "do";
        // TODO: Complete the expand() method
        String words = "";
        int word;

        while (!BinaryStdIn.isEmpty()){
            word = BinaryStdIn.readInt(5);
            if (word <= 30){
                words += knownWords[word];
            }
            else{
                words += (char)word;
            }
        }


        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
