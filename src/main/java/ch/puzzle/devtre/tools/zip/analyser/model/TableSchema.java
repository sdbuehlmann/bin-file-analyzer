package ch.puzzle.devtre.tools.zip.analyser.model;

import ch.puzzle.devtre.tools.utils.CastHelper;
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

    public int getSignature() { // TODO: Remove
        return CastHelper.tryCast(StaticValueField.class, fields.get(0))
                .map(StaticValueField::getValue)
                .orElseThrow(() -> new IllegalStateException("No static value field as signature at the beginning of the table"));
    }
}
