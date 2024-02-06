package ch.puzzle.devtre.tools.zip.analyser.tables.interpreted;

import java.util.Arrays;
import java.util.Set;

public enum CompressionMethod {

    THE_FILE_IS_STORED_NO_COMPRESSION(0),
    THE_FILE_IS_SHRUNK(1),
    THE_FILE_IS_REDUCED_WITH_COMPRESSION_FACTOR_1(2),
    THE_FILE_IS_REDUCED_WITH_COMPRESSION_FACTOR_2(3),
    THE_FILE_IS_REDUCED_WITH_COMPRESSION_FACTOR_3(4),
    THE_FILE_IS_REDUCED_WITH_COMPRESSION_FACTOR_4(5),
    THE_FILE_IS_IMPLODED(6),
    RESERVED_FOR_TOKENIZING_COMPRESSION_ALGORITHM(7),
    THE_FILE_IS_DEFLATED(8),
    ENHANCED_DEFLATING_USING_DEFLATE_64(9),
    PKWARE_DATA_COMPRESSION_LIBRARY_IMPLODING(10),
    RESERVED_BY_PKWARE(11, 13, 15, 17),
    FILE_IS_COMPRESSED_USING_BZIP_2_ALGORITHM(12),
    LZMA(14),
    IBM_Z_OS_CMPSC_COMPRESSION(16),
    FILE_IS_COMPRESSED_USING_IBM_TERSE(18),
    IBM_LZ_77_ZARCHITECTURE(19),
    DEPRECATED_USE_METHOD_93_FOR_ZSTD(20),
    ZSTANDARD(93),
    MP_3_COMPRESSION(94),
    XZ_COMPRESSION(95),
    JPEG_VARIANT(96),
    WAVPACK_COMPRESSED_DATA(97),
    PPMD_VERSION_I_REV_1(98),
    AE_XENCRYPTION_MARKER(99);


    private final Set<Integer> values;

    CompressionMethod(Integer... values) {
        this.values = Set.of(values);
    }

    public static CompressionMethod fromValue(final int value) {
        return Arrays.stream(CompressionMethod.values())
                .filter(compressionMethod -> compressionMethod.values.contains(value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "Value %d is not supported",
                        value
                )));
    }
}
