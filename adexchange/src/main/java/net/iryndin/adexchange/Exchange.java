package net.iryndin.adexchange;

import net.iryndin.adexchange.model.BidRequest;

/**
 * A simple exchange server. Publishers will call processBidRequest when they have inventory they want to auction on the
 * TripleLift exchange
 *
 */
public interface Exchange extends AutoCloseable {
	/**
	 *
	 * @param bidRequest
	 *            a bid request to send to all the demand partners
	 */
	void processBidRequest(BidRequest bidRequest);
}