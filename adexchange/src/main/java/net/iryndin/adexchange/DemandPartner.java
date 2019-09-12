package net.iryndin.adexchange;

import net.iryndin.adexchange.model.BidRequest;
import net.iryndin.adexchange.model.BidResponse;
import net.iryndin.adexchange.model.Notify;

/**
 * This is a bidder with the networking abstracted away.
 **/
public interface DemandPartner {
	/**
	 * Called from the exchange to inform the demand partner of an auction
	 *
	 * @param bidRequest
	 * @return
	 */
	BidResponse processRequest(BidRequest bidRequest);

	/**
	 * Called when the partner wins the auction
	 *
	 * @param notify
	 */
	void processWin(Notify notify);
}