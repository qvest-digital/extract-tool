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

import org.evolvis.tartools.backgroundjobs.BackgroundJobMonitor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

public class DefaultRowFetcher implements RowFetcher {
    private final JdbcTemplate jdbcTemplate;

    public DefaultRowFetcher(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @SuppressWarnings("SqlNoDataSourceInspection")
    public void fetch(final ExtractorQuery query, final BackgroundJobMonitor monitor,
      final RowPrinter printer, final RowProcessor rowProcessor) {

        final String sql = query.getOrderBy() == null ? query.getSql() : query.getSql() + " ORDER BY " + query.getOrderBy();
        jdbcTemplate.query(sql, (ResultSetExtractor<Void>) rs -> {
            int rownum = 0;
            while (rs.next()) {
                rowProcessor.processRow(rs, printer);
                if (rownum++ % query.getProgressInterval() == 0) {
                    monitor.reportProgressAbsolute(rownum);
                }
            }
            monitor.reportProgressAbsolute(rownum);
            return null;
        });
    }
}
