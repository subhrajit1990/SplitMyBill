/**
 * 
 */
package bill.manager.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bill.manager.domain.AddBillDomain;
import bill.manager.domain.AddMembersDomain;
import bill.manager.domain.CreateGroupDomain;
import bill.manager.model.AddMembersRequest;
import bill.manager.model.AddMembersResponse;
import bill.manager.model.CreateGroupRequest;
import bill.manager.model.CreateGroupResponse;
import bill.manager.model.Expense;
import bill.manager.model.Members;
import bill.manager.repo.AddMembers;
import bill.manager.repo.CreateBill;
import bill.manager.repo.CreateGroup;
import bill.manager.utils.AddBillException;
import bill.manager.utils.CommonUtils;
import bill.manager.utils.commonConstants;
import bill.manager.utils.GroupFailedException;

/**
 * @author Troublem@ker
 */

@Service
public class BillManagerService implements IBillManagerService {

	private static final Logger logger = Logger.getLogger(BillManagerService.class);

	@Autowired
	private CreateGroup addGroup;

	@Autowired
	private AddMembers addMembers;

	@Autowired
	private CreateBill createBill;

	List<Expense> expenses;
	Map<String, Map<String, Double>> balanceSheet;

	/**
	 * @param createGroupRequest
	 * @param masterTxnRefNo
	 * @param channel
	 */
	@Override
	@Transactional
	public CreateGroupResponse createGroup(CreateGroupRequest createGroupRequest, String channel, String masterTxnRefNo)
			throws GroupFailedException {

		logger.info("create group execution started :: " + createGroupRequest.toString());
		CreateGroupDomain createGroupDomain = new CreateGroupDomain();
		AddMembersResponse addMembersResponse = new AddMembersResponse();
		CreateGroupResponse createGroupResponse = new CreateGroupResponse();
		createGroupDomain.setChannel(channel);
		createGroupDomain.setMasterTxnNo(masterTxnRefNo);
		createGroupDomain.setGroupType(createGroupRequest.getGroupType());
		createGroupDomain.setGroupName(createGroupRequest.getGroupName());
		createGroupDomain.setCreatorAccountNumber(createGroupRequest.getCreatorAccountNumber());
		createGroupDomain.setUpdatedDt(CommonUtils.currentTime());
		createGroupDomain.setCreatedDt(CommonUtils.currentTime());
		logger.info("Data before saving " + createGroupDomain.toString());
		try {
			addGroup.save(createGroupDomain);
			String insertedId = Long.toString(createGroupDomain.getId());
			logger.info("Data After saving with Id " + insertedId);
			if (insertedId == null || insertedId.isEmpty()) {
				createGroupResponse.setGroupStatus(commonConstants.FAILURE);
			} else {

				AddMembersRequest addMembersRequest = new AddMembersRequest();
				addMembersRequest.setGroupId(insertedId);
				addMembersRequest.setMembers(createGroupRequest.getGroupMembers());
				addMembersResponse = commonAddMember(addMembersRequest, channel, masterTxnRefNo);
				if (addMembersResponse.getMemberStatus().equalsIgnoreCase(commonConstants.SUCCESS)) {
					createGroupResponse.setGroupStatus(commonConstants.SUCCESS);
				} else {
					createGroupResponse.setGroupStatus(commonConstants.FAILURE);
				}
				logger.info("Member addition during group creation is over ");
			}

		} catch (Exception e) {
			logger.error("Exception happend during the group creation :: " + e.getStackTrace());
			addMembersResponse.setMemberStatus(commonConstants.FAILURE);
			createGroupResponse.setGroupStatus(commonConstants.FAILURE);
			throw new GroupFailedException("SPL_GRP", "Exception");
		}
		logger.info("create group execution over :: ");
		return createGroupResponse;
	}

	/**
	 * @param addMembersRequest
	 * @param masterTxnRefNo
	 * @param channel
	 */
	@Override
	@Transactional
	public AddMembersResponse addmembers(AddMembersRequest addMembersRequest, String channel, String masterTxnRefNo) {
		logger.info("add memebrs execution started :: " + addMembersRequest.toString());
		AddMembersResponse addMembersResponse = new AddMembersResponse();
		addMembersResponse = commonAddMember(addMembersRequest, channel, masterTxnRefNo);
		logger.info("add memebrs execution over :: ");
		return addMembersResponse;
	}

	@Transactional
	public AddMembersResponse commonAddMember(AddMembersRequest addMembersRequest, String channel,
			String masterTxnRefNo) {
		AddMembersResponse addMembersResponse = new AddMembersResponse();
		List<AddMembersDomain> addMembersDomainList = new ArrayList<>();
		for (Members members : addMembersRequest.getMembers()) {
			AddMembersDomain addMembersDomain = new AddMembersDomain();
			addMembersDomain.setChannel(channel);
			addMembersDomain.setMasterTxnNo(masterTxnRefNo);
			addMembersDomain.setId(Long.parseLong(addMembersRequest.getGroupId()));
			addMembersDomain.setAccountName(members.getAccountName());
			addMembersDomain.setMemberAccountNumber(members.getMemberAccountNumber());
			addMembersDomain.setUpdatedDt(CommonUtils.currentTime());
			addMembersDomain.setCreatedDt(CommonUtils.currentTime());
			addMembersDomainList.add(addMembersDomain);
		}

		try {
			addMembers.saveAll(addMembersDomainList);
			addMembersResponse.setMemberStatus(commonConstants.SUCCESS);
			String insertedId = Long.toString(addMembers.count());
			logger.info("Data After saving the members with Id " + insertedId);
		} catch (Exception e) {
			logger.error("Exception happend during the add members  :: " + e.getStackTrace());
			addMembersResponse.setMemberStatus(commonConstants.FAILURE);

		}
		return addMembersResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bill.manager.service.IBillManagerService#addExpenses(bill.manager.model.
	 * Expense)
	 */

	@Transactional
	public String addExpenses(Expense addExpensesRequest, String channel, String masterTxnNo) throws AddBillException {
		logger.info("Expense request :: " + addExpensesRequest.toString());

		String expenseType = addExpensesRequest.getExpenseType();
		double amount = addExpensesRequest.getAmount();
		String paidBy = addExpensesRequest.getPaidBy();
		Map<String, Object> map = addExpensesRequest.getSplits();
		String groupId = addExpensesRequest.getId();
		String billId = addExpensesRequest.getBillId();

		balanceSheet = new HashMap<>();

		try {
			for (Map.Entry<String, Object> set : map.entrySet()) {
				logger.info(set.getKey());

				balanceSheet.put(set.getKey(), new HashMap<String, Double>());

			}

			Map<String, Double> splits = createExpenses(expenseType, amount, map);
			for (Map.Entry<String, Double> set : splits.entrySet()) {
				String paidTo = set.getKey();
				Map<String, Double> balances = balanceSheet.get(paidBy);
				if (!balances.containsKey(paidTo)) {
					balances.put(paidTo, 0.0);
				}
				balances.put(paidTo, balances.get(paidTo) + set.getValue());

				balances = balanceSheet.get(paidTo);
				if (!balances.containsKey(paidBy)) {
					balances.put(paidBy, 0.0);
				}
				balances.put(paidBy, balances.get(paidBy) - set.getValue());

			}
			finalBalances(channel, masterTxnNo, groupId, billId, balanceSheet);
		} catch (Exception e) {
			logger.error("exception occurred during balance creation" + e.getStackTrace());
			e.printStackTrace();
			throw new AddBillException("SPL_BIL", "Exception");
		}

		return null;
	}

	/**
	 * @param expenseType
	 * @param amount
	 * @param members
	 * @param map
	 * @return
	 */

	private Map<String, Double> createExpenses(String expenseType, double amount, Map<String, Object> map) {
		Map<String, Double> splits = new HashMap<>();
		switch (expenseType) {
		case commonConstants.PERCENT:
			logger.info("Before Values :: " + map.toString());
			for (Map.Entry<String, Object> set : map.entrySet()) {
				logger.info(set.getKey() + " :: " + (amount * (double) set.getValue()) / 100.00);
				splits.put(set.getKey(), (amount * (double) set.getValue()) / 100.00);
			}
			logger.info("Final Values :: " + map.toString());
			break;
		case commonConstants.EQUAL:

			int splitsAmong = map.size();
			double splitAmount = ((double) Math.round(amount * 100 / splitsAmong)) / 100.0;

			for (Map.Entry<String, Object> set : map.entrySet()) {
				logger.info("EQUAL :: " + set.getKey() + " :: " + splitAmount);
				splits.put(set.getKey(), splitAmount);
			}

			logger.info("Final EQUAL AMOUNT :: " + splitAmount + (amount - splitAmount * splitsAmong));

			break;
		case commonConstants.EXACT:
			double sumSplitAmount = 0;
			for (Map.Entry<String, Object> set : map.entrySet()) {
				logger.info("EQUAL :: " + set.getKey() + " :: " + (set.getValue()));
				sumSplitAmount += (double) set.getValue();
			}
			logger.info("EQUAL FINAL AMOUNT :: " + sumSplitAmount);

			if (sumSplitAmount == amount) {

				for (Map.Entry<String, Object> set : map.entrySet()) {
					logger.info("EQUAL :: " + set.getKey() + " :: " + (amount));
					splits.put(set.getKey(), (double) set.getValue());
				}

			}
			break;
		default:
			return splits;
		}
		return splits;

	}

	/**
	 * @param balanceSheet2
	 * @param billId
	 * @param groupId
	 * 
	 */
	@Transactional
	public void finalBalances(String channel, String masterTxnNo, String groupId, String billId,
			Map<String, Map<String, Double>> finalBalanceSheet) {
		boolean isEmpty = true;
		String fBalance = "";
		for (Map.Entry<String, Map<String, Double>> allBalances : finalBalanceSheet.entrySet()) {
			for (Map.Entry<String, Double> memberBalance : allBalances.getValue().entrySet()) {
				if (memberBalance.getValue() > 0) {
					isEmpty = false;
					fBalance = fBalance.concat(
							saveBalance(allBalances.getKey(), memberBalance.getKey(), memberBalance.getValue()));

				}
			}
		}

		if (isEmpty) {
			logger.info("No balances");
		} else {
			logger.info("final :: " + fBalance);
			AddBillDomain addBillDomain = new AddBillDomain();
			addBillDomain.setChannel(channel);
			addBillDomain.setBillId(Long.parseLong(billId));
			addBillDomain.setId(Long.parseLong(groupId));
			addBillDomain.setUpdatedDt(CommonUtils.currentTime());
			addBillDomain.setCreatedDt(CommonUtils.currentTime());
			addBillDomain.setMasterTxnNo(masterTxnNo);
			addBillDomain.setBill(fBalance);
			createBill.save(addBillDomain);
		}
	}

	private String saveBalance(String user1, String user2, Double amount) {
		String billBalanceDetails = "";
		// format : user1(owes)~user2-amount|
		if (amount < 0) {

			billBalanceDetails = user1 + "~" + user2 + "-" + Math.abs(amount) + "|";

		} else if (amount > 0) {

			billBalanceDetails = user2 + "~" + user1 + "-" + Math.abs(amount) + "|";
		}
		return billBalanceDetails;
	}

}
