package pl.JMediaNFOCreator;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.Checksum;

public class ComputeCRC32 {
	public static String getChecksumValue(Checksum checksum, String fileName) {
		try {
			BufferedInputStream is = new BufferedInputStream(new FileInputStream(fileName));
			byte[] bytes = new byte[1024];
			int lenght = 0;
			while ((lenght = is.read(bytes)) >= 0) {
				checksum.update(bytes, 0, lenght);
			}
			is.close();
		}
	    catch (IOException e) {
	    	e.printStackTrace();
	    }
	    String checksumHEX = Long.toString(checksum.getValue(), 16).toUpperCase();
	    return checksumHEX;
	}
}
