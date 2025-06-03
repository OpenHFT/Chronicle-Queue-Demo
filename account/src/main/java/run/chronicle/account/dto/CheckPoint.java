/*
 * Copyright 2016-2025 chronicle.software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
