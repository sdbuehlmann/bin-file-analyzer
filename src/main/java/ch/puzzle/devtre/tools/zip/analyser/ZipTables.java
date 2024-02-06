package ch.puzzle.devtre.tools.zip.analyser;

import ch.puzzle.devtre.tools.zip.analyser.model.DynamicLengthField;
import ch.puzzle.devtre.tools.zip.analyser.model.StaticLengthField;
import ch.puzzle.devtre.tools.zip.analyser.model.StaticValueField;
import ch.puzzle.devtre.tools.zip.analyser.model.TableSchema;
import lombok.Builder;
import lombok.Value;

public class ZipTables {

    @Builder
    @Value
    public static class EndOfCentralDirectory {
        public static final StaticValueField SIGNATURE = new StaticValueField(4, "Signature", 0x06054b50);
        public static final StaticLengthField NR_OF_THIS_DISK = new StaticLengthField(2, "Number of this disk");
        public static final StaticLengthField DISK_WHERE_CENTRAL_DIR_STARTS = new StaticLengthField(2, "Disk where central directory starts");
        public static final StaticLengthField NR_OF_CENTRAL_DIR_RECORDS_ON_THIS_DISK = new StaticLengthField(2, "Numbers of central directory records on this disk");
        public static final StaticLengthField TOTAL_NR_OF_CENTRAL_DIR_RECORDS = new StaticLengthField(2, "Total number of central directory records");
        public static final StaticLengthField SIZE_OF_CENTRAL_DIR_IN_BYTES = new StaticLengthField(4, "Size of central directory in bytes");
        public static final StaticLengthField OFFSET_TO_START_OF_CENTRAL_DIR = new StaticLengthField(4, "Offset to start of central directory");
        public static final StaticLengthField COMMENT_LENGTH = new StaticLengthField(2, "Comment length");
        public static final DynamicLengthField COMMENT = new DynamicLengthField(COMMENT_LENGTH, "Comment");


        public static final TableSchema EOCD = TableSchema.builder()
                .field(SIGNATURE)
                .field(NR_OF_THIS_DISK)
                .field(DISK_WHERE_CENTRAL_DIR_STARTS)
                .field(NR_OF_CENTRAL_DIR_RECORDS_ON_THIS_DISK)
                .field(TOTAL_NR_OF_CENTRAL_DIR_RECORDS)
                .field(SIZE_OF_CENTRAL_DIR_IN_BYTES)
                .field(OFFSET_TO_START_OF_CENTRAL_DIR)
                .field(COMMENT_LENGTH)
                .field(COMMENT)
                .build();

        int signature;
        int nrOfThisDisk;
        int diskWhereCentralDirStarts;
        int nrOfCentralDirRecordsOnThisDisk;
        int totalNrOfCentralDirRecords;
        int sizeOfCentralDirInBytes;
        int offsetToStartOfCentralDir;
        int commentLength;
        byte[] comment;

        public static EndOfCentralDirectory read(final ReadableTable readableTable) {
            return EndOfCentralDirectory.builder()
                    .signature(readableTable.getValueOfField(SIGNATURE))
                    .nrOfThisDisk(readableTable.getValueOfField(NR_OF_THIS_DISK))
                    .diskWhereCentralDirStarts(readableTable.getValueOfField(DISK_WHERE_CENTRAL_DIR_STARTS))
                    .nrOfCentralDirRecordsOnThisDisk(readableTable.getValueOfField(NR_OF_CENTRAL_DIR_RECORDS_ON_THIS_DISK))
                    .totalNrOfCentralDirRecords(readableTable.getValueOfField(TOTAL_NR_OF_CENTRAL_DIR_RECORDS))
                    .sizeOfCentralDirInBytes(readableTable.getValueOfField(SIZE_OF_CENTRAL_DIR_IN_BYTES))
                    .offsetToStartOfCentralDir(readableTable.getValueOfField(OFFSET_TO_START_OF_CENTRAL_DIR))
                    .commentLength(readableTable.getValueOfField(COMMENT_LENGTH))
                    .comment(readableTable.getValueOfField(COMMENT))
                    .build();
        }
    }
}
