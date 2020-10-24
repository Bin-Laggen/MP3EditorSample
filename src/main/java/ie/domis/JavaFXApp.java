package ie.domis;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;

public class JavaFXApp extends Application {
	
	private ConfigurableApplicationContext context;

	@Override
	public void init() throws Exception {
		
		ApplicationContextInitializer<GenericApplicationContext> initializer = ac -> {
			ac.registerBean(Application.class, () -> JavaFXApp.this);
			ac.registerBean(Parameters.class, () -> getParameters());
			ac.registerBean(HostServices.class, () -> getHostServices());
		};
		
		this.context = new SpringApplicationBuilder()
			.sources(Mp3ReaderApplication.class)
			.initializers(initializer)
			.run(getParameters().getRaw().toArray(new String[0]));
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.context.publishEvent(new StageReadyEvent(primaryStage));
		
	}

	@Override
	public void stop() throws Exception {
		this.context.close();
		Platform.exit();
	}

}

class StageReadyEvent extends ApplicationEvent {
	
	public Stage getStage() {
		return Stage.class.cast(getSource());
	}
	
	public StageReadyEvent(Stage source) {
		super(source);
	}
	
}
