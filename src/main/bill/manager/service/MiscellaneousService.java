/**
 * 
 */
package bill.manager.service;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bill.manager.domain.GetCardsDomain;
import bill.manager.model.GameCardResponse;
import bill.manager.model.GameCardsListResponse;
import bill.manager.repo.AddMembers;
import bill.manager.repo.CreateBill;
import bill.manager.repo.CreateGroup;
import bill.manager.repo.FetchCards;

/**
 * @author Troublem@ker
 */

@Service
public class MiscellaneousService implements IMiscellaneousService {

	private static final Logger logger = Logger.getLogger(MiscellaneousService.class);

	@Autowired
	private CreateGroup addGroup;

	@Autowired
	private AddMembers addMembers;

	@Autowired
	private CreateBill createBill;

	@Autowired
	private FetchCards fetchCards;

	/**
	 * 
	 */
	public void deleteAllData() {
		logger.info("Deletion started");
		addGroup.deleteAll();
		addMembers.deleteAll();
		createBill.deleteAll();
		logger.info("Deletion ends");
	}

	/**
	 * @param channel
	 * @param masterTxnRefNo
	 * @return
	 */
	public GameCardsListResponse fetchCards(String channel, String masterTxnRefNo) {
		GameCardsListResponse GameCardsResponse = new GameCardsListResponse();
		ArrayList<GetCardsDomain> fetchCardsRes = null;
		try {
			fetchCardsRes = fetchCards.findAllByChannel(channel);
			if (fetchCardsRes.isEmpty()) {
				
			} else {
				ArrayList<GameCardResponse> gameCardsRes = new ArrayList<>();
				Iterator<GetCardsDomain> itr = fetchCardsRes.iterator();
				while (itr.hasNext()) {
					GameCardResponse gCardRes = new GameCardResponse();
					GetCardsDomain fc = itr.next();
					gCardRes.setCardIcon(fc.getIconName());
					gameCardsRes.add(gCardRes);
				}

				GameCardsResponse.setGameCardResponse(gameCardsRes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return GameCardsResponse;
	}

}
