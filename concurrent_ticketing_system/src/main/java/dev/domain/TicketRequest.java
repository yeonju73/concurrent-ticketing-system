package dev.domain;

public class TicketRequest {
	private long ticketNo;
	private final String name;
	private final boolean isBot;

	public TicketRequest(String name, boolean isBot) {
		this.name = name;
		this.isBot = isBot;
	}

	public String getName() {
		return name;
	}

	public boolean isBot() {
		return isBot;
	}

	public long getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(long ticketNo) {
		this.ticketNo = ticketNo;
	}
}
