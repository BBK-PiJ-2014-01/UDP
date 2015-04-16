/**
 * Created by Pierre on 11/04/2015.
 */

import java.io.*;

public interface FileFactory {

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

    static byte[] concatenateByteArrays(byte[] byteArray1, byte[] byteArray2 ) {
        byte[] newByteArray = new byte[byteArray1.length + byteArray2.length];
        System.arraycopy(byteArray1, 0, newByteArray, 0, byteArray1.length);
        System.arraycopy(byteArray2, 0, newByteArray, byteArray1.length, byteArray2.length);
        return(newByteArray);
    }
}
