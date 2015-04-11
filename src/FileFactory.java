/**
 * Created by Pierre on 11/04/2015.
 */

import java.io.*;

public interface FileFactory {

    static File[] split(File inputFile, int chunkByteSize) {
        byte[] buffer = new byte[chunkByteSize];
        int chunkNumber = 0;
        int chunkMaxNumber = (int) inputFile.length()/chunkByteSize;
        File[] chunkedFile = new File[chunkMaxNumber+1];

        try(BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile))) {
            int bytesRead = 0;
            while((bytesRead = inputStream.read(buffer)) > 0) {
                File outputFile = new File(inputFile.getName() + chunkNumber);
                try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                    outputStream.write(buffer, 0, bytesRead);
                    chunkedFile[chunkNumber] = outputFile;
                }
                chunkNumber++;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found!");
        } catch (IOException ex) {
            ex.printStackTrace();
        };
        return(chunkedFile);
    };

    static File merge(File[] chunks) {
        File outputFile = null;
        try(BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile))) {

        } catch (FileNotFoundException ex) {
            System.out.println("File not found!");
        } catch (IOException ex) {
            ex.printStackTrace();
        };
        return(null);
    };

}
