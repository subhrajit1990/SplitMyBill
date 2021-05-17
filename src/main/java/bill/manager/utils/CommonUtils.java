package bill.manager.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import bill.manager.model.ResponseHeader;

/**
 * @author Troublem@ker
 */

public class CommonUtils {

	public static final String FAILURE = ErrorConstants.FAILURE;
	public static final String SUCCESS = ErrorConstants.SUCCESS;

	private CommonUtils() {
	}

	public static void generateHeaderForSoapConnectionFailure(ResponseHeader responseHeader) {

		responseHeader.setResponseCode(FAILURE);
		responseHeader.setErrorCode(ErrorConstants.SEVICENOTREACHABLEERROR);
		responseHeader.setErrorMessage(ErrorConstants.SEVICENOTREACHABLEERROR);
	}

	public static void generateHeaderForNoResult(ResponseHeader responseHeader) {
		responseHeader.setHttpStatus(HttpStatus.OK);
		responseHeader.setErrorCode(ErrorConstants.NORECORD);
		responseHeader.setErrorMessage(ErrorConstants.NORECORD);
		responseHeader.setResponseCode(FAILURE);
	}

	public static void generateHeaderForDBError(ResponseHeader responseHeader) {
		responseHeader.setHttpStatus(HttpStatus.OK);
		responseHeader.setResponseCode(FAILURE);
		responseHeader.setErrorCode(ErrorConstants.PERSISTENCEERROR);
		responseHeader.setErrorMessage(ErrorConstants.PERSISTENCEERROR);
	}

	public static void generateHeaderForGenericError(ResponseHeader responseHeader) {
		responseHeader.setHttpStatus(HttpStatus.OK);
		responseHeader.setResponseCode(FAILURE);
		responseHeader.setErrorCode(ErrorConstants.PROCESSINGREQUESTERROR);
		responseHeader.setErrorMessage(ErrorConstants.PROCESSINGREQUESTERROR);
	}

	public static void generateHeaderForSuccess(ResponseHeader responseHeader) {
		responseHeader.setResponseCode(SUCCESS);
		responseHeader.setHttpStatus(HttpStatus.OK);
	}

	public static Timestamp currentTime() {
		LocalDateTime localDateTime = LocalDateTime.now();
		return Timestamp.valueOf(localDateTime);

	}

}
