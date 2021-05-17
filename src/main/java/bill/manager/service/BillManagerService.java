/**
 * 
 */
package bill.manager.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bill.manager.domain.AddMembersDomain;
import bill.manager.domain.CreateGroupDomain;
import bill.manager.model.AddExpensesRequest;
import bill.manager.model.AddMembersRequest;
import bill.manager.model.AddMembersResponse;
import bill.manager.model.CreateGroupRequest;
import bill.manager.model.CreateGroupResponse;
import bill.manager.model.Members;
import bill.manager.repo.AddMembers;
import bill.manager.repo.CreateGroup;
import bill.manager.utils.CommonUtils;
import bill.manager.utils.ErrorConstants;
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
				createGroupResponse.setGroupStatus(ErrorConstants.FAILURE);
			} else {

				AddMembersRequest addMembersRequest = new AddMembersRequest();
				addMembersRequest.setGroupId(insertedId);
				addMembersRequest.setMembers(createGroupRequest.getGroupMembers());
				addMembersResponse = commonAddMember(addMembersRequest, channel, masterTxnRefNo);
				if (addMembersResponse.getMemberStatus().equalsIgnoreCase(ErrorConstants.SUCCESS)) {
					createGroupResponse.setGroupStatus(ErrorConstants.SUCCESS);
				} else {
					createGroupResponse.setGroupStatus(ErrorConstants.FAILURE);
				}
				logger.info("Member addition during group creation is over ");
			}

		} catch (Exception e) {
			logger.error("Exception happend during the group creation :: " + e.getStackTrace());
			addMembersResponse.setMemberStatus(ErrorConstants.FAILURE);
			createGroupResponse.setGroupStatus(ErrorConstants.FAILURE);
			throw new GroupFailedException("SPL_GRP", "Exception");
		}
		logger.info("create group execution over :: ");
		return createGroupResponse;
	}

	/**
	 * @param addExpensesRequest
	 */
	@Override
	@Transactional
	public String addExpenses(AddExpensesRequest addExpensesRequest) {

		return null;

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
			addMembersResponse.setMemberStatus(ErrorConstants.SUCCESS);
			String insertedId = Long.toString(addMembers.count());
			logger.info("Data After saving the members with Id " + insertedId);
		} catch (Exception e) {
			logger.error("Exception happend during the add members  :: " + e.getStackTrace());
			addMembersResponse.setMemberStatus(ErrorConstants.FAILURE);

		}
		return addMembersResponse;
	}

}
