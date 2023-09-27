package ch.puzzle.devtre.tools.zip.analyser;

import ch.puzzle.devtre.tools.zip.analyser.model.DynamicLengthField;
import ch.puzzle.devtre.tools.zip.analyser.model.Field;
import ch.puzzle.devtre.tools.zip.analyser.model.StaticField;
import ch.puzzle.devtre.tools.zip.analyser.model.TableSchema;
import lombok.Builder;
import lombok.Value;
import lombok.val;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadableTable {
    private final TableSchema tableSchema;
    private final TableData tableData;

    private final Map<Field, FieldInformation> startIndexesByFields;

    public ReadableTable(TableSchema tableSchema, TableData tableData) {
        this.tableSchema = tableSchema;
        this.tableData = tableData;

        startIndexesByFields = findStartIndexesOfFields();
    }

    public int getValueOfField(final StaticField field) {
        return tryGetValueOfField(field)
                .orElseThrow(() -> new IllegalStateException(String.format("No value for field \"%s\" found", field.getDescription())));
    }

    public Optional<Integer> tryGetValueOfField(final StaticField field) {
        return tryFindStartIndexOfField(field)
                .map(fieldInformation -> tableData.readLittleEndian(fieldInformation.getStartIndex(), fieldInformation.getNrOfBytes()));
    }

    public byte[] getValueOfField(final DynamicLengthField field) {
        return tryGetValueOfField(field)
                .orElseThrow(() -> new IllegalStateException(String.format("No value for field \"%s\" found", field.getDescription())));
    }

    public Optional<byte[]> tryGetValueOfField(final DynamicLengthField field) {
        return tryFindStartIndexOfField(field)
                .map(fieldInformation -> tableData.getData(fieldInformation.getStartIndex(), fieldInformation.getNrOfBytes()));
    }

    public Optional<FieldInformation> tryFindStartIndexOfField(final Field field) {
        return Optional.ofNullable(startIndexesByFields.get(field));
    }

    public int getSize() {
        val lastField = tableSchema.getFields().get(tableSchema.getFields().size() - 1);
        val lastFieldInfo = tryFindStartIndexOfField(lastField)
                .orElseThrow(() -> new IllegalStateException(String.format("No value for field \"%s\" found", lastField.getDescription())));

        return lastFieldInfo.getStartIndex() + lastFieldInfo.getNrOfBytes();
    }

    private Map<Field, FieldInformation> findStartIndexesOfFields() {
        AtomicInteger index = new AtomicInteger();
        Map<Field, FieldInformation> startIndexesByFields = new HashMap<>();

        for (Field field : tableSchema.getFields()) {
            if (field instanceof DynamicLengthField dynamicLengthField) {
                // special case: The length is of a field is stored in additional field
                val referencedField = dynamicLengthField.getFieldContainingLength();

                val startIndexOfReferencedField = Optional.ofNullable(startIndexesByFields.get(referencedField))
                        .orElseThrow(() -> new IllegalArgumentException("Referenced field is not available"));

                val valueReferencedField = tableData.readLittleEndian(
                        startIndexOfReferencedField.getStartIndex(),
                        referencedField.getNrOfBytes());

                startIndexesByFields.put(field, FieldInformation.builder()
                        .startIndex(index.getAndAdd(valueReferencedField))
                        .field(field)
                        .nrOfBytes(valueReferencedField)
                        .build());

            } else if (field instanceof StaticField staticField) {
                // normal case: The length of the fields is available
                startIndexesByFields.put(field, FieldInformation.builder()
                        .startIndex(index.getAndAdd(staticField.getNrOfBytes()))
                        .field(field)
                        .nrOfBytes(staticField.getNrOfBytes())
                        .build());
            } else {
                throw new UnsupportedOperationException(String.format("Field of type %s is not supported", field.getClass()));
            }
        }

        return Collections.unmodifiableMap(startIndexesByFields);
    }

    @Builder
    @Value
    public static class FieldInformation {
        Field field;
        int startIndex;
        int nrOfBytes;
    }
}
