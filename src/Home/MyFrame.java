package Home;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Client.ClientPanel;
import Function.FunctionPanel;
import Invoice.InvoicePanel;
import Order.OrderPanel;
import Product.ProductPanel;



/**
 * 
 * @author ������2018081301003
 *	�û������ǰ������
 */
public class MyFrame extends JFrame implements ChangeListener{
	private ClientPanel clientPanel = new ClientPanel();
	private ProductPanel productPanel = new ProductPanel();
	private OrderPanel orderPanel = new OrderPanel();
	private InvoicePanel invoicePanel = new InvoicePanel();
	private JTabbedPane tabbedPane = new JTabbedPane();
	public MyFrame (String title) {
		super(title);
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		contentPane.add(tabbedPane);
		
		FunctionPanel functionpanel = new FunctionPanel();
		tabbedPane.addTab("�ͻ�����", clientPanel);
		tabbedPane.addTab("�������", productPanel);
		tabbedPane.addTab("��������", orderPanel);
		tabbedPane.addTab("��Ʊ����", invoicePanel);
		tabbedPane.addTab("�������", functionpanel);
		
		tabbedPane.addChangeListener(this);		
	}

	@Override
	public void stateChanged(ChangeEvent e){
		// TODO Auto-generated method stub
		switch (tabbedPane.getSelectedIndex()) {
		case 0:
			clientPanel.refresh();
			break;
		case 1:
			productPanel.refresh();
			break;
		case 2:
			orderPanel.refresh();
			break;
		case 3:
			invoicePanel.refresh();
			break;
		}
	}
}
