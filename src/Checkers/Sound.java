package Checkers;

// Java program to play an Audio 
// file using Clip Object 
import java.io.File; 
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 

public class Sound { 
	Clip clip;
	AudioInputStream audioInputStream;
	String filePath;
	int time;

	public Sound(String filePath, int time) throws UnsupportedAudioFileException, IOException, LineUnavailableException { 
		// create AudioInputStream object
		this.filePath = filePath;
		this.time = time;
		audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile()); 
		
		// create clip reference 
		clip = AudioSystem.getClip(); 
		
		// open audioInputStream to the clip 
		
	}
	
	public void play() throws LineUnavailableException, IOException{
		clip.open(audioInputStream);
		clip.loop(0);
		clip.start();
		try {
            TimeUnit.MILLISECONDS.sleep(time);
			resetAudioStream();
        } catch (Exception e) {};
	}
	// Method to reset audio stream 
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		clip.close();
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
    }
} 
