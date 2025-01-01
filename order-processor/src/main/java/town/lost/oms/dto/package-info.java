/**
 * Provides the classes and interfaces necessary to create and manage Order Management System (OMS) data transfer objects (DTOs) aligned with
 * the FIX 4.2 specification (https://www.fixtrading.org/standards/fix-4-2/).
 *
 * <p>This package includes the following key components:
 *
 * <ul>
 *     <li><strong>AbstractEvent</strong> and its subclasses, which represent different types of events that can occur in an OMS, such as:
 *         <ul>
 *             <li>{@link town.lost.oms.dto.NewOrderSingle}: Represents a new order submission.</li>
 *             <li>{@link town.lost.oms.dto.CancelOrderRequest}: Represents a request to cancel an existing order.</li>
 *             <li>{@link town.lost.oms.dto.ExecutionReport}: Represents the execution status of an order.</li>
 *             <li>{@link town.lost.oms.dto.OrderCancelReject}: Indicates that an order cancellation request was rejected.</li>
 *             <li>{@link town.lost.oms.dto.CancelAll}: Represents a request to cancel all orders for a specific symbol.</li>
 *         </ul>
 *     </li>
 *     <li><strong>Enums</strong> that define constants for various order attributes:
 *         <ul>
 *             <li>{@link town.lost.oms.dto.Side}: Represents the side of an order (e.g., buy or sell).</li>
 *             <li>{@link town.lost.oms.dto.OrderType}: Represents the type of an order (e.g., market, limit).</li>
 *             <li>{@link town.lost.oms.dto.TimeInForce}: Represents the time-in-force instructions for an order.</li>
 *             <li>{@link town.lost.oms.dto.Ccy}: Represents currency codes as per ISO 4217.</li>
 *         </ul>
 *     </li>
 *     <li><strong>Utility Classes</strong>:
 *         <ul>
 *             <li>{@link town.lost.oms.dto.ValidateUtil}: Provides utility methods for validating order parameters such as price and quantity.</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <p>Each class is designed to be marshalled and unmarshalled efficiently for high-performance data transfer, leveraging serialization optimizations provided by Chronicle Wire.
 *
 * <p>For more details, refer to the documentation of each individual class.
 */
package town.lost.oms.dto;
