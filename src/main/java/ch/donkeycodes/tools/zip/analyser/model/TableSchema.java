package ch.donkeycodes.tools.zip.analyser.model;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class TableSchema {
    @Singular
    List<Field> fields;

    public int getMaxNrOfBytes() {
        return fields.stream()
                .mapToInt(Field::getMaxNrOfBytes)
                .sum();
    }
}
