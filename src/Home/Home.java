package Home;

import javax.swing.JFrame;

/**
 * 
 * @author ������2018081301003
 *ϵͳ�������ӿ�
 */

public class Home {
	private static void createGUI() {
		MyFrame frame = new MyFrame("�ͻ���������ϵͳ");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 800);
		frame.setVisible(true);
	}
	
	public static void main(String []args)
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						createGUI();
					}
				});
	}
}
