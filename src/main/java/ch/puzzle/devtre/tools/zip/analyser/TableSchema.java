package ch.puzzle.devtre.tools.zip.analyser;

import ch.puzzle.devtre.tools.utils.CastHelper;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import lombok.val;

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

    public int getSignature() {
        return CastHelper.tryCast(StaticValueField.class, fields.get(0))
                .map(StaticValueField::getValue)
                .orElseThrow(() -> new IllegalStateException("No static value field as signature at the beginning of the table"));
    }

    public interface Field {
        int getMaxNrOfBytes();
    }

    public interface StaticField {
        int getNrOfBytes();
    }

    @Value
    public static class StaticLengthField implements Field, StaticField {
        @NonNull int nrOfBytes;
        @NonNull String describtion;


        @Override
        public int getMaxNrOfBytes() {
            return nrOfBytes;
        }
    }

    @Value
    public static class StaticValueField implements Field, StaticField {
        @NonNull int nrOfBytes;
        @NonNull String describtion;
        @NonNull int value;


        @Override
        public int getMaxNrOfBytes() {
            return nrOfBytes;
        }
    }

    @Value
    public static class DynamicLengthField implements Field {
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
}



