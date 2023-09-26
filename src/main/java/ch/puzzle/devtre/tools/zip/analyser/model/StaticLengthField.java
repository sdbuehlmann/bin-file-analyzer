package ch.puzzle.devtre.tools.zip.analyser.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class StaticLengthField implements Field, StaticField {

    @NonNull int nrOfBytes;
    @NonNull String describtion;


    @Override
    public int getMaxNrOfBytes() {
        return nrOfBytes;
    }
}
