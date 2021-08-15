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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import bill.manager.model.FetchCardsRequestWrapper;
import bill.manager.model.GameCardsListResponse;
import bill.manager.model.GameCardsResponseWrapper;
import bill.manager.model.ResponseHeader;
import bill.manager.service.MiscellaneousService;
import bill.manager.utils.CommonUtils;

/**
 * @author Troublem@ker
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/Misc/api/")
@Api(tags = "Miscellaneous", value = "/Misc/api/")
public class MiscellaneousController {

	private static final Logger logger = Logger.getLogger(MiscellaneousController.class);

	@Autowired
	private MiscellaneousService miscellaneousService;

	@ApiResponses({ @ApiResponse(code = 200, message = "deleteAllData API reachable"),
			@ApiResponse(code = 408, message = "Service Timed Out"),
			@ApiResponse(code = 500, message = "Internal Server Error"),
			@ApiResponse(code = 404, message = "deleteAllData API not reachable") })
	@ApiOperation(value = "deleteAllData", notes = "Delete All Data")
	@GetMapping(value = "/deleteAllAppData", produces = MediaType.APPLICATION_JSON_VALUE)
	public String deletAllAppData() {
		logger.info("Deletion for all data started");
		try {
			miscellaneousService.deleteAllData();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occurred during Deletion for all data ");
		}
		return "Hellow";

	}

	@ApiResponses({ @ApiResponse(code = 200, message = "Get Cards API is reachable"),
			@ApiResponse(code = 408, message = "Service Timed Out"),
			@ApiResponse(code = 500, message = "Internal Server Error"),
			@ApiResponse(code = 404, message = "Get Cards API is not reachable") })
	@ApiOperation(value = "Get Cards", notes = "Get Cards")
	@PostMapping(value = "/fetchCards",  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GameCardsResponseWrapper> getCards(
			
			@RequestBody FetchCardsRequestWrapper fetchCardsrequestWrapper,
			@RequestHeader("masterTxnRefNo") String masterTxnRefNo,
			@RequestHeader("channel") String channel) {
		logger.info("Started the execution for the get cards request with masterTxnRefNo :: " + masterTxnRefNo);
		HttpStatus httpStatus = null;
		httpStatus = HttpStatus.OK;
		ResponseHeader responseHeader = new ResponseHeader();
		GameCardsResponseWrapper gameCardsResponseWrapper = new GameCardsResponseWrapper();
		GameCardsListResponse gameCardsResponse = new GameCardsListResponse();
		try {
			gameCardsResponse = miscellaneousService.fetchCards(channel, masterTxnRefNo);
			if (gameCardsResponse.getGameCardResponse().isEmpty()) {

				CommonUtils.generateHeaderForNoResult(responseHeader);

			} else {
				CommonUtils.generateHeaderForSuccess(responseHeader);
				gameCardsResponseWrapper.setGameCardListResponse(gameCardsResponse);
			}

		} catch (Exception e) {
			logger.error("exception happened during get cards service execution :: " + e.getStackTrace());
			CommonUtils.generateHeaderForGenericError(responseHeader);
		}
		gameCardsResponseWrapper.setResponseHeader(responseHeader);
		logger.info("Finished the execution for the get cards request with masterTxnRefNo :: " + masterTxnRefNo);
		return new ResponseEntity<>(gameCardsResponseWrapper, httpStatus);

	}

}
