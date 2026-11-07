
package parser;

import java.util.List;

public class SQLParser {

    private List<String> tokens;
    private int position = 0;

    public SQLParser(List<String> tokens) {
        this.tokens = tokens;
    }

    private String current() {
        return position < tokens.size() ? tokens.get(position) : null;
    }

    private String consume() {
        if (current() == null) {
            throw new IllegalArgumentException("Unexpected end of query");
        }
        return tokens.get(position++);
    }

    private void expect(String expected) {
        String actual = consume();

        if (!actual.equalsIgnoreCase(expected)) {
            throw new IllegalArgumentException(
                "Expected " + expected + ", found " + actual
            );
        }
    }

    public Query parse() {

        Query query = new Query();

        String command = consume();

        if (command.equalsIgnoreCase("CREATE")) {

            expect("TABLE");
            query.operation = "CREATE TABLE";
            query.tableName = consume();
            expect("(");

            while (true) {
                String column = consume();
                String type = consume();

                if (type.equalsIgnoreCase("VARCHAR")) {
                    expect("(");
                    type += "(" + consume();
                    expect(")");
                    type += ")";
                }

                query.columnNames.add(column);
                query.dataTypes.add(type);

                if (",".equals(current())) {
                    consume();
                } else {
                    break;
                }
            }

            expect(")");

        } else if (command.equalsIgnoreCase("INSERT")) {

            expect("INTO");
            query.operation = "INSERT";
            query.tableName = consume();
            expect("VALUES");
            expect("(");

            while (true) {
                query.values.add(consume());

                if (",".equals(current())) {
                    consume();
                } else {
                    break;
                }
            }

            expect(")");

        } else if (command.equalsIgnoreCase("SELECT")) {

            expect("*");
            expect("FROM");
            query.operation = "SELECT";
            query.tableName = consume();

        } else if (command.equalsIgnoreCase("UPDATE")) {

            query.operation = "UPDATE";
            query.tableName = consume();

            expect("SET");
            query.updateColumn = consume();
            expect("=");
            query.updateValue = consume();

            expect("WHERE");
            query.conditionColumn = consume();
            expect("=");
            query.conditionValue = consume();

        } else if (command.equalsIgnoreCase("DELETE")) {

            expect("FROM");
            query.operation = "DELETE";
            query.tableName = consume();

            expect("WHERE");
            query.conditionColumn = consume();
            expect("=");
            query.conditionValue = consume();

        } else {
            throw new IllegalArgumentException("Unsupported SQL command");
        }

        if (";".equals(current())) {
            consume();
        }

        if (current() != null) {
            throw new IllegalArgumentException("Unexpected token: " + current());
        }

        return query;
    }
}
