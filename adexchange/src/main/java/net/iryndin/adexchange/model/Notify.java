package net.iryndin.adexchange.model;

/** this will be sent to a demand partner only if they win */
public class Notify {
	private final String requestId;
	private final long clearPrice;

	public Notify(String requestId, long clearPrice) {
		this.requestId = requestId;
		this.clearPrice = clearPrice;
	}

	public String getRequestId() {
		return requestId;
	}

	public long getClearPrice() {
		return clearPrice;
	}
}