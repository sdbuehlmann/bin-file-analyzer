package ch.puzzle.devtre.tools.zip.analyser.model;

import lombok.NonNull;
import lombok.Value;
import lombok.val;

@Value
public class DynamicLengthField implements Field {

    @NonNull StaticLengthField fieldContainingLength;
    @NonNull String describtion;

    @Override
    public int getMaxNrOfBytes() {
        val nrBits = fieldContainingLength.getNrOfBytes() * 8;
        val nrOfPossibleValues = 1L << nrBits;
        val maxNrOfBytes = nrOfPossibleValues - 1;

        return Long.valueOf(maxNrOfBytes).intValue();
    }
}