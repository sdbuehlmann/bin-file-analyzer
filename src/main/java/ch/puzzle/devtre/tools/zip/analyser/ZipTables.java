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

    @Builder
    @Value
    public static class CentralDirectoryFileHeader {
        public static final StaticValueField SIGNATURE = new StaticValueField(4, "Signature", 0x02014b50);
        public static final StaticLengthField VERSION_MADE_BY = new StaticLengthField(2, "Version made by");
        public static final StaticLengthField MIN_VERSION_NEEDED_TO_EXTRACT = new StaticLengthField(2, "Minimum version needed to extract");
        public static final StaticLengthField BIT_FLAG = new StaticLengthField(2, "Bit flag");
        public static final StaticLengthField COMPRESSION_METHOD = new StaticLengthField(2, "Compression method");
        public static final StaticLengthField FILE_LAST_MODIFICATION_TIME = new StaticLengthField(2, "File last modification time (MS-DOS format)");
        public static final StaticLengthField FILE_LAST_MODIFICATION_DATE = new StaticLengthField(2, "File last modification date (MS-DOS format)");
        public static final StaticLengthField CRC32_OF_UNCOMPRESSED_DATA = new StaticLengthField(4, "CRC-32 of uncompressed data");
        public static final StaticLengthField COMPRESSED_SIZE = new StaticLengthField(4, "Compressed size");
        public static final StaticLengthField UNCOMPRESSED_SIZE = new StaticLengthField(4, "Uncompressed size");
        public static final StaticLengthField FILE_NAME_LENGTH = new StaticLengthField(2, "File name length");
        public static final StaticLengthField EXTRA_FIELD_LENGTH = new StaticLengthField(2, "Extra field length (m)");
        public static final StaticLengthField FILE_COMMENT_LENGTH = new StaticLengthField(2, "File comment length (k)");
        public static final StaticLengthField DISK_NUMBER_WHERE_FILE_STARTS = new StaticLengthField(2, "Disk number where file starts");
        public static final StaticLengthField INTERNAL_FILE_ATTRIBUTES = new StaticLengthField(2, "Internal file attributes");
        public static final StaticLengthField EXTERNAL_FILE_ATTRIBUTES = new StaticLengthField(4, "External file attributes");
        public static final StaticLengthField OFFSET_OF_LOCAL_FILE_HEADER = new StaticLengthField(4, "Offset of local file header (from start of disk)");
        public static final DynamicLengthField FILE_NAME = new DynamicLengthField(FILE_NAME_LENGTH, "File name");
        public static final DynamicLengthField EXTRA_FIELD = new DynamicLengthField(EXTRA_FIELD_LENGTH, "Extra field");
        public static final DynamicLengthField FILE_COMMENT = new DynamicLengthField(FILE_COMMENT_LENGTH, "File comment");

        public static final TableSchema TABLE_SCHEMA = TableSchema.builder()
                .field(SIGNATURE)
                .field(VERSION_MADE_BY)
                .field(MIN_VERSION_NEEDED_TO_EXTRACT)
                .field(BIT_FLAG)
                .field(COMPRESSION_METHOD)
                .field(FILE_LAST_MODIFICATION_TIME)
                .field(FILE_LAST_MODIFICATION_DATE)
                .field(CRC32_OF_UNCOMPRESSED_DATA)
                .field(COMPRESSED_SIZE)
                .field(UNCOMPRESSED_SIZE)
                .field(FILE_NAME_LENGTH)
                .field(EXTRA_FIELD_LENGTH)
                .field(FILE_COMMENT_LENGTH)
                .field(DISK_NUMBER_WHERE_FILE_STARTS)
                .field(INTERNAL_FILE_ATTRIBUTES)
                .field(EXTERNAL_FILE_ATTRIBUTES)
                .field(OFFSET_OF_LOCAL_FILE_HEADER)
                .field(FILE_NAME)
                .field(EXTRA_FIELD)
                .field(FILE_COMMENT)
                .build();

        int signature;
        int versionMadeBy;
        int minVersionNeededToExtract;
        int bitFlag;
        int compressionMethod;
        int fileLastModificationTime;
        int fileLastModificationDate;
        int crc32OfUncompressedData;
        int compressedSize;
        int uncompressedSize;
        int fileNameLength;
        int extraFieldLength;
        int fileCommentLength;
        int diskNumberWhereFileStarts;
        int internalFileAttributes;
        int externalFileAttributes;
        int offsetOfLocalFileHeader;
        byte[] fileName;
        byte[] extraField;
        byte[] fileComment;

        public static CentralDirectoryFileHeader read(final ReadableTable readableTable) {
            return CentralDirectoryFileHeader.builder()
                    .signature(readableTable.getValueOfField(SIGNATURE))
                    .versionMadeBy(readableTable.getValueOfField(VERSION_MADE_BY))
                    .minVersionNeededToExtract(readableTable.getValueOfField(MIN_VERSION_NEEDED_TO_EXTRACT))
                    .bitFlag(readableTable.getValueOfField(BIT_FLAG))
                    .compressionMethod(readableTable.getValueOfField(COMPRESSION_METHOD))
                    .fileLastModificationTime(readableTable.getValueOfField(FILE_LAST_MODIFICATION_TIME))
                    .fileLastModificationDate(readableTable.getValueOfField(FILE_LAST_MODIFICATION_DATE))
                    .crc32OfUncompressedData(readableTable.getValueOfField(CRC32_OF_UNCOMPRESSED_DATA))
                    .compressedSize(readableTable.getValueOfField(COMPRESSED_SIZE))
                    .uncompressedSize(readableTable.getValueOfField(UNCOMPRESSED_SIZE))
                    .fileNameLength(readableTable.getValueOfField(FILE_NAME_LENGTH))
                    .extraFieldLength(readableTable.getValueOfField(EXTRA_FIELD_LENGTH))
                    .fileCommentLength(readableTable.getValueOfField(FILE_COMMENT_LENGTH))
                    .diskNumberWhereFileStarts(readableTable.getValueOfField(DISK_NUMBER_WHERE_FILE_STARTS))
                    .internalFileAttributes(readableTable.getValueOfField(INTERNAL_FILE_ATTRIBUTES))
                    .externalFileAttributes(readableTable.getValueOfField(EXTERNAL_FILE_ATTRIBUTES))
                    .offsetOfLocalFileHeader(readableTable.getValueOfField(OFFSET_OF_LOCAL_FILE_HEADER))
                    .fileName(readableTable.getValueOfField(FILE_NAME))
                    .extraField(readableTable.getValueOfField(EXTRA_FIELD))
                    .fileComment(readableTable.getValueOfField(FILE_COMMENT))
                    .build();
        }
    }
}
