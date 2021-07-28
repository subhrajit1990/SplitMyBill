/**
 * 
 */
package bill.manager.controller;

import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bill.manager.domain.FetchCardsTransformerResponse;
import bill.manager.model.CreateGroupRequestWrapper;
import bill.manager.model.CreateGroupResponseWrapper;
import bill.manager.transformer.FetchCardsTransformer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Troublem@ker
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/Msc")
@Api(tags = "Miscellaneous Management", value = "/Msc/api/fetchCards")
public class MiscellaneousController {
	private static final Logger logger = Logger.getLogger(MiscellaneousController.class);

	@ApiResponses({ @ApiResponse(code = 200, message = "FetchCard API reachable"),
			@ApiResponse(code = 408, message = "Service Timed Out"),
			@ApiResponse(code = 500, message = "Internal Server Error"),
			@ApiResponse(code = 404, message = "FetchCard API not reachable") })
	@ApiOperation(value = "fetchCards", notes = "Cards")
	@PostMapping(value = "/api/fetchCards", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public FetchCardsTransformerResponse groupCreation(@RequestBody Map<String, Object> payload,
			@RequestHeader("masterTxnRefNo") String masterTxnRefNo, @RequestHeader("channel") String channel) {

		FetchCardsTransformer fetchCardsTransformer = new FetchCardsTransformer();
		JSONObject finalPayload = new JSONObject(payload);
		fetchCardsTransformer.setPayload(finalPayload);

		return fetchCardsTransformer.execute();

	}
}
