/**
 * 
 */
package bill.manager.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bill.manager.model.AddExpensesRequestWrapper;
import bill.manager.model.AddMembersRequestWrapper;
import bill.manager.model.AddMembersResponse;
import bill.manager.model.AddMembersResponseWrapper;
import bill.manager.model.CreateGroupRequestWrapper;
import bill.manager.model.CreateGroupResponse;
import bill.manager.model.CreateGroupResponseWrapper;
import bill.manager.model.ResponseHeader;
import bill.manager.service.BillManagerService;
import bill.manager.utils.AddBillException;
import bill.manager.utils.CommonUtils;
import bill.manager.utils.commonConstants;
import bill.manager.utils.GroupFailedException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Troublem@ker
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/expenses/api/")
@Api(tags = "Bill Management", value = "/expenses/api/")
public class BillManagerController {

	private static final Logger logger = Logger.getLogger(BillManagerController.class);

	@Autowired
	private BillManagerService billManagerService;

	@ApiResponses({ @ApiResponse(code = 200, message = "Biller API reachable"),
			@ApiResponse(code = 408, message = "Service Timed Out"),
			@ApiResponse(code = 500, message = "Internal Server Error"),
			@ApiResponse(code = 404, message = "Biller API not reachable") })
	@ApiOperation(value = "Status", notes = "Heloow")
	@GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public String serviceStatus() {
		return "Hellow";

	}

	@ApiResponses({ @ApiResponse(code = 200, message = "Create Group API is reachable"),
			@ApiResponse(code = 408, message = "Service Timed Out"),
			@ApiResponse(code = 500, message = "Internal Server Error"),
			@ApiResponse(code = 404, message = "Create Group API is not reachable") })
	@ApiOperation(value = "Group Creation", notes = "Create Group")
	@PostMapping(value = "/createGroup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreateGroupResponseWrapper> groupCreation(
			@RequestBody CreateGroupRequestWrapper createGroupRequestWrapper,
			@RequestHeader("masterTxnRefNo") String masterTxnRefNo, @RequestHeader("channel") String channel)
			throws GroupFailedException {
		logger.info("Started the execution for the create group request with masterTxnRefNo :: " + masterTxnRefNo);
		HttpStatus httpStatus = null;
		httpStatus = HttpStatus.OK;
		ResponseHeader responseHeader = new ResponseHeader();
		CreateGroupResponseWrapper createGroupResponseWrapper = new CreateGroupResponseWrapper();
		CreateGroupResponse createGroupResponse = new CreateGroupResponse();
		try {
			createGroupResponse = billManagerService.createGroup(createGroupRequestWrapper.getCreateGroupRequest(),
					channel, masterTxnRefNo);
			if (createGroupResponse.getGroupStatus().equalsIgnoreCase(commonConstants.SUCCESS)) {
				CommonUtils.generateHeaderForSuccess(responseHeader);
			} else {
				CommonUtils.generateHeaderForGenericError(responseHeader);
			}
		} catch (Exception e) {
			logger.error("exception happened during create group service execution :: " + e.getStackTrace());
			CommonUtils.generateHeaderForGenericError(responseHeader);
		}
		createGroupResponseWrapper.setResponseHeader(responseHeader);
		logger.info("Finished the execution for the create group request with masterTxnRefNo :: " + masterTxnRefNo);
		return new ResponseEntity<>(createGroupResponseWrapper, httpStatus);

	}

	@ApiResponses({ @ApiResponse(code = 200, message = "Add Expenses API is reachable"),
			@ApiResponse(code = 408, message = "Service Timed Out"),
			@ApiResponse(code = 500, message = "Internal Server Error"),
			@ApiResponse(code = 404, message = "Add Expenses API is not reachable") })
	@ApiOperation(value = "Add Expenses", notes = "Add Expenses")
	@PostMapping(value = "/addExpenses", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String addExpenses(@RequestBody AddExpensesRequestWrapper addExpensesRequestWrapper,
			@RequestHeader("masterTxnRefNo") String masterTxnRefNo, @RequestHeader("channel") String channel) throws AddBillException {
		logger.info("Started the execution for the add expenses request with masterTxnRefNo :: " + masterTxnRefNo);
		billManagerService.addExpenses(addExpensesRequestWrapper.getAddExpensesRequest(),channel,masterTxnRefNo);
		logger.info("Finished the execution for the add expenses request with masterTxnRefNo :: " + masterTxnRefNo);
		return "Hellow";

	}

	@ApiResponses({ @ApiResponse(code = 200, message = "Add Members API is reachable"),
			@ApiResponse(code = 408, message = "Service Timed Out"),
			@ApiResponse(code = 500, message = "Internal Server Error"),
			@ApiResponse(code = 404, message = "Add Members API is not reachable") })
	@ApiOperation(value = "Add Members", notes = "Add Members")
	@PostMapping(value = "/addMembers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AddMembersResponseWrapper> addMembers(
			@RequestBody AddMembersRequestWrapper addMembersRequestWrapper,
			@RequestHeader("masterTxnRefNo") String masterTxnRefNo, @RequestHeader("channel") String channel) {
		logger.info("Started the execution for the add members request with masterTxnRefNo :: " + masterTxnRefNo);
		HttpStatus httpStatus = null;
		httpStatus = HttpStatus.OK;
		ResponseHeader responseHeader = new ResponseHeader();
		AddMembersResponseWrapper addMembersResponseWrapper = new AddMembersResponseWrapper();
		AddMembersResponse addMembersResponse = new AddMembersResponse();

		try {
			addMembersResponse = billManagerService.addmembers(addMembersRequestWrapper.getAddMembersRequest(), channel,
					masterTxnRefNo);
			if (addMembersResponse.getMemberStatus().equalsIgnoreCase(commonConstants.SUCCESS)) {
				CommonUtils.generateHeaderForSuccess(responseHeader);
			} else {
				CommonUtils.generateHeaderForGenericError(responseHeader);
			}
		} catch (Exception e) {
			logger.error("exception happened during add member service execution :: " + e.getStackTrace());
			CommonUtils.generateHeaderForGenericError(responseHeader);
		}
		addMembersResponseWrapper.setResponseHeader(responseHeader);
		logger.info("Finished the execution for the add members request with masterTxnRefNo :: " + masterTxnRefNo);
		return new ResponseEntity<>(addMembersResponseWrapper, httpStatus);

	}

}
