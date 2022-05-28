package run.chronicle.routing.out;

import run.chronicle.routing.inout.api.Value;
import run.chronicle.routing.out.api.Even;
import run.chronicle.routing.out.api.SifterIn;
import run.chronicle.routing.out.api.SifterOut;
import run.chronicle.routing.out.api.Triple;

public class SifterImpl implements SifterIn {
    private final SifterOut so;
    private final Even even = new Even();
    private final Triple triple = new Triple();

    public SifterImpl(SifterOut so) {
        this.so = so;
    }

    @Override
    public void value(Value value) {
        if (value.val() % 2 == 0)
            so.evens()
                    .even(even.val(value.val()));
        if (value.val() % 3 == 0)
            so.triples()
                    .triple(triple.val(value.val()));
        so.out().value(value);
    }
}
