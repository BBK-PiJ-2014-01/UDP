/**
 * Created by Pierre on 11/04/2015
 * Utility methods for:
 * - converting files to byte arrays
 * - byte arrays to files
 * - concatenating byte arrays into a single byte array
 * Methods defined as static. Can be used by both clients and servers for transferring bytes.
 *
 * Implementing static methods with full body is a new feature of Interfaces from Java7.
 */

import java.io.*;

public interface FileFactory {

    /**
     * Converts a file to a byte array
     *
     * @param inputFile file to be converted to a byte array
     * @return resulting byte array
     */
    static byte[] toByteArray(File inputFile) {
        byte[] buffer = new byte[1024];
        byte[] byteArray = null;
        try(BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
            int bytesRead = 0;
            while((bytesRead = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer);
            }
            byteArray = outputStream.toByteArray();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return(byteArray);
    }

    /**
     * Converts a byte array to a file
     *
     * @param byteArray byte array to be converted to a file
     * @param outputFilePath file path for the resulting file
     * @return resulting file
     */
    static File fromByteArray(byte[] byteArray, String outputFilePath) {
        byte[] buffer = new byte[1024];
        File outputFile = new File(outputFilePath);
        try(ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile))) {
            int bytesRead = 0;
            while((bytesRead = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return(outputFile);
    }

    /**
     * Concatenates two bytes arrays into a single byte array
     *
     * @param byteArray1 first of the two concatenated byte arrays
     * @param byteArray2 second of the two concatenated byte arrays
     * @return resulting byte array
     */
    static byte[] concatenateByteArrays(byte[] byteArray1, byte[] byteArray2 ) {
        byte[] newByteArray = new byte[byteArray1.length + byteArray2.length];
        System.arraycopy(byteArray1, 0, newByteArray, 0, byteArray1.length);
        System.arraycopy(byteArray2, 0, newByteArray, byteArray1.length, byteArray2.length);
        return(newByteArray);

    }
}
