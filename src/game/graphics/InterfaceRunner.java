package game.graphics;

import java.io.IOException;

public class InterfaceRunner {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new GraphicInterface();
				} catch (IOException IOE) {
					System.err.println(IOE.toString());
				}
			}
		});
	}
}
