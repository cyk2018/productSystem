package Function;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;



public class FunctionPanel extends JPanel implements ActionListener{
	
	JComboBox comboBox = new JComboBox();
	private JTextField searchInput = new JTextField(15);
	private JButton searchButton = new JButton("查询");
	
	FunctionDatabase fd = new FunctionDatabase();
	
	public FunctionPanel() {
		this.setLayout(new BorderLayout());
		comboBox.addItem("订单结算：请输入订单编号");
		comboBox.addItem("订单结算：请输入客户编号");
		
		JToolBar toolBar = new JToolBar();
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT & FlowLayout.LEADING, 20, 0));
		toolBar.add(comboBox);
		toolBar.add(searchInput);
		toolBar.add(searchButton);
		
		this.add(toolBar, BorderLayout.NORTH);
		
		searchButton.setActionCommand("search");
		searchButton.addActionListener(this);
		searchInput.setActionCommand("searchInput");
		searchInput.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command = e.getActionCommand();
		
		if(command == "search" || command == "searchInput")
		{
			int searchoption = comboBox.getSelectedIndex();
			String searchinput = (String)searchInput.getText();
			try {
				boolean checksearch = fd.search(searchoption, searchinput);
			} catch (SQLException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			FunctionShowFrame fsf = new FunctionShowFrame(fd);
		}
	}
}
