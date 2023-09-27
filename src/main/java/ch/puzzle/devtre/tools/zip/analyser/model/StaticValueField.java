package ch.puzzle.devtre.tools.zip.analyser.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class StaticValueField implements StaticField {

    @NonNull int nrOfBytes;
    @NonNull String description;
    @NonNull int value;


    @Override
    public int getMaxNrOfBytes() {
        return nrOfBytes;
    }
}
