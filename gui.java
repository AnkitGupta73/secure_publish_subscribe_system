import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class gui extends JFrame{
	gui(){
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
/*		JPanel cp = new JPanel();
		cp.setLayout(new GridLayout(1, 2));
		cp.add(new producer());
		cp.add(new Consumer());
*/		setContentPane(new producer());
	}
	public static void main(String args[]){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new gui();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
