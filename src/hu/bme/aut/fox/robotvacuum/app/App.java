package hu.bme.aut.fox.robotvacuum.app;

import hu.bme.aut.fox.robotvacuum.app.main.MainScreen;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.LinkedList;
import java.util.List;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setWidth(960);
		primaryStage.setHeight(600);
		primaryStage.setScene(new MainScreen());
		primaryStage.show();
	}

	@Override
	public void stop() {
		System.exit(0);
	}

	@SuppressWarnings({"WeakerAccess", "unused"})
	public static abstract class Screen extends Scene {

		private List<Disposable> disposables = new LinkedList<>();

		public Screen() {
			super(new Pane());

			windowProperty().addListener((p, o, n) -> {
				if (o != n) {
					if (o != null) detach();
					if (n != null) attach();
				}
			});
		}

		// Attaching

		private void attach() {
			onAttach();
		}

		public void onAttach() {

		}

		// Detaching

		private void detach() {
			onDetach();
			dispose();
		}

		public void onDetach() {

		}

		// Disposing

		public void addDisposable(Disposable disposable) {
			disposables.add(disposable);
		}

		private void dispose() {
			for (Disposable disposable : disposables) {
				disposable.dispose();
			}
			disposables.clear();
		}

		/* Helper functions */

		public Stage getStage() {
			Window window = getWindow();
			if (window instanceof Stage) {
				return (Stage) window;
			} else {
				return null;
			}
		}

		public void navigate(Scene scene) {
			Stage stage = getStage();
			if (stage != null) {
				stage.setScene(scene);
			} else {
				throw new IllegalStateException("The screen is not attached to a stage.");
			}
		}

		public <T> void subscribe(Observable<T> observable, Consumer<? super T> onNext) {
			Disposable disposable = observable.subscribe((value) -> Platform.runLater(() -> {
				try {
					onNext.accept(value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}));
			addDisposable(disposable);
		}
	}
}
