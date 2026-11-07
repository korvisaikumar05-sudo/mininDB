
package executor;

import parser.Query;
import storage.StorageEngine;

public class QueryExecutor {

    private StorageEngine storageEngine = new StorageEngine();

    public String execute(Query query) {

        switch (query.operation) {

            case "CREATE TABLE":
                return storageEngine.createTable(query);

            case "INSERT":
                return storageEngine.insert(query);

            case "SELECT":
                return storageEngine.select(query);

            case "UPDATE":
                return storageEngine.update(query);

            case "DELETE":
                return storageEngine.delete(query);

            default:
                return "Unsupported operation";
        }
    }
}
