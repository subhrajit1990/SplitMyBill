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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bill.manager.model.ResponseHeader;
import bill.manager.model.ToDoAddRequestWrapper;
import bill.manager.model.ToDoResponse;
import bill.manager.model.ToDoResponseWrapper;
import bill.manager.service.ToDoService;
import bill.manager.utils.CommonUtils;
import bill.manager.utils.commonConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Troublem@ker
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/todo/api/")
@Api(tags = "To Do list", value = "/todo/api/")
public class ToDoController {

	private static final Logger logger = Logger.getLogger(ToDoController.class);

	@Autowired
	private ToDoService toDoService;

	@ApiResponses({ @ApiResponse(code = 200, message = "To Do API is reachable"),
			@ApiResponse(code = 408, message = "Service Timed Out"),
			@ApiResponse(code = 500, message = "Internal Server Error"),
			@ApiResponse(code = 404, message = "To Do Add API is not reachable") })
	@ApiOperation(value = "To Do Add", notes = "To Do Add")
	@PostMapping(value = "/addList", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ToDoResponseWrapper> toDoAdd(@RequestBody ToDoAddRequestWrapper toDoAddRequestWrapper,
			@RequestHeader("masterTxnRefNo") String masterTxnRefNo, @RequestHeader("channel") String channel) {
		logger.info("Started the execution for the create group request with masterTxnRefNo :: " + masterTxnRefNo);
		HttpStatus httpStatus = null;
		httpStatus = HttpStatus.OK;
		ResponseHeader responseHeader = new ResponseHeader();
		ToDoResponseWrapper toDoAddResponseWrapper = new ToDoResponseWrapper();
		ToDoResponse toDoAddResponse = new ToDoResponse();
		try {
			toDoAddResponse = toDoService.toDoAdd(toDoAddRequestWrapper.getToDoAddRequest(), channel, masterTxnRefNo);
			if (toDoAddResponse.getGroupStatus().equalsIgnoreCase(commonConstants.SUCCESS)) {
				CommonUtils.generateHeaderForSuccess(responseHeader);
			} else {
				CommonUtils.generateHeaderForGenericError(responseHeader);
			}
		} catch (Exception e) {
			logger.error("exception happened during create group service execution :: " + e.getStackTrace());
			CommonUtils.generateHeaderForGenericError(responseHeader);
		}
		toDoAddResponseWrapper.setResponseHeader(responseHeader);
		logger.info("Finished the execution for the create group request with masterTxnRefNo :: " + masterTxnRefNo);
		return new ResponseEntity<>(toDoAddResponseWrapper, httpStatus);

	}

	

}
