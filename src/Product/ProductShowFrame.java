package Product;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Container;


public class ProductShowFrame extends JFrame{
	
	public ProductShowFrame (ProductDatabase pd) {
		this.setVisible(true);
		this.setTitle("²éÑ¯½á¹û");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(1000, 600);
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		DefaultTableModel dtm = new DefaultTableModel(pd.srowData, pd.scolumnName);
		
		JTable jt = new JTable(dtm)
				{
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				};
		JScrollPane jsp = new JScrollPane(jt);
		jt.setRowHeight(25);
		contentPane.add(jsp);
	}
}
