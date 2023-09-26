package ch.puzzle.devtre.tools.zip.analyser;

public class ZipTables {

    public static class EndOfCentralDirectory {
        public static final TableSchema.StaticValueField SIGNATURE = new TableSchema.StaticValueField(4, "Signature", 0x06054b50);
        public static final TableSchema.StaticLengthField NR_OF_THIS_DISK = new TableSchema.StaticLengthField(2, "Number of this disk");
        public static final TableSchema.StaticLengthField DISK_WHERE_CENTRAL_DIR_STARTS = new TableSchema.StaticLengthField(2, "Disk where central directory starts");
        public static final TableSchema.StaticLengthField NR_OF_CENTRAL_DIR_RECORDS_ON_THIS_DISK = new TableSchema.StaticLengthField(2, "Numbers of central directory records on this disk");
        public static final TableSchema.StaticLengthField TOTAL_NR_OF_CENTRAL_DIR_RECORDS = new TableSchema.StaticLengthField(2, "Total number of central directory records");
        public static final TableSchema.StaticLengthField SIZE_OF_CENTRAL_DIR_IN_BYTES = new TableSchema.StaticLengthField(4, "Size of central directory in bytes");
        public static final TableSchema.StaticLengthField OFFSET_TO_START_OF_CENTRAL_DIR = new TableSchema.StaticLengthField(4, "Offset to start of central directory");
        public static final TableSchema.StaticLengthField COMMENT_LENGTH = new TableSchema.StaticLengthField(2, "Comment length");
        public static final TableSchema.DynamicLengthField COMMENT = new TableSchema.DynamicLengthField(COMMENT_LENGTH, "Comment");


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
    }

}
