package Utils;

import java.lang.reflect.Field;
import java.util.*;

public class EntityMapper {

    public static <T> List<T> mapToEntity(List<Object[]> rows, Class<T> clazz) {
        List<T> result = new ArrayList<>();

        // Step 1: Get all field names from the class
        Field[] fields = clazz.getDeclaredFields();
        List<String> columnNames = new ArrayList<>();
        for (Field field : fields) {
            columnNames.add(field.getName());
        }

        for (Object[] row : rows) {
            try {
                T obj = clazz.getDeclaredConstructor().newInstance();

                for (int i = 0; i < columnNames.size(); i++) {
                    if (i >= row.length) break; // Avoid IndexOutOfBounds
                    String col = columnNames.get(i);
                    Object value = row[i];

                    Field field = clazz.getDeclaredField(col);
                    field.setAccessible(true);

                    // Optional: type conversion (timestamp to LocalDateTime, etc.)
                    if (field.getType().getName().equals("java.time.LocalDateTime") && value instanceof java.sql.Timestamp) {
                        field.set(obj, ((java.sql.Timestamp) value).toLocalDateTime());
                    } else if (field.getType().getName().equals("java.time.LocalDate") && value instanceof java.sql.Date) {
                        field.set(obj, ((java.sql.Date) value).toLocalDate());
                    } else {
                        field.set(obj, value);
                    }
                }

                result.add(obj);

            } catch (Exception e) {
                e.printStackTrace(); // helpful for debugging
            }
        }

        return result;
    }
}
