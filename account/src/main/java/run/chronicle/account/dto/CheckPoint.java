//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//
package run.chronicle.account.dto;


/**
 * The {@code CheckPoint} class represents a request to produce a snapshot
 * (or "dump") of the entire current system state at a given moment in time.
 * This includes all accounts and their balances, ensuring that the state
 * can be recorded for audit, recovery, or analysis.
 * <p>
 * A valid {@code CheckPoint} event must have all these fields set; validation
 * is performed automatically when the event is processed.
 *
 * <p>Usage example:
 * <pre>{@code
 * CheckPoint cp = new CheckPoint()
 *     .sender(gw2Id)
 *     .target(vaultId)
 *     .sendingTime(SystemTimeProvider.CLOCK.currentTimeNanos());
 * }</pre>
 */
public class CheckPoint extends AbstractEvent<CheckPoint> {
    // The CheckPoint event leverages the common fields and validation logic provided by AbstractEvent.
}
