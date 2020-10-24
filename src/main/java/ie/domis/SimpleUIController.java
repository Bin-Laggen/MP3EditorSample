package ie.domis;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

@Component
public class SimpleUIController {

	private final HostServices hostServices;
	private Stage primaryStage;

	public SimpleUIController(HostServices hostServices) {
		this.hostServices = hostServices;
	}

	@FXML
	public Label label;

	@FXML
	public Button button;

	@FXML
	public void initialize() {
		this.button.setOnAction(actionEvent -> {
			this.label.setText(this.hostServices.getDocumentBase());
			FileChooser fc = new FileChooser();
			fc.setTitle("Choose MP3 File");
			fc.getExtensionFilters().add(new ExtensionFilter("MP3", "*.mp3"));
			File dest = fc.showOpenDialog(this.primaryStage);
			if (dest != null) {
				this.label.setText(dest.getAbsolutePath());
				processMP3File(dest);
			}
		});
	}

	public void setStage(Stage stage) {
		this.primaryStage = stage;
	}
	
	private void processMP3File(File file) {
		try {
			Mp3File mp3file = new Mp3File(file);
			String[] dirs = mp3file.getFilename().replace("\\", "\\\\").split("\\\\");
			String filename = dirs[dirs.length - 1];
			System.out.println("Filename: " + filename);
			System.out.println("Length of this mp3 is: " + mp3file.getLengthInSeconds() + " seconds");
	        System.out.println("Bitrate: " + mp3file.getBitrate() + " kbps " + (mp3file.isVbr() ? "(VBR)" : "(CBR)"));
	        System.out.println("Sample rate: " + mp3file.getSampleRate() + " Hz");
	        System.out.println("Has ID3v1 tag?: " + (mp3file.hasId3v1Tag() ? "YES" : "NO"));
	        System.out.println("Has ID3v2 tag?: " + (mp3file.hasId3v2Tag() ? "YES" : "NO"));
	        System.out.println("Has custom tag?: " + (mp3file.hasCustomTag() ? "YES" : "NO"));
	        
	        if (mp3file.hasId3v1Tag()) {
	        	ID3v1 id3v1Tag = mp3file.getId3v1Tag();
	        	System.out.println("Track: " + id3v1Tag.getTrack());
	        	System.out.println("Artist: " + id3v1Tag.getArtist());
	        	System.out.println("Title: " + id3v1Tag.getTitle());
	        	System.out.println("Album: " + id3v1Tag.getAlbum());
	        	System.out.println("Year: " + id3v1Tag.getYear());
	        	System.out.println("Genre: " + id3v1Tag.getGenre() + " (" + id3v1Tag.getGenreDescription() + ")");
	        	System.out.println("Comment: " + id3v1Tag.getComment());
	        }
	        
	        if (mp3file.hasId3v2Tag()) {
	        	ID3v2 id3v2Tag = mp3file.getId3v2Tag();
	        	System.out.println("Track: " + id3v2Tag.getTrack());
	        	System.out.println("Artist: " + id3v2Tag.getArtist());
	        	System.out.println("Title: " + id3v2Tag.getTitle());
	        	System.out.println("Album: " + id3v2Tag.getAlbum());
	        	System.out.println("Year: " + id3v2Tag.getYear());
	        	System.out.println("Genre: " + id3v2Tag.getGenre() + " (" + id3v2Tag.getGenreDescription() + ")");
	        	System.out.println("Comment: " + id3v2Tag.getComment());
	        	System.out.println("Composer: " + id3v2Tag.getComposer());
	        	System.out.println("Publisher: " + id3v2Tag.getPublisher());
	        	System.out.println("Original artist: " + id3v2Tag.getOriginalArtist());
	        	System.out.println("Album artist: " + id3v2Tag.getAlbumArtist());
	        	System.out.println("Copyright: " + id3v2Tag.getCopyright());
	        	System.out.println("URL: " + id3v2Tag.getUrl());
	        	System.out.println("Encoder: " + id3v2Tag.getEncoder());
	        	
	        	if (id3v2Tag.getTitle() == null) {
	        		id3v2Tag.setTitle(filename.split("-")[0]);
	        		mp3file.save(mp3file.getFilename().replace(".mp3", "(1).mp3"));
	        	}
	        }
	        
		} catch (UnsupportedTagException | InvalidDataException | IOException | NotSupportedException e) {
			throw new RuntimeException(e);
		}
		
	}

}
