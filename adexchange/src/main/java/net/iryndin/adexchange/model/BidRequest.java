package net.iryndin.adexchange.model;

/** this request comes from our suppliers, all we know about the request is the url and the userId */
public class BidRequest {
	private final String requestUrl;
	private final String userId;
	private final String requestId;

	public BidRequest(String requestUrl, String userId, String requestId) {
		this.requestUrl = requestUrl;
		this.userId = userId;
		this.requestId = requestId;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public String getUserId() {
		return userId;
	}

	public String getRequestId() {
		return requestId;
	}
}