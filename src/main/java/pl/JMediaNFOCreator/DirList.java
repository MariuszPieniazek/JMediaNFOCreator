package pl.JMediaNFOCreator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class DirList{
	
	public ArrayList<String> getDirList(String[] fileTypes, String pathToDirectory) {
		File path = new File(pathToDirectory);
		ArrayList<String> listOfFilterFiles = new ArrayList<String>();
		for (int i = 0; i < fileTypes.length; i++) {
			String[] listOfAllFiles = path.list(new DirFilter(fileTypes[i]));
			for (int j = 0; j < listOfAllFiles.length; j++) listOfFilterFiles.add(listOfAllFiles[j]);	
		}
		Collections.sort(listOfFilterFiles);
		return listOfFilterFiles;		
	}
	
	public Long getFileSize(String filename) {
	    File file = new File(filename);
	    return file.length();
	  }
}