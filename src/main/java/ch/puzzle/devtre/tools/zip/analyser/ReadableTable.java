package ch.puzzle.devtre.tools.zip.analyser;

import ch.puzzle.devtre.tools.utils.CastHelper;
import ch.puzzle.devtre.tools.zip.analyser.model.DynamicLengthField;
import ch.puzzle.devtre.tools.zip.analyser.model.Field;
import ch.puzzle.devtre.tools.zip.analyser.model.StaticField;
import ch.puzzle.devtre.tools.zip.analyser.model.TableSchema;
import lombok.val;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadableTable {
    private final TableSchema tableSchema;
    private final TableData tableData;

    private final Map<Field, Integer> startIndexesByFields;

    public ReadableTable(TableSchema tableSchema, TableData tableData) {
        this.tableSchema = tableSchema;
        this.tableData = tableData;

        startIndexesByFields = findStartIndexesOfFields();
    }

    public int tryGetValueOfField(final Field field) {
        return 0;
    }

    public Optional<Integer> tryFindStartIndexOfField(final Field field) {
        return Optional.ofNullable(startIndexesByFields.get(field));
    }

    private Map<Field, Integer> findStartIndexesOfFields() {
        AtomicInteger index = new AtomicInteger();
        final Map<Field, Integer> indexes = new HashMap<>();

        for (Field field : tableSchema.getFields()) {
            // special case: The length is of a field is stored in additional field
            CastHelper.tryCast(DynamicLengthField.class, field)
                    .ifPresent(dynamicLengthField -> {

                        val referencedField = dynamicLengthField.getFieldContainingLength();
                        val startIndexOfReferencedField = Optional.ofNullable(indexes.get(referencedField))
                                .orElseThrow(() -> new IllegalArgumentException("Referenced field is not available"));
                        val length = tableData.readData(startIndexOfReferencedField, referencedField.getNrOfBytes());

                        indexes.put(field, index.get());
                        index.addAndGet(length);
                    });

            // normal case: The length of the fields is available
            CastHelper.tryCast(StaticField.class, field)
                    .ifPresent(staticField -> {
                        indexes.put(field, index.getAndAdd(staticField.getNrOfBytes()));
                    });
        }

        return indexes;
    }
}
