package town.lost.processor.events;

public class EventTwo extends AbstractEvent {
    String symbol;
    double price;
    double quantiity;

    public String symbol() {
        return symbol;
    }

    public EventTwo symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public double price() {
        return price;
    }

    public EventTwo price(double price) {
        this.price = price;
        return this;
    }

    public double quantiity() {
        return quantiity;
    }

    public EventTwo quantiity(double quantiity) {
        this.quantiity = quantiity;
        return this;
    }
}
