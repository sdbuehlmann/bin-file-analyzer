package ch.puzzle.devtre.tools.zip.analyser;


import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TableDataTest {

    private static final int SIGNATURE = 0xAB110137;

    private static final byte[] DATA = {
            (byte)0xFF, // 0
            (byte)0xFF, // 1
            (byte)0xFF, // 2
            (byte)0xFF, // 3
            (byte)0x2A, // 4
            (byte)0x38, // 5

            //---------------- // signature stored in little-endian
            (byte)0x37, // 6
            (byte)0x01, // 7
            (byte)0x11, // 8
            (byte)0xAB, // 9
            //----------------

            (byte)0xF7, // 10

            //----------------  // signature stored in little-endian
            (byte)0x37, // 11
            (byte)0x01, // 12
            (byte)0x11, // 13
            (byte)0xAB, // 14
            //----------------

            (byte)42,   // 15
            (byte)0,    // 16
            (byte)0,    // 17
            (byte)0,    // 18
            (byte)0     // 19
    };

    @Test
    public void tryFindLastSignatureStartIndex() {
        // given
        val tableData = new TableData(DATA);

        // when
        val value = tableData.tryFindLastSignatureStartIndex(SIGNATURE);

        //then
        Assertions.assertThat(value).contains(11);
    }

    @Test
    public void readLittleEndian() {
        // given
        val tableData = new TableData(DATA);

        // when
        val value = tableData.readLittleEndian(11, 4);

        //then
        Assertions.assertThat(value).isEqualTo(SIGNATURE);
    }
}