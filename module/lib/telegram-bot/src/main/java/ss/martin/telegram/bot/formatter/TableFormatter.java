package ss.martin.telegram.bot.formatter;

import java.util.stream.Stream;

/**
 * Table formatter.
 * @author alex
 */
public class TableFormatter {
    
    public static String formatTable(final Table table) {
        final var sb = new StringBuilder();
        final var columns = table.rows[0].cells.length;
        final var lengthMap = lengthMap(table, columns);
        final var wPadding = table.paddingOfWidth * columns * 2;
        final var tableWidth = Stream.of(lengthMap).reduce(0, Integer::sum) + columns + 1 + wPadding;
        sb.append(line(tableWidth));
        Stream.of(table.rows).forEach(row -> {
            sb.append(printRow(row, lengthMap, table));
            sb.append(line(tableWidth));
        });
        return sb.toString();
    }
    
    private static String printRow(final Row row, final Integer[] lengthMap, final Table table) {
        final var sb = new StringBuilder(table.columnSeparator);
        for (int i = 0; i < row.cells.length; i++) {
            final var cell = row.cells[i];
            final var len = columnWidth(cell, i, lengthMap, table);
            final var cellText = cellText(cell, len);
            sb.append(" ".repeat(table.paddingOfWidth));
            sb.append(cellText);
            sb.append(" ".repeat(table.paddingOfWidth)).append(table.columnSeparator);
        }
        sb.append("\n");
        return sb.toString();
    }
    
    private static int columnWidth(final Cell cell, final int columnNum, final Integer[] lengthMap, final Table table) {
        final var colspan = cell.colSpan;
        var len = 0;
        for (int i = columnNum; i < columnNum + colspan; i++) {
            len += lengthMap[i];
        }
        len += table.paddingOfWidth * (colspan - 1) * 2;
        len += table.columnSeparator.length() * (colspan - 1);
        return len;
    }
    
    private static String cellText(final Cell cell, final Integer maxLength) {
        var temp = cell.text == null ? "" : cell.text;
        while (temp.length() < maxLength) {
            if (cell.align == Align.LEFT) {
                temp = temp + " ";
            } else {
                temp = " " + temp;
            }
        }
        return temp;
    }
    
    private static String line(final Integer width) {
        return "-".repeat(width) + "\n";
    }
    
    private static Integer[] lengthMap(final Table tableData, final int columns) {
        final var lengthMap = new Integer[columns];
        for (int i = 0; i < lengthMap.length; i++) {
            lengthMap[i] = 0;
        }
        for (Row row : tableData.rows) {
            for (int colNum = 0; colNum < row.cells.length; colNum++) {
                final var cell = row.cells[colNum];
                if (cell.text != null && lengthMap[colNum] < cell.text.length()) {
                    lengthMap[colNum] = cell.text.length();
                }
            }
        }
        return lengthMap;
    }
    
    public static record Table(
        Row[] rows,
        int paddingOfWidth,
        String columnSeparator
    ) {
        public Table(Row[] rows) {
            this(rows, 1, "|");
        }
    }
    
    public static record Row(
        Cell[] cells
    ) {}
    
    public static record Cell(
        String text,
        Align align,
        int colSpan
    ) {
        public Cell(String text) {
            this(text, Align.LEFT, 1);
        }
        
        public Cell(String text, Align align) {
            this(text, align, 1);
        }
    }
    
    public static enum Align {
        LEFT, RIGHT;
    }
}
