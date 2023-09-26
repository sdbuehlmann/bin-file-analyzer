package ch.puzzle.devtre.tools.zip.analyser;

import ch.puzzle.devtre.tools.zip.analyser.model.DynamicLengthField;
import ch.puzzle.devtre.tools.zip.analyser.model.StaticLengthField;
import ch.puzzle.devtre.tools.zip.analyser.model.StaticValueField;
import ch.puzzle.devtre.tools.zip.analyser.model.TableSchema;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

public class ReadableTableTest {

    private static final int FIELD_1_VALUE = 0x06054b50;
    private static final int FIELD_2_VALUE = 0xABCDEF;
    private static final int FIELD_3_VALUE = 4;
    private static final int FIELD_4_VALUE = 0x11072409;
    private static final int FIELD_5_VALUE = 0x0123;

    public static final StaticValueField FIELD_1 = new StaticValueField(4, "Field 1", FIELD_1_VALUE);
    public static final StaticLengthField FIELD_2 = new StaticLengthField(3, "Field 2");
    public static final StaticLengthField FIELD_3 = new StaticLengthField(2, "Field 3");
    public static final DynamicLengthField FIELD_4 = new DynamicLengthField(FIELD_3, "Field 4");
    public static final StaticLengthField FIELD_5 = new StaticLengthField(2, "Field 5");


    private final TableSchema tableSchemaMock = Mockito.mock(TableSchema.class);
    private final TableData tableDataMock = Mockito.mock(TableData.class);

    private ReadableTable readableTable;

    @BeforeEach
    void setUp() {
        Mockito.when(tableSchemaMock.getFields()).thenReturn(List.of(
                FIELD_1,
                FIELD_2,
                FIELD_3,
                FIELD_4,
                FIELD_5
        ));
    }

    @Test
    public void tryFindStartIndexOfField_forField1() {
        // given
        Mockito.when(tableDataMock.readData(Mockito.eq(7), Mockito.eq(FIELD_3.getNrOfBytes()))).thenReturn(FIELD_3_VALUE); // just value of field 3 is needed to determine all indexes
        readableTable = new ReadableTable(tableSchemaMock, tableDataMock);

        // when
        val startIndex = readableTable.tryFindStartIndexOfField(FIELD_1);

        //then
        Assertions.assertThat(startIndex).contains(0);
    }

    @Test
    public void tryFindStartIndexOfField_forField3() {
        // given
        Mockito.when(tableDataMock.readData(Mockito.eq(7), Mockito.eq(FIELD_3.getNrOfBytes()))).thenReturn(FIELD_3_VALUE); // just value of field 3 is needed to determine all indexes
        readableTable = new ReadableTable(tableSchemaMock, tableDataMock);

        // when
        val startIndex = readableTable.tryFindStartIndexOfField(FIELD_3);

        //then
        Assertions.assertThat(startIndex)
                .contains(
                        FIELD_1.getNrOfBytes() +
                                FIELD_2.getNrOfBytes()
                );
    }

    @Test
    public void tryFindStartIndexOfField_forField5() {
        // given
        Mockito.when(tableDataMock.readData(Mockito.eq(7), Mockito.eq(FIELD_3.getNrOfBytes()))).thenReturn(FIELD_3_VALUE); // just value of field 3 is needed to determine all indexes
        readableTable = new ReadableTable(tableSchemaMock, tableDataMock);

        // when
        val startIndex = readableTable.tryFindStartIndexOfField(FIELD_5);

        //then
        Assertions.assertThat(startIndex)
                .contains(
                        FIELD_1.getNrOfBytes() +
                                FIELD_2.getNrOfBytes() +
                                FIELD_3.getNrOfBytes() +
                                FIELD_3_VALUE
                );
    }

    public void blub() {
        Mockito.when(tableDataMock.readData(Mockito.eq(0), Mockito.eq(FIELD_1.getNrOfBytes()))).thenReturn(FIELD_1_VALUE);
        Mockito.when(tableDataMock.readData(Mockito.eq(4), Mockito.eq(FIELD_2.getNrOfBytes()))).thenReturn(FIELD_2_VALUE);
        Mockito.when(tableDataMock.readData(Mockito.eq(7), Mockito.eq(FIELD_3.getNrOfBytes()))).thenReturn(FIELD_3_VALUE);
        Mockito.when(tableDataMock.readData(Mockito.eq(9), Mockito.eq(FIELD_3_VALUE))).thenReturn(FIELD_4_VALUE);
        Mockito.when(tableDataMock.readData(Mockito.eq(13), Mockito.eq(FIELD_5.getNrOfBytes()))).thenReturn(FIELD_5_VALUE);
    }

}