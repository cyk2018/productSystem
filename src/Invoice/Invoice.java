package Invoice;

import java.io.Serializable;

public class Invoice implements Serializable {
	
	private String invoice_no;
	private String invoice_client_no;
	private String order_no;
	private String invoice_time;
	
	public void set_invoice_no(String invoice_no)
	{
		this.invoice_no = invoice_no;
	}
	public String get_invoice_no()
	{
		return this.invoice_no;
	}
	
	public void set_invoice_client_no(String invoice_client_no)
	{
		this.invoice_client_no = invoice_client_no;
	}
	public String get_invoice_client_no()
	{
		return this.invoice_client_no;
	}
	
	public void set_order_no(String order_no)
	{
		this.order_no = order_no;
	}
	public String get_order_no()
	{
		return this.order_no;
	}
	
	public void set_invoice_time(String invoice_time)
	{
		this.invoice_time = invoice_time;
	}
	public String get_invoice_time()
	{
		return this.invoice_time;
	}
}
