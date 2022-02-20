/**
 * 
 */
package bill.manager.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import bill.manager.model.AddExpensesResponseWrapper;
import bill.manager.model.ContactMeResponseWrapper;
import bill.manager.model.CreateGroupResponseWrapper;
import bill.manager.model.ResponseHeader;

/**
 * @author Troublem@ker
 */
@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

	ResponseHeader responseHeader = new ResponseHeader();

	@ExceptionHandler(ContactMeException.class)
	public ResponseEntity<ContactMeResponseWrapper> handleContactMeException(ContactMeException ex, WebRequest webReq) {
		logger.error("Run time exception happened for contact me module ");
		CommonUtils.generateHeaderForGenericError(responseHeader);
		ContactMeResponseWrapper contactMeResponseWrapper = new ContactMeResponseWrapper();
		contactMeResponseWrapper.setResponseHeader(responseHeader);
		return new ResponseEntity<>(contactMeResponseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler(AddBillException.class)
	public ResponseEntity<AddExpensesResponseWrapper> handleAddBillException(AddBillException ex, WebRequest webReq) {
		logger.error("Run time exception happened for add bill module ");
		CommonUtils.generateHeaderForGenericError(responseHeader);
		AddExpensesResponseWrapper addExpensesResponseWrapper = new AddExpensesResponseWrapper();
		addExpensesResponseWrapper.setResponseHeader(responseHeader);
		return new ResponseEntity<>(addExpensesResponseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler(GroupFailedException.class)
	public ResponseEntity<CreateGroupResponseWrapper> handleGroupFailedException(GroupFailedException ex,
			WebRequest webReq) {
		logger.error("Run time exception happened for create group module ");
		CommonUtils.generateHeaderForGenericError(responseHeader);
		CreateGroupResponseWrapper createGroupResponseWrapper = new CreateGroupResponseWrapper();
		createGroupResponseWrapper.setResponseHeader(responseHeader);
		return new ResponseEntity<>(createGroupResponseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
