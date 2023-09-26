package ch.puzzle.devtre.tools.zip.analyser;

import lombok.Value;
import lombok.val;

@Value
public class TableData {

    byte[] bytes;

    public int readData(int startIndex, int nrOfBytes) {
        int value = 0;

        for(int processedBytes = 0; processedBytes < nrOfBytes; processedBytes++) {
            val currentIndex = (startIndex + nrOfBytes - 1) - processedBytes;
            val currentValue = this.bytes[currentIndex];
            val nrOfShiftedBits = processedBytes * 8;
            val maskedAndShiftedValue = (currentValue & 0xFF) << (nrOfShiftedBits);

            value |= maskedAndShiftedValue;
        }

        return value;
    }
}
