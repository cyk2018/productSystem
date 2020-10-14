package Product;

import java.io.Serializable;

public class Product implements Serializable{
	private String product_no;
	private String product_name;
	private String product_price;
	private String product_amount;
	
	public void set_product_no(String product_no)
	{
		this.product_no = product_no;
	}
	
	public String get_product_no()
	{
		return this.product_no;
	}
	
	public void set_product_name(String product_name)
	{
		this.product_name = product_name;
	}
	
	public String get_product_name()
	{
		return this.product_name;
	}
	
	public void set_product_price(String product_price) {
		this.product_price = product_price;
	}
	
	public String get_product_price()
	{
		return this.product_price;
	}
	
	public void set_product_amount(String product_amount)
	{
		this.product_amount = product_amount;
	}
	
	public String get_product_amount()
	{
		return this.product_amount;
	}
}
