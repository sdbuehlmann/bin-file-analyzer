package ch.donkeycodes.tools.zip.analyser;

import ch.donkeycodes.tools.zip.analyser.ZipTables.EndOfCentralDirectory;
import ch.donkeycodes.tools.zip.analyser.tables.CentralDirectoryFileHeader;
import lombok.SneakyThrows;
import lombok.val;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ZipAnalyzer {


    @SneakyThrows
    public void analyze(final File zipFile) {

        RandomAccessFile raf = new RandomAccessFile(zipFile, "r");

        val maxNrOfBytesEocd = EndOfCentralDirectory.EOCD.getMaxNrOfBytes();
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
        val readableTable = new ReadableTable(EndOfCentralDirectory.EOCD, eocdData);
        val eocd = EndOfCentralDirectory.read(readableTable);
        val fileHeaders = interpretCentralDir(raf, eocd);

        val interpreted = fileHeaders.stream()
                .map(CentralDirectoryFileHeader::interpret)
                .collect(Collectors.toList());

        val invalidCompressed = interpreted.stream()
                .filter(interpreted1 -> interpreted1.getFileName().equals("content/schema0/table5/lob3/record39.txt"))
                .findAny();

        System.out.println(eocd);
    }

    private List<CentralDirectoryFileHeader> interpretCentralDir(final RandomAccessFile fileData, final EndOfCentralDirectory eocd) throws IOException {
        val buffer = new byte[eocd.getSizeOfCentralDirInBytes()];

        fileData.seek(eocd.getOffsetToStartOfCentralDir());
        fileData.read(buffer, 0, eocd.getSizeOfCentralDirInBytes());

        final AtomicReference<byte[]> remainingDataRef = new AtomicReference<>(buffer);

        val fileHeaders = IntStream.range(0, eocd.getNrOfCentralDirRecordsOnThisDisk())
                .mapToObj(i -> {
                    val data = new TableData(remainingDataRef.get());

                    val readableTable = new ReadableTable(CentralDirectoryFileHeader.TABLE_SCHEMA, data);

                    val remainingData = data.dropFirstNBytes(readableTable.getSize());
                    remainingDataRef.set(remainingData);

                    return CentralDirectoryFileHeader.read(readableTable);
                })
                .collect(Collectors.toList());

        return fileHeaders;
    }

    private byte[] findEOCDData(final byte[] data) {
        val signature = EndOfCentralDirectory.SIGNATURE.getValue();

        val tableData = new TableData(data);
        val tableStartIndex = tableData
                .tryFindLastSignatureStartIndex(signature)
                .orElseThrow(() -> new IllegalArgumentException("Data does not match to table"));

        val eocdData = Arrays.copyOfRange(data, tableStartIndex, data.length);

        return eocdData;
    }
}
