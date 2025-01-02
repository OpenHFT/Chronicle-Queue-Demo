package run.chronicle.routing.out;

import run.chronicle.routing.inout.api.Value;
import run.chronicle.routing.out.api.Even;
import run.chronicle.routing.out.api.SifterIn;
import run.chronicle.routing.out.api.SifterOut;
import run.chronicle.routing.out.api.Triple;

/**
 * SifterImpl checks if val is even or divisible by 3 (or both),
 * then routes to different destinations. For example:
 * - Even -> 'evens()'
 * - Multiple of 3 -> 'triples()'
 *
 * * The SifterImpl class is an implementation of the SifterIn interface.
 * This class implements the logic for processing a Value object.
 * The Value object is processed based on whether its val property is an even number or divisible by 3.
 */
public class SifterImpl implements SifterIn {

    // The SifterOut object for outputting the processed values
    private final SifterOut so;

    // Even object to process even values
    private final Even even = new Even();

    // Triple object to process values divisible by 3
    private final Triple triple = new Triple();

    /**
     * Constructor for the SifterImpl class.
     * This initialises the SifterOut object used for outputting the processed values.
     *
     * @param so A SifterOut object to be used for outputting the processed values
     */
    public SifterImpl(SifterOut so) {
        this.so = so;
    }

    /**
     * Implementation of the value method from the SifterIn interface.
     * This method processes a Value object.
     * If the val property of the Value object is an even number, it is processed as an Even object.
     * If the val property of the Value object is divisible by 3, it is processed as a Triple object.
     * Regardless of the above conditions, the Value object is always output using the SifterOut object.
     *
     * @param value A Value object to be processed
     */
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
