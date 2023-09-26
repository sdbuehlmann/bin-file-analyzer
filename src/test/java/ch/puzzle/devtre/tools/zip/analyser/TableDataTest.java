package ch.puzzle.devtre.tools.zip.analyser;


import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TableDataTest {

    private static final byte[] BYTES_ALL_FF = {
            (byte)0xFF,
            (byte)0xFF,
            (byte)0xFF,
            (byte)0xFF
    };

    private static final byte[] BYTES_ALL_ZERO = {
            (byte)0,
            (byte)0,
            (byte)0,
            (byte)0
    };

    private static final byte[] BYTES_DIFFERENT_VALUES = {
            (byte)0x2A,
            (byte)0x38,
            (byte)0x01,
            (byte)0xF7
    };

    @Test
    public void readData_withFourBytesWith255Value_expectMergedValue() {
        // given
        val tableData = new TableData(BYTES_ALL_FF);

        // when
        val value = tableData.readData(0, 4);

        //then
        Assertions.assertThat(value).isEqualTo(0xFFFFFFFF);
    }

    @Test
    public void readData_withFourBytesWith0Value_expectMergedValue() {
        // given
        val tableData = new TableData(BYTES_ALL_ZERO);

        // when
        val value = tableData.readData(0, 4);

        //then
        Assertions.assertThat(value).isEqualTo(0);
    }

    @Test
    public void readData_withOneBytesWithDifferentValues_expectMergedValue() {
        // given
        val tableData = new TableData(BYTES_DIFFERENT_VALUES);

        // when
        val value = tableData.readData(1, 1);

        //then
        Assertions.assertThat(value).isEqualTo(0x38);
    }

    @Test
    public void readData_withTwoBytesWithDifferentValues_expectMergedValue() {
        // given
        val tableData = new TableData(BYTES_DIFFERENT_VALUES);

        // when
        val value = tableData.readData(1, 2);

        //then
        Assertions.assertThat(value).isEqualTo(0x3801);
    }

    @Test
    public void readData_withThreeBytesWithDifferentValues_expectMergedValue() {
        // given
        val tableData = new TableData(BYTES_DIFFERENT_VALUES);

        // when
        val value = tableData.readData(1, 3);

        //then
        Assertions.assertThat(value).isEqualTo(0x3801F7);
    }
}