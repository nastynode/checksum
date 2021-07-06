package com.davisdev;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Scanner;

/* Checksum Tool
* Lets you input a file path and a SHA-256 checksum
* hashes the file and compares the result to the supplied checksum
*
*/
public class Main {

    public static void main(String[] args) {
        Scanner userIn = new Scanner(System.in);
        String filePath, checksum = "";

	    if( args.length != 1){
	        System.out.print("You can enter filepath as an argument when running program\n");
	        filePath = getFilePath(userIn);
        }
	    else{
	        filePath = args[0];
        }

	    File f = new File(filePath);

	    while (!f.exists()){
	        System.out.println("Error: File Does Not Exist");
	        f = new File(filePath = getFilePath(userIn));
        }
	    while( checksum.length() != 64){
	        System.out.print("Enter a valid 64-digit hex checksum: ");
	        checksum = userIn.nextLine();
        }

	    //hash
        String hashed = toHash(filePath);
	    System.out.println( hashed );
	    System.out.println( "comparing hashed value to supplied checksum...");
	    if( hashed.equalsIgnoreCase(checksum) ){
	        System.out.println("MATCH FOUND! Your file's hash and your supplied checksum are a match");
        }
	    else{
	        System.out.println("WARNING! Your file's hash and the supplied checksum DO NOT MATCH, your file is not safe!");
        }
    }

    static String getFilePath(Scanner userIn){
        System.out.print("Enter a valid file path to verify checksum: ");
        String fp = userIn.nextLine();
        return fp;
    }

    static String toHash(String filepath){

        try {

            System.out.println("Hashing '" + filepath + "'");
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] toBytes = Files.readAllBytes(Paths.get(filepath));
            md.update(toBytes);
            byte[] hashed = md.digest();
            //System.out.println( "Hash results:\n" + asHex(hashed) );
            return new String( asHex(hashed) );

        }catch(Exception e){
            System.out.println("SHA-256 error: " + e);
        }
        return "Error when hashing, verify filepath";
    }

    /*Hex Converter
     * Java problem: Smallest bit increment is 2 bytes
     * each element of byte array is 8 bits but only want 4 at a time
     * */
    static String asHex(byte[] bytearray){
        char hexArray[] = new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char encoded[] = new char[bytearray.length * 2]; //doing 4 bits per char instead of 8
        int i, j;

        for(i=0; i< bytearray.length; i++){
            //mask 8 bits
            j = bytearray[i] & 0xFF;
            //right shift 4 for first byte, no need to mask with leading 0s
            encoded[i * 2] = hexArray[j >>> 4];
            //mask last 4 bits for second byte
            encoded[i * 2 + 1] = hexArray[j & 0x0F];
        }
        String asHex = new String(encoded);

        return asHex;
    }
}
