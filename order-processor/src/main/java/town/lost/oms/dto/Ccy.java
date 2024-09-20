package town.lost.oms.dto;

/**
 * The {@code Ccy} enum represents international currency codes as per the ISO 4217 standard.
 *
 * <p>Each constant corresponds to a specific currency, identified by its three-letter code.
 * Additional information such as the currency name, country, and symbol can be accessed through
 * the provided methods.
 *
 * <p>Example usage:
 * <pre>{@code
 * Ccy currency = Ccy.USD;
 * System.out.println("Currency Code: " + currency.getCode());
 * System.out.println("Currency Name: " + currency.getCurrencyName());
 * System.out.println("Country: " + currency.getCountry());
 * System.out.println("Symbol: " + currency.getSymbol());
 * }</pre>
 *
 * <p>This would output:
 * <pre>
 * Currency Code: USD
 * Currency Name: United States Dollar
 * Country: United States
 * Symbol: $
 * </pre>
 */
public enum Ccy {
    /**
     * United States Dollar.
     */
    USD("United States Dollar", "United States", "$"),
    /**
     * Euro.
     */
    EUR("Euro", "Eurozone", "€"),
    /**
     * British Pound Sterling.
     */
    GBP("Pound Sterling", "United Kingdom", "£"),
    /**
     * Japanese Yen.
     */
    JPY("Japanese Yen", "Japan", "¥"),
    /**
     * Swiss Franc.
     */
    CHF("Swiss Franc", "Switzerland", "CHF"),
    /**
     * Canadian Dollar.
     */
    CAD("Canadian Dollar", "Canada", "C$"),
    /**
     * Australian Dollar.
     */
    AUD("Australian Dollar", "Australia", "A$"),
    /**
     * New Zealand Dollar.
     */
    NZD("New Zealand Dollar", "New Zealand", "NZ$"),
    /**
     * Chinese Yuan Renminbi.
     */
    CNY("Chinese Yuan", "China", "¥"),
    /**
     * Swedish Krona.
     */
    SEK("Swedish Krona", "Sweden", "kr"),
    /**
     * Norwegian Krone.
     */
    NOK("Norwegian Krone", "Norway", "kr"),
    /**
     * Russian Ruble.
     */
    RUB("Russian Ruble", "Russia", "₽"),
    /**
     * South African Rand.
     */
    ZAR("South African Rand", "South Africa", "R"),
    /**
     * Singapore Dollar.
     */
    SGD("Singapore Dollar", "Singapore", "S$"),
    /**
     * Hong Kong Dollar.
     */
    HKD("Hong Kong Dollar", "Hong Kong", "HK$");

    // Fields
    private final String currencyName;
    private final String country;
    private final String symbol;

    /**
     * Constructs a {@code Ccy} enum constant with the specified currency name, country, and symbol.
     *
     * @param currencyName the full name of the currency
     * @param country      the country or region where the currency is used
     * @param symbol       the symbol representing the currency
     */
    Ccy(String currencyName, String country, String symbol) {
        this.currencyName = currencyName;
        this.country = country;
        this.symbol = symbol;
    }

    /**
     * Gets the full name of the currency.
     *
     * @return the currency name
     */
    public String getCurrencyName() {
        return currencyName;
    }

    /**
     * Gets the country or region where the currency is used.
     *
     * @return the country or region
     */
    public String getCountry() {
        return country;
    }

    /**
     * Gets the symbol representing the currency.
     *
     * @return the currency symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Gets the three-letter currency code as per the ISO 4217 standard.
     *
     * @return the currency code
     */
    public String getCode() {
        return this.name();
    }
}

