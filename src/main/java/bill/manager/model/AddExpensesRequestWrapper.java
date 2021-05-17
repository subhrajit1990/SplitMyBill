package bill.manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Troublem@ker
 */

public class AddExpensesRequestWrapper {

	@JsonProperty("addExpensesRequest")
	private AddExpensesRequest addExpensesRequest;

	public AddExpensesRequest getAddExpensesRequest() {
		return addExpensesRequest;
	}

	public void setAddExpensesRequest(AddExpensesRequest addExpensesRequest) {
		this.addExpensesRequest = addExpensesRequest;
	}

	
}
