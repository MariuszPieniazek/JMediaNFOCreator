package pl.JMediaNFOCreator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.logging.LogManager;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;

public class TagsReader {
	
	static {
	    try {
	      LogManager.getLogManager().readConfiguration(
	          new ByteArrayInputStream("org.jaudiotagger.level = OFF".getBytes()));
	    } catch (Exception e) {
	      
	    }
	  }
	
	public String getTagReader(String pathToFile, FieldKey fieldKey) {
		String tagReader = null;
		try {
			File file = new File(pathToFile);
			if (AudioFileIO.read(file).getTag() != null && AudioFileIO.read(file).getTag().getFirst(fieldKey) != null && AudioFileIO.read(file).getTag().getFirst(fieldKey) != "") {
				tagReader = AudioFileIO.read(file).getTag().getFirst(fieldKey);
			}
			else {
				tagReader =  "-";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return tagReader;	
	}
	
	public String getBitRate(String pathToFile) {
		String bitRate = null;
		try {
			File file = new File(pathToFile);
			AudioFile audioFile = AudioFileIO.read(file);
			AudioHeader audioHeader = audioFile.getAudioHeader();
			if (audioHeader != null) {
				bitRate = audioHeader.getBitRate();
				if (audioHeader.isVariableBitRate() == true) {
					bitRate = "VBR "+bitRate+" kbit/s";
				}
				else if (audioHeader.isVariableBitRate() == false) {
					bitRate = "CBR "+bitRate+" kbit/s";		
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return bitRate;
	}
	
	public String getTrackLengthAsString(String pathToFile) {
		String trackLengthAsString = null;
		try {
			File file = new File(pathToFile);
			MP3File audioFile = (MP3File)AudioFileIO.read(file);
			MP3AudioHeader mp3AudioHeader = (MP3AudioHeader)audioFile.getAudioHeader();
			if (mp3AudioHeader != null) {
				trackLengthAsString = mp3AudioHeader.getTrackLengthAsString();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return trackLengthAsString;
	}
	
	public Boolean isVariableBitRate(String pathToFile) {
		Boolean variableBitRate = false;
		try {
			File file = new File(pathToFile);
			AudioFile audioFile = AudioFileIO.read(file);
			AudioHeader audioHeader = audioFile.getAudioHeader();
			if (audioHeader != null) {
				variableBitRate = audioHeader.isVariableBitRate();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return variableBitRate;
	}
	
	public String getSampleRate(String pathToFile) {
		String sampleRate = null;
		try {
			File file = new File(pathToFile);
			AudioFile audioFile = AudioFileIO.read(file);
			AudioHeader audioHeader = audioFile.getAudioHeader();
			if (audioHeader != null) {
				sampleRate = audioHeader.getSampleRate();
			}
			else {
				sampleRate = "-";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return sampleRate;
	}
	
	public String getChannels(String pathToFile) {
		String channels = null;
		try {
			File file = new File(pathToFile);
			AudioFile audioFile = AudioFileIO.read(file);
			AudioHeader audioHeader = audioFile.getAudioHeader();
			if (audioHeader != null) {
				channels = audioHeader.getChannels();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return channels;
	}
	
	public String getFormat(String pathToFile) {
		String format = null;
		try {
			File file = new File(pathToFile);
			AudioFile audioFile = AudioFileIO.read(file);
			AudioHeader audioHeader = audioFile.getAudioHeader();
			if (audioHeader != null) {
				format = audioHeader.getFormat();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return format;
	}
	
	public String getEncoder(String pathToFile) {
		String encoder = null;
		try {
			File file = new File(pathToFile);
			AudioFile audioFile = AudioFileIO.read(file);
			if (Utils.getExtension(file).equals("mp3") == true) {
				MP3AudioHeader mp3AudioHeader = (MP3AudioHeader)audioFile.getAudioHeader();
				if (mp3AudioHeader != null && mp3AudioHeader.getEncoder() != null && mp3AudioHeader.getEncoder() != "") {
					encoder = mp3AudioHeader.getEncoder();
				}
			} else {
				encoder = "-";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return encoder;
	}
	
	public Long getBitrateAsNumer(String pathToFile) {
		Long bitrateAsNumer = null;
		try {
			File file = new File(pathToFile);
			AudioFile audioFile = AudioFileIO.read(file);
			AudioHeader audioHeader = audioFile.getAudioHeader();
			if (audioHeader != null) {
				bitrateAsNumer = audioHeader.getBitRateAsNumber();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return bitrateAsNumer;
	}
	
	public int getTrackLength(String pathToFile) {
		int trackLength = 0;
		try {
			File file = new File(pathToFile);
			AudioFile audioFile = AudioFileIO.read(file);
			AudioHeader audioHeader = audioFile.getAudioHeader();
			if (audioHeader != null) {
				trackLength = audioHeader.getTrackLength();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return trackLength;
	}
	
	public String getV2TagId(String pathToFile) {
		String v2TagId = null;
		try {
			File file = new File(pathToFile);
			if (Utils.getExtension(file).equals("mp3") == true) {
				MP3File audioFile = (MP3File)AudioFileIO.read(file);
				if (audioFile.getID3v2Tag() != null) {
					v2TagId  = audioFile.getID3v2Tag().getIdentifier();
				}
			} else {
				v2TagId  = "-";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return v2TagId;
	}
	
	public String getV1TagId(String pathToFile) {
		String v1TagId = null;
		try {
			File file = new File(pathToFile);
			if (Utils.getExtension(file).equals("mp3") == true) {
				MP3File audioFile = (MP3File)AudioFileIO.read(file);
				if (audioFile.getID3v1Tag() != null) {
					v1TagId  = audioFile.getID3v1Tag().getIdentifier();
				}
			} else {
					v1TagId  = "-";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return v1TagId;
	}		
}

