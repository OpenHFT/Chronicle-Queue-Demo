//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

package run.chronicle.staged;

import net.openhft.chronicle.values.Array;

public interface IFacadeAll extends IFacadeSon {
    @Array(length = 42)
    void setTimestampAt(int i, long v);

    long getTimestampAt(int i);
}
