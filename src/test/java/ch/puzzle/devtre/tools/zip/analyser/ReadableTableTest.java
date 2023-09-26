package ch.puzzle.devtre.tools.zip.analyser;

import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReadableTableTest {

    @Test
    public void readData_withFourBytesWith255Value_expectMergedValue() {
        // given
        val tableData = new TableData(new byte[]{}); // TODO

        // when
        val value = tableData.readData(0, 4);

        //then
        Assertions.assertThat(value).isEqualTo(0xFFFFFFFF);
    }

}