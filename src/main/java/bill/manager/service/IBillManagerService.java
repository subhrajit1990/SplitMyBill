/**
 * 
 */
package bill.manager.service;
import org.springframework.stereotype.Component;

import bill.manager.model.AddExpensesRequest;
import bill.manager.model.AddMembersRequest;
import bill.manager.model.AddMembersResponse;
import bill.manager.model.CreateGroupRequest;
import bill.manager.model.CreateGroupResponse;
import bill.manager.utils.GroupFailedException;

/**
 * @author Troublem@ker
 */

@Component
public interface IBillManagerService {

	public CreateGroupResponse createGroup(CreateGroupRequest addGroupRequest, String channel, String masterTxnRefNo) throws GroupFailedException;
	
	public String addExpenses(AddExpensesRequest addExpensesRequest);

	/**
	 * @param addMembersRequest
	 * @return 
	 */
	public AddMembersResponse addmembers(AddMembersRequest addMembersRequest,String channel, String masterTxnRefNo);
}

