package io.github.donnie4w.jdao;

import io.github.donnie4w.jdao.handle.JdaoException;
import io.github.donnie4w.jdao.util.Serializer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class SerializerTest {

    @Test
    public void testSerializationAndDeserialization() throws JdaoException {
        Map<String, Object> testData = new HashMap<>();
        testData.put("bool", true);
        testData.put("string", "Hello, World!");
        testData.put("double", 3.1415926);
        testData.put("float", 2.718f);
        testData.put("long", 1234567890L);
        testData.put("integer", 98765);
        testData.put("short", (short) 1234);
        testData.put("byte", (byte) 12);
        testData.put("char", 'A');
        testData.put("bigDecimal", new BigDecimal("1234567890123456789.1234567890123456789"));
        testData.put("bigInteger", new BigInteger("1234567890123456789"));
        testData.put("date", new Date());
        testData.put("byteArray", new byte[]{1, 2, 3, 4, 5});

        Serializer serializer = new Serializer();
        byte[] encodedData = Serializer.encode(testData);

        Map<String, Object> decodedData = Serializer.decode(encodedData);

        assertEquals(testData.size(), decodedData.size());
        for (Map.Entry<String, Object> entry : testData.entrySet()) {
            Object originalValue = entry.getValue();
            Object decodedValue = decodedData.get(entry.getKey());

            if (originalValue instanceof Boolean) {
                assertEquals(originalValue, decodedValue);
            } else if (originalValue instanceof String) {
                assertEquals(originalValue, decodedValue);
            } else if (originalValue instanceof Double) {
                assertEquals(((Double) originalValue), ((Double) decodedValue), 0.00001);
            } else if (originalValue instanceof Float) {
                assertEquals(((Float) originalValue), ((Float) decodedValue), 0.00001f);
            } else if (originalValue instanceof Long || originalValue instanceof Integer ||
                    originalValue instanceof Short || originalValue instanceof Byte) {
                assertEquals(originalValue, decodedValue);
            } else if (originalValue instanceof Character) {
                assertEquals(originalValue, decodedValue);
            } else if (originalValue instanceof BigDecimal) {
                assertEquals(0, ((BigDecimal) originalValue).compareTo((BigDecimal) decodedValue));
            } else if (originalValue instanceof BigInteger) {
                assertEquals(originalValue, decodedValue);
            } else if (originalValue instanceof Date) {
                assertEquals(((Date) originalValue).getTime(), ((Date) decodedValue).getTime());
            } else if (originalValue instanceof byte[]) {
                assertArrayEquals((byte[]) originalValue, (byte[]) decodedValue);
            }
        }
    }
}
