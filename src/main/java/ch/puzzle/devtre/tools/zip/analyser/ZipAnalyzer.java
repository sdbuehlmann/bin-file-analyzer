package ch.puzzle.devtre.tools.zip.analyser;

import ch.puzzle.devtre.tools.zip.analyser.model.Field;
import lombok.SneakyThrows;
import lombok.val;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Optional;

public class ZipAnalyzer {


    @SneakyThrows
    public void analyze(final File zipFile) {

        RandomAccessFile raf = new RandomAccessFile(zipFile, "r");

        val maxNrOfBytesEocd = ZipTables.EndOfCentralDirectory.EOCD.getMaxNrOfBytes();
        val buffer = new byte[maxNrOfBytesEocd];
        val sizeOfFile = zipFile.length();

        if (sizeOfFile > maxNrOfBytesEocd) {
            // Seek to the beginning of EOCD
            raf.seek(sizeOfFile - maxNrOfBytesEocd);
            raf.read(buffer, 0, maxNrOfBytesEocd);
        } else {
            // file is smaller than max size of EOCD -> read whole file
            raf.read(buffer, 0, Long.valueOf(sizeOfFile).intValue());
        }

        val eocdData = new TableData(findEOCDData(buffer));
        val readableTable = new ReadableTable(ZipTables.EndOfCentralDirectory.EOCD, eocdData);

        for (Field field : ZipTables.EndOfCentralDirectory.EOCD.getFields()) {
            val fieldValue = readableTable.tryGetValueOfField(field)
                    .map(integer -> String.format("0x%X", integer))
                    .orElse("n/a");

            val fieldInfo = readableTable.tryFindStartIndexOfField(field)
                    .map(fieldInformation -> String.format(
                            "StartIndex: %d, NrOfBytes: %d",
                            fieldInformation.getStartIndex(),
                            fieldInformation.getNrOfBytes()))
                    .orElse("n/a");

            System.out.printf("%s: %s (%s)%n",
                    field.getDescription(),
                    fieldValue,
                    fieldInfo
            );
        }
    }

    private byte[] findEOCDData(final byte[] data) {
        val signature = ZipTables.EndOfCentralDirectory.SIGNATURE.getValue();

        val tableStartIndex = tryFindSignatureStartIndex(signature, data)
                .orElseThrow(() -> new IllegalArgumentException("Data does not match to table"));

        val eocdData = Arrays.copyOfRange(data, tableStartIndex, data.length);

        return eocdData;
    }

    private Optional<Integer> tryFindSignatureStartIndex(final int signature, final byte[] data) {
        byte b3 = (byte) ((signature >> 24));
        byte b2 = (byte) ((signature >> 16) & 255);
        byte b1 = (byte) ((signature >> 8) & 255);
        byte b0 = (byte) ((signature) & 255);

        for (int i = data.length - 1; i >= 3; i--) {
            if (data[i] == b3) {
                if (data[i - 1] == b2) {
                    if (data[i - 2] == b1) {
                        if (data[i - 3] == b0) {
                            return Optional.of(i - 3);
                        }
                    }
                }
            }
        }

        return Optional.empty();
    }
}
