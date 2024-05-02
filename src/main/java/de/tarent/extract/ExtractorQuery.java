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

import java.util.HashMap;
import java.util.Map;

public class ExtractorQuery {
    private String sql;
    private String orderBy;
    private Map<String, ColumnMapping> mappings = new HashMap<>();
    private int progressInterval = 500;

    public String getSql() {
        return sql;
    }

    public void setSql(final String sql) {
        this.sql = sql;
    }

    public void setMappings(final Map<String, ColumnMapping> mappings) {
        this.mappings = mappings;
    }

    public Map<String, ColumnMapping> getMappings() {
        return mappings;
    }

    public int getProgressInterval() {
        return progressInterval;
    }

    @SuppressWarnings("unused")
    public void setProgressInterval(final int progressInterval) {
        this.progressInterval = progressInterval;
    }

    public String getOrderBy() {
        return orderBy;
    }

    @SuppressWarnings("unused")
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
