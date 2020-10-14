package Order;

import java.io.Serializable;

public class Order implements Serializable{
	
	private String order_no;
	private String order_client_no;
	private String order_client_name;
	private String order_time;
	private String order_product_no;
	private String product_name;
	private String order_amount;
	private String order_price;
	
	public void set_order_no(String order_no){
		this.order_no = order_no;
	}
	
	public String get_order_no(){
		return this.order_no;
	}
	
	public void set_order_client_no(String order_client_no){
		this.order_client_no = order_client_no;
	}
	
	public String get_order_client_no()
	{
		return this.order_client_no;
	}
	
	public void set_order_client_name(String order_client_name)
	{
		this.order_client_name = order_client_name;
	}
	
	public String get_order_client_name()
	{
		return this.order_client_name;
	}
	
	public void set_order_time(String order_time)
	{
		this.order_time = order_time;
	}
	
	public String get_order_time()
	{
		return this.order_time;
	}
	
	public void set_order_product_no(String order_product_no)
	{
		this.order_product_no = order_product_no;
	}
	
	public String get_order_product_no()
	{
		return this.order_product_no;
	}
	
	public void set_product_name(String product_name)
	{
		this.product_name = product_name;
	}
	
	public String get_product_name()
	{
		return this.product_name;
	}
	
	public void set_order_amount(String order_amount)
	{
		this.order_amount = order_amount;
	}
	
	public String get_order_amount()
	{
		return this.order_amount;
	}
	
	public void set_order_price(String order_price)
	{
		this.order_price = order_price;
	}
	
	public String get_order_price()
	{
		return this.order_price;
	}
}
