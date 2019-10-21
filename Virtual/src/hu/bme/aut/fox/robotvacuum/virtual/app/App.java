package hu.bme.aut.fox.robotvacuum.virtual.app;

import hu.bme.aut.fox.robotvacuum.virtual.app.main.MainScreen;

import javax.swing.*;
import java.awt.*;

public class App {

	public static void main(String[] args) {
		JFrame frame = new JFrame();

		// Setting up the window
		frame.setTitle("Robot Vacuum");
		frame.setSize(960, 800);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Setting the main screen
		Container cnt = new MainScreen();
		frame.setContentPane(cnt);
		frame.setVisible(true);
	}


}
