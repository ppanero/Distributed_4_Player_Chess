package game.graphics;

import java.io.IOException;

public class Runner   {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new game.graphics.Interface();
				} catch (IOException IOE) {
					System.err.println(IOE.toString());
				}
			}
		});
	}
}
