package ch.puzzle.devtre.tools.zip.analyser.tables;

import ch.puzzle.devtre.tools.utils.IntegerUtils;
import ch.puzzle.devtre.tools.zip.analyser.ReadableTable;
import ch.puzzle.devtre.tools.zip.analyser.model.DynamicLengthField;
import ch.puzzle.devtre.tools.zip.analyser.model.StaticLengthField;
import ch.puzzle.devtre.tools.zip.analyser.model.StaticValueField;
import ch.puzzle.devtre.tools.zip.analyser.model.TableSchema;
import ch.puzzle.devtre.tools.zip.analyser.tables.interpreted.CompressionMethod;
import lombok.Builder;
import lombok.Value;
import lombok.val;

import java.util.Arrays;
import java.util.Optional;

@Builder
@Value
public class CentralDirectoryFileHeader {
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

    public static Interpreted interpret(final CentralDirectoryFileHeader cdfh) {
        val versionMadeBy = Interpreted.VersionMadeBy.builder()
                .fileAttributesCompatibility(Interpreted.VersionMadeBy.FileAttributesCompatibility
                        .fromValue(IntegerUtils.getByte(cdfh.versionMadeBy, 1)))
                .zipSpecVersion(Interpreted.Version.from(cdfh.getVersionMadeBy()))
                .build();

        return Interpreted.builder()
                .versionMadeBy(versionMadeBy)
                .minVersionNeededToExtract(Interpreted.Version.from(cdfh.getMinVersionNeededToExtract()))
                .compressionMethod(CompressionMethod.fromValue(cdfh.getCompressionMethod()))
                .fileName(new String(cdfh.getFileName()))
                .build();
    }

    @Builder
    @Value
    public static class Interpreted {
        VersionMadeBy versionMadeBy;
        Version minVersionNeededToExtract;
        int bitFlag;
        CompressionMethod compressionMethod;
        int fileLastModificationTime;
        int fileLastModificationDate;
        int crc32OfUncompressedData;
        int compressedSize;
        int uncompressedSize;

        int diskNumberWhereFileStarts;
        int internalFileAttributes;
        int externalFileAttributes;
        int offsetOfLocalFileHeader;

        String fileName;
        byte[] extraField;
        String fileComment;

        @Override
        public String toString() {
            return "Header of File " + fileName;
        }

        @Value
        public static class Version {
            int major;
            int minor;

            @Override
            public String toString() {
                return major + "." + minor;
            }

            public static Version from(final int value) {
                return new Version(
                        IntegerUtils.getByte(value, 0) / 10,
                        IntegerUtils.getByte(value, 0) % 10
                );
            }
        }

        @Builder
        @Value
        public static class VersionMadeBy {
            FileAttributesCompatibility fileAttributesCompatibility;
            Version zipSpecVersion;

            public enum FileAttributesCompatibility {
                FAT_VFAT_FAT32_FILE_SYSTEMS(0),
                AMIGA(1),
                OPEN_VMS(2),
                UNIX(3),
                VM_CMS(4),
                ATTARI_ST(5),
                OS2_HPFS(6),
                MACINTOSH(7),
                Z_SYSTEMS(8),
                CP_M(9),
                WINDOWS_NTFS(10),
                MVS(11),
                VSE(12),
                ACRON_RISC(13),
                VFAT(14),
                ALTERNATIVE_MVS(15),
                BE_OS(16),
                TANDEM(17),
                OS_400(18),
                OS_X(19),
                UNKNOWN;


                private final Optional<Integer> value;

                FileAttributesCompatibility(int value) {
                    this.value = Optional.of(value);
                }

                FileAttributesCompatibility() {
                    this.value = Optional.empty();
                }

                public static FileAttributesCompatibility fromValue(final int value) {
                    return Arrays.stream(FileAttributesCompatibility.values())
                            .filter(compatibility -> compatibility.value
                                    .stream()
                                    .anyMatch(integer -> integer.equals(value)))
                            .findAny()
                            .orElse(FileAttributesCompatibility.UNKNOWN);
                }
            }
        }
    }
}