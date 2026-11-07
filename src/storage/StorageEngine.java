
package storage;

import parser.Query;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ArrayList;

public class StorageEngine {

    private DiskManager diskManager = new DiskManager();

    public String createTable(Query query) {

        StringBuilder metadata = new StringBuilder();
        metadata.append("TABLE=").append(query.tableName).append("\n");

        for (int i = 0; i < query.columnNames.size(); i++) {
            metadata.append(query.columnNames.get(i))
                    .append(":")
                    .append(query.dataTypes.get(i))
                    .append("\n");
        }

        try {
            String metaFile = query.tableName + ".meta";
            String dataFile = query.tableName + ".data";

            if (diskManager.exists(metaFile)) {
                return "Table already exists";
            }

            diskManager.write(metaFile,
                metadata.toString().getBytes(StandardCharsets.UTF_8));

            diskManager.write(dataFile, new byte[0]);

            return "Table created successfully";

        } catch (IOException e) {
            return "Storage Error: " + e.getMessage();
        }
    }

    public String insert(Query query) {

        try {
            Path path = Path.of("data", query.tableName + ".data");

            if (!Files.exists(path)) {
                return "Table does not exist";
            }

            String record = String.join("|", query.values) + "\n";

            Files.writeString(path, record, StandardOpenOption.APPEND);

            return "1 row inserted";

        } catch (IOException e) {
            return "Insert Error: " + e.getMessage();
        }
    }

    public String select(Query query) {

        try {
            Path path = Path.of("data", query.tableName + ".data");

            if (!Files.exists(path)) {
                return "Table does not exist";
            }

            List<String> records = Files.readAllLines(path);

            if (records.isEmpty()) {
                return "No records found";
            }

            StringBuilder result = new StringBuilder();

            for (String record : records) {
                result.append(record.replace("|", " | "))
                      .append("\n");
            }

            return result.toString().trim();

        } catch (IOException e) {
            return "Select Error: " + e.getMessage();
        }
    }

    public String update(Query query) {

        try {
            Path path = Path.of("data", query.tableName + ".data");
            Path meta = Path.of("data", query.tableName + ".meta");

            if (!Files.exists(path) || !Files.exists(meta)) {
                return "Table does not exist";
            }

            List<String> metadata = Files.readAllLines(meta);

            int conditionIndex = -1;
            int updateIndex = -1;

            for (int i = 1; i < metadata.size(); i++) {
                String column = metadata.get(i).split(":")[0];

                if (column.equalsIgnoreCase(query.conditionColumn))
                    conditionIndex = i - 1;

                if (column.equalsIgnoreCase(query.updateColumn))
                    updateIndex = i - 1;
            }

            if (conditionIndex == -1 || updateIndex == -1) {
                return "Invalid column";
            }

            List<String> records = Files.readAllLines(path);
            int count = 0;

            for (int i = 0; i < records.size(); i++) {
                String[] values = records.get(i).split("\\|", -1);

                if (values.length > conditionIndex &&
                    values[conditionIndex].equalsIgnoreCase(query.conditionValue)) {

                    values[updateIndex] = query.updateValue;
                    records.set(i, String.join("|", values));
                    count++;
                }
            }

            Files.write(path, records);
            return count + " row(s) updated";

        } catch (Exception e) {
            return "Update Error: " + e.getMessage();
        }
    }

    public String delete(Query query) {

        try {
            Path path = Path.of("data", query.tableName + ".data");
            Path meta = Path.of("data", query.tableName + ".meta");

            if (!Files.exists(path) || !Files.exists(meta)) {
                return "Table does not exist";
            }

            List<String> metadata = Files.readAllLines(meta);
            int conditionIndex = -1;

            for (int i = 1; i < metadata.size(); i++) {
                String column = metadata.get(i).split(":")[0];

                if (column.equalsIgnoreCase(query.conditionColumn)) {
                    conditionIndex = i - 1;
                    break;
                }
            }

            if (conditionIndex == -1) {
                return "Invalid column";
            }

            List<String> records = Files.readAllLines(path);
            int oldSize = records.size();

            final int index = conditionIndex;

            records.removeIf(record -> {
                String[] values = record.split("\\|", -1);
                return values.length > index &&
                    values[index].equalsIgnoreCase(query.conditionValue);
            });

            Files.write(path, records);

            return (oldSize - records.size()) + " row(s) deleted";

        } catch (Exception e) {
            return "Delete Error: " + e.getMessage();
        }
    }
}
