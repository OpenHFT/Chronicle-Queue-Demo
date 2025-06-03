/*
 * Copyright 2016-2025 chronicle.software
 */
package town.lost.oms.dto;

/**
 * The {@code ValidateUtil} class provides utility methods for validating numerical values such as price and quantity.
 *
 * <p>This class is final and cannot be instantiated. It contains static methods that check whether a given price or quantity
 * is valid according to predefined business rules.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * double price = 100.0;
 * if (ValidateUtil.invalidPrice(price)) {
 *     throw new IllegalArgumentException("Invalid price");
 * }
 * }</pre>
 */
public final class ValidateUtil {

    /**
     * Private constructor to prevent instantiation.
     */
    private ValidateUtil() {
    }

    /**
     * Checks if the provided price is invalid.
     *
     * <p>A price is considered invalid if it is not a finite number or if it is less than or equal to zero.
     *
     * @param price the price to validate
     * @return {@code true} if the price is invalid; {@code false} otherwise
     */
    public static boolean invalidPrice(double price) {
        return !Double.isFinite(price) || (price <= 0);
    }

    /**
     * Checks if the provided quantity is invalid.
     *
     * <p>A quantity is considered invalid if it is not a finite number or if it is less than zero.
     *
     * @param quantity the quantity to validate
     * @return {@code true} if the quantity is invalid; {@code false} otherwise
     */
    public static boolean invalidQuantity(double quantity) {
        return !Double.isFinite(quantity) || (quantity < 0);
    }
}
