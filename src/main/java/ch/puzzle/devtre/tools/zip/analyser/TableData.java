package ch.puzzle.devtre.tools.zip.analyser;

import lombok.Value;
import lombok.val;

import java.util.Arrays;
import java.util.Optional;

@Value
public class TableData {

    byte[] bytes;

    public int readLittleEndian(int startIndex, int nrOfBytes) {
        if (nrOfBytes > 4) {
            throw new IllegalArgumentException("A int is limited to 4 bytes");
        }

        int value = 0;

        // little endian -> LSB first
        for(int processedBytes = 0; processedBytes < nrOfBytes; processedBytes++) {
            val currentIndex = startIndex + processedBytes;
            val currentValue = this.bytes[currentIndex];
            val nrOfShiftedBits = processedBytes * 8;
            val maskedAndShiftedValue = (currentValue & 0xFF) << (nrOfShiftedBits);

            value |= maskedAndShiftedValue;
        }

        return value;
    }


    public byte[] getData(int startIndex, int nrOfBytes) {
        if (nrOfBytes < 1) {
            return new byte[0];
        }

        return Arrays.copyOfRange(bytes, startIndex, startIndex + nrOfBytes);
    }

    public byte[] dropFirstNBytes(final int nrOfBytesToDrop) {
        return Arrays.copyOfRange(bytes, nrOfBytesToDrop, bytes.length);
    }

    public Optional<Integer> tryFindLastSignatureStartIndex(final int signature) {
        // big endian -> msb first
        // little endian -> lsb first
        byte b3 = (byte) ((signature >> 24)); // most significant byte
        byte b2 = (byte) ((signature >> 16) & 255);
        byte b1 = (byte) ((signature >> 8) & 255);
        byte b0 = (byte) ((signature) & 255); // least significant

        for (int i = bytes.length - 1; i >= 3; i--) {
            // zip is stored in little-endian, but here the signature is searched from the bottom -> msb is closer to bottom in little-endian
            if (bytes[i] == b3) {
                if (bytes[i - 1] == b2) {
                    if (bytes[i - 2] == b1) {
                        if (bytes[i - 3] == b0) {
                            return Optional.of(i - 3);
                        }
                    }
                }
            }
        }

        return Optional.empty();
    }
}
