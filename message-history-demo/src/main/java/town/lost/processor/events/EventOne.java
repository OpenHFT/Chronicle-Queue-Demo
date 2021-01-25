package town.lost.processor.events;

public class EventOne extends AbstractEvent<EventOne> {
    String text;

    public String text() {
        return text;
    }

    public EventOne text(String text) {
        this.text = text;
        return this;
    }
}
