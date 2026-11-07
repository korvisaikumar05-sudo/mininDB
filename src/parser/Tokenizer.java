
package parser;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    public static List<String> tokenize(String sql) {

        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < sql.length(); i++) {

            char ch = sql.charAt(i);

            if (Character.isWhitespace(ch)) {

                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }

            } else if (ch == '\'' || ch == '"') {

                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }

                char quote = ch;
                StringBuilder value = new StringBuilder();

                i++;

                while (i < sql.length() && sql.charAt(i) != quote) {
                    value.append(sql.charAt(i));
                    i++;
                }

                tokens.add("'" + value.toString() + "'");

            } else if ("(),;*".indexOf(ch) != -1) {

                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }

                tokens.add(String.valueOf(ch));

            } else if ("=<>!".indexOf(ch) != -1) {

                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }

                String operator = String.valueOf(ch);

                if (i + 1 < sql.length() &&
                    (sql.charAt(i + 1) == '=' ||
                    (ch == '<' && sql.charAt(i + 1) == '>'))) {

                    operator += sql.charAt(++i);
                }

                tokens.add(operator);

            } else {

                current.append(ch);
            }
        }

        if (current.length() > 0) {
            tokens.add(current.toString());
        }

        return tokens;
    }

    public static void main(String[] args) {

        String sql = "CREATE TABLE students (id INT, name VARCHAR(50));";

        List<String> tokens = tokenize(sql);

        for (String token : tokens) {
            System.out.println(token);
        }
    }
}
