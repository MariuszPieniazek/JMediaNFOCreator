package pl.JMediaNFOCreator;

import java.io.File;
import java.util.Arrays;

public class DirList {
	
	public String[] getDirList(String fileType, String pathToDirectory) {
		File path = new File(pathToDirectory);
		String[] list;
		if(fileType.isEmpty()) list = path.list(); 
		else list = path.list(new DirFilter(fileType));
		Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
		return list;		
	}
	
	public int getNumbersOfFiles(String[] filterFiles) {
		int countFiles = filterFiles.length;
		return countFiles;
	}
	
	public Long getFileSize(String filename) {
	    File file = new File(filename);
	    return file.length();
	  }
}