package ch.puzzle.devtre.tools.zip.analyser;

import ch.puzzle.devtre.tools.zip.analyser.model.TableSchema;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ReadableTableTest {

    private final TableData tableDataMock = Mockito.mock(TableData.class);
    private final TableSchema tableSchemaMock = Mockito.mock(TableSchema.class);

    private final ReadableTable readableTable = new ReadableTable(tableSchemaMock, tableDataMock);

    @Test
    public void readData_withFourBytesWith255Value_expectMergedValue() {
        // given

        // when
        //readableTable.tryFindStartIndexOfField()

        //then

    }

}