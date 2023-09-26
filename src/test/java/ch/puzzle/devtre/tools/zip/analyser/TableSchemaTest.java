package ch.puzzle.devtre.tools.zip.analyser;

import ch.puzzle.devtre.tools.zip.analyser.model.DynamicLengthField;
import ch.puzzle.devtre.tools.zip.analyser.model.StaticLengthField;
import ch.puzzle.devtre.tools.zip.analyser.model.StaticValueField;
import ch.puzzle.devtre.tools.zip.analyser.model.TableSchema;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TableSchemaTest {

    public static final StaticValueField STATIC_VALUE_FIELD_A = new StaticValueField(4, "Static value field A", 0x06054b50);
    public static final StaticLengthField STATIC_LENGTH_FIELD_A = new StaticLengthField(2, "Static length field A");
    public static final StaticLengthField STATIC_LENGTH_FIELD_LA = new StaticLengthField(2, "Static length field with length of dynamic length field A");
    public static final StaticLengthField STATIC_LENGTH_FIELD_LB = new StaticLengthField(4, "Static length field with length of dynamic length field B");

    public static final DynamicLengthField DYNAMIC_LENGTH_FIELD_A = new DynamicLengthField(STATIC_LENGTH_FIELD_LA, "Dynamic length field A");
    public static final DynamicLengthField DYNAMIC_LENGTH_FIELD_B = new DynamicLengthField(STATIC_LENGTH_FIELD_LB, "Dynamic length field B");


    public static final TableSchema TABLE = TableSchema.builder()
            .field(STATIC_VALUE_FIELD_A)
            .field(STATIC_LENGTH_FIELD_A)
            .field(STATIC_LENGTH_FIELD_LA)
            .field(STATIC_LENGTH_FIELD_LB)
            .field(DYNAMIC_LENGTH_FIELD_A)
            .field(DYNAMIC_LENGTH_FIELD_B)
            .build();

    @Test
    public void getMaxNrOfBytes() {
        // given

        // when
        val maxNrOfBytes = TABLE.getMaxNrOfBytes();

        // then
        Assertions.assertThat(maxNrOfBytes)
                .isEqualTo(
                        STATIC_VALUE_FIELD_A.getNrOfBytes() +
                                STATIC_LENGTH_FIELD_A.getNrOfBytes() +
                                STATIC_LENGTH_FIELD_LA.getNrOfBytes() +
                                STATIC_LENGTH_FIELD_LB.getNrOfBytes() +
                                0xffff +
                                0xffffffff
                );
    }

}