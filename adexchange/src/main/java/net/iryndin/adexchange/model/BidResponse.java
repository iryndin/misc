package net.iryndin.adexchange.model;

/** this is a response from a demand partner. bidPrice null indicates a noBid */
public class BidResponse {
	private final long bidPrice;

	public BidResponse(long bidPrice) {
		this.bidPrice = bidPrice;
	}

	public long getBidPrice() {
		return bidPrice;
	}
}