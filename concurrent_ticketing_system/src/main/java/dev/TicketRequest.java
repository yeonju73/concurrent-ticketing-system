package dev;

public class TicketRequest {
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
}
