
package parser;

import java.util.ArrayList;
import java.util.List;

public class Query {

    public String operation;
    public String tableName;
    public String conditionColumn;
    public String conditionValue;
    public String updateColumn;
    public String updateValue;  
    public List<String> columnNames = new ArrayList<>();
    public List<String> dataTypes = new ArrayList<>();
    public List<String> values = new ArrayList<>();

    public void display() {
        System.out.println("Operation: " + operation);
        System.out.println("Table: " + tableName);
    }
}
