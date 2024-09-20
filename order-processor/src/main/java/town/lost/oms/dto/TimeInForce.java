package town.lost.oms.dto;

/**
 * The {@code TimeInForce} enum represents the time conditions under which an order will be executed.
 *
 * <p>These conditions specify how long an order remains active in the market before it is executed or expires.
 * This enum includes commonly used time-in-force instructions in trading systems, conforming to standard trading practices.
 *
 * <p>Example usage:
 * <pre>{@code
 * TimeInForce tif = TimeInForce.DAY;
 * System.out.println("Time in Force: " + tif);
 * }</pre>
 *
 * <p>This would output:
 * <pre>
 * Time in Force: DAY
 * </pre>
 */
public enum TimeInForce {
    /**
     * Indicates that the order should be valid for the current trading day only.
     * If not executed by the end of the trading day, the order expires.
     */
    DAY,

    /**
     * "Good Till Cancelled" - The order remains active until it is either executed or explicitly cancelled by the trader.
     * It may carry over to the next trading day.
     */
    GTC,

    /**
     * "Immediate or Cancel" - The order attempts to execute all or part immediately.
     * Any portion not immediately executed is cancelled.
     */
    IOC,

    /**
     * "Fill or Kill" - The order must be executed immediately in its entirety, or it is cancelled.
     * Partial fills are not allowed.
     */
    FOK,

    /**
     * "Good Till Date" - The order remains active until a specified date unless it is executed or cancelled before then.
     */
    GTD,

    /**
     * "Good Till Time" - The order remains active until a specified time on the current trading day.
     * If not executed by that time, the order expires.
     */
    GTT,

    /**
     * "At the Opening" - The order is to be executed at the opening of the market.
     * If not executed at the opening, it is cancelled.
     */
    OPG,

    /**
     * "At the Close" - The order is to be executed at the close of the market.
     * If not executed at the close, it is cancelled.
     */
    ATC,

    /**
     * "Good for Auction" - The order is valid only during the auction period.
     */
    GFA,

    /**
     * "Good in Session" - The order is valid only during the specified trading session.
     */
    GIS,

    /**
     * "Good Through Crossing" - The order is valid through the crossing session.
     */
    GTX,

    /**
     * "Systematic Internaliser Only" - The order is to be executed only on a systematic internaliser.
     */
    SIO
}
