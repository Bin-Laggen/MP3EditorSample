package ie.domis;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;

@SpringBootApplication
public class Mp3ReaderApplication {

	public static void main(String[] args) {
		//SpringApplication.run(Mp3ReaderApplication.class, args);
		
		Application.launch(JavaFXApp.class, args);
	}

}
