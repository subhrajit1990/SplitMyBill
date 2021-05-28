/**
 * 
 */
package bill.manager.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Troublem@ker
 */
public class FetchExpense {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("groupId")
	@ApiModelProperty(example = "1233", position = 1, required = true, value = "${SplitBillRequest.groupId.value}")
	private String id;

	@JsonProperty("billId")
	@ApiModelProperty(example = "1233", position = 2, required = true, value = "${SplitBillRequest.billId.value}")
	private String billId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

}
