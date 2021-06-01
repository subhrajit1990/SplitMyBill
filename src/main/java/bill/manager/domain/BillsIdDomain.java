/**
 * 
 */
package bill.manager.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @author Troublem@ker
 */
public class BillsIdDomain implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "group_id",updatable=false)
	private Long id;

	@Id
	@Column(name="bill_id",updatable=false)
	private String billId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	
	

}
