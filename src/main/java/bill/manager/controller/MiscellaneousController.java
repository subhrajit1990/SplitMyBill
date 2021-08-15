/**
 * 
 */
package bill.manager.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import bill.manager.service.MiscellaneousService;


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

	

}
