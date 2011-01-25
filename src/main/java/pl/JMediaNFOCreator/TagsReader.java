package pl.JMediaNFOCreator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.logging.LogManager;

import org.jaudiotagger.audio.AudioFileIO;
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
			MP3File audioFile = (MP3File)AudioFileIO.read(file);
			MP3AudioHeader mp3AudioHeader = (MP3AudioHeader)audioFile.getAudioHeader();
			if (mp3AudioHeader != null) {
				bitRate = mp3AudioHeader.getBitRate();
				if (mp3AudioHeader.isVariableBitRate() == true) {
					bitRate = "VBR "+bitRate+" kbit/s";
				}
				else if (mp3AudioHeader.isVariableBitRate() == false) {
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
			MP3File audioFile = (MP3File)AudioFileIO.read(file);
			MP3AudioHeader mp3AudioHeader = (MP3AudioHeader)audioFile.getAudioHeader();
			if (mp3AudioHeader != null) {
				variableBitRate = mp3AudioHeader.isVariableBitRate();
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
			MP3File audioFile = (MP3File)AudioFileIO.read(file);
			MP3AudioHeader mp3AudioHeader = (MP3AudioHeader)audioFile.getAudioHeader();
			if (mp3AudioHeader != null) {
				sampleRate = mp3AudioHeader.getSampleRate();
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
			MP3File audioFile = (MP3File)AudioFileIO.read(file);
			MP3AudioHeader mp3AudioHeader = (MP3AudioHeader)audioFile.getAudioHeader();
			if (mp3AudioHeader != null) {
				channels = mp3AudioHeader.getChannels();
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
			MP3File audioFile = (MP3File)AudioFileIO.read(file);
			MP3AudioHeader mp3AudioHeader = (MP3AudioHeader)audioFile.getAudioHeader();
			if (mp3AudioHeader != null) {
				format = mp3AudioHeader.getFormat();
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
			MP3File audioFile = (MP3File)AudioFileIO.read(file);
			MP3AudioHeader mp3AudioHeader = (MP3AudioHeader)audioFile.getAudioHeader();
			if (mp3AudioHeader != null && mp3AudioHeader.getEncoder() != null && mp3AudioHeader.getEncoder() != "") {
				encoder = mp3AudioHeader.getEncoder();
			}
			else {
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
			MP3File audioFile = (MP3File)AudioFileIO.read(file);
			MP3AudioHeader mp3AudioHeader = (MP3AudioHeader)audioFile.getAudioHeader();
			if (mp3AudioHeader != null) {
				bitrateAsNumer = mp3AudioHeader.getBitRateAsNumber();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return bitrateAsNumer;
	}
	
	public Double getPreciseTrackLength(String pathToFile) {
		Double preciseTrackLength = null;
		try {
			File file = new File(pathToFile);
			MP3File audioFile = (MP3File)AudioFileIO.read(file);
			MP3AudioHeader mp3AudioHeader = (MP3AudioHeader)audioFile.getAudioHeader();
			if (mp3AudioHeader != null) {
				preciseTrackLength = mp3AudioHeader.getPreciseTrackLength();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return preciseTrackLength;
	}
	
	public String getV2TagId(String pathToFile) {
		String v2TagId = null;
		try {
			File file = new File(pathToFile);
			MP3File audioFile = (MP3File)AudioFileIO.read(file);
			if (audioFile.getID3v2Tag() != null) {
				v2TagId  = audioFile.getID3v2Tag().getIdentifier();
			}
			else v2TagId  = "-";
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
			MP3File audioFile = (MP3File)AudioFileIO.read(file);
			if (audioFile.getID3v1Tag() != null) {
				v1TagId  = audioFile.getID3v1Tag().getIdentifier();
			}
			else v1TagId  = "-";
				
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return v1TagId;
	}
}

