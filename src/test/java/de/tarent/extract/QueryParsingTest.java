package de.tarent.extract;

/*-
 * Extract-Tool is Copyright
 *  © 2015, 2016, 2018 Lukas Degener (l.degener@qvest-digital.com)
 *  © 2018, 2019, 2020 mirabilos (t.glaser@qvest-digital.com)
 *  © 2015 Jens Oberender (j.oberender@tarent.de)
 * Licensor: Qvest Digital AG, Bonn, Germany
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.*;

public class QueryParsingTest {
    @Before
    public void setUp() {
    }

    @Test
    public void simpleQueryWithoutOrderBy() throws IOException {
        InputStreamReader reader = new InputStreamReader(
          Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("test-query.yaml")));
        ExtractorQuery descriptor = mapper().readValue(reader, ExtractorQuery.class);
        Map<String, ?> properties = descriptor.getMappings().get("fnarz").getProperties();
        assertEquals(4, properties.size());
        assertEquals("bang", properties.get("bar"));
        assertEquals("42", properties.get("foo"));
        assertEquals(42, properties.get("fee"));
        assertTrue(properties.get("baz") instanceof Map);
        assertEquals("SELECT fnarz FROM knusper WHERE alles ist cool", descriptor.getSql());
    }

    @Test
    public void sameQueryWithOrderBy() throws IOException {
        InputStreamReader reader = new InputStreamReader(
          Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("test-query-with-orderBy.yaml")));
        ExtractorQuery descriptor = mapper().readValue(reader, ExtractorQuery.class);
        Map<String, ?> properties = descriptor.getMappings().get("fnarz").getProperties();
        assertEquals(4, properties.size());
        assertEquals("bang", properties.get("bar"));
        assertEquals("42", properties.get("foo"));
        assertEquals(42, properties.get("fee"));
        assertTrue(properties.get("baz") instanceof Map);
        assertEquals("SELECT fnarz\nFROM knusper\nWHERE alles ist cool\n", descriptor.getSql());
        assertEquals("fnarz ASC", descriptor.getOrderBy());
    }

    private ObjectMapper mapper() {
        return YAMLMapper.builder()
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .configure(MapperFeature.AUTO_DETECT_CREATORS, true)
          .configure(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS, true)
          .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true)
          .build();
    }
}
