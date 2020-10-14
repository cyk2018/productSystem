package Home;

import javax.swing.JFrame;

/**
 * 
 * @author 陈钰坤2018081301003
 *系统的启动接口
 */

public class Home {
	private static void createGUI() {
		MyFrame frame = new MyFrame("客户订货管理系统");
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
