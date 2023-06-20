package ss.martin.telegram.bot.formatter;

import java.util.stream.Stream;
import ss.martin.telegram.bot.exception.FormatException;

/**
 * Table formatter.
 * @author alex
 */
public class TableFormatter {
    
    private Table table;
    
    private final int columns;
    
    private final int[] lengthMap;
    
    private final int tableWidth;
    
    private final StringBuilder sb = new StringBuilder();
    
    public TableFormatter(final Table table) {
        this.table = table;
        this.columns = table.rows[0].cells.length;
        this.lengthMap = lengthMap();
        this.tableWidth = table.tableWidth;
    }
    
    public String format() {
        line();
        printHeader();
        Stream.of(table.rows).forEach(row -> {
            printRow(row);
            line();
        });
        return sb.toString();
    }
    
    private void printHeader() {
        final var buff = new StringBuilder(table.columnSeparator);
        final var cells = table.header.cells;
        for (int i = 0; i < cells.length; i++) {
            final var cell = cells[i];
            final var len = lengthMap[i];
            final var cellText = headerCellText(cell, len);
            buff.append(" ".repeat(table.paddingOfWidth));
            buff.append(cellText);
            buff.append(" ".repeat(table.paddingOfWidth)).append(table.columnSeparator);
        }
        buff.append("\n");
        sb.append(buff);
        line();
    }
    
    private String headerCellText(final HeaderCell cell, final Integer maxLength) {
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
    
    private void printRow(final Row row) {
        final var buff = new StringBuilder(table.columnSeparator);
        for (int i = 0; i < row.cells.length; i++) {
            final var cell = row.cells[i];
            final var len = lengthMap[i];
            final var cellText = cellText(cell, len);
            buff.append(" ".repeat(table.paddingOfWidth));
            buff.append(cellText);
            buff.append(" ".repeat(table.paddingOfWidth)).append(table.columnSeparator);
        }
        buff.append("\n");
        sb.append(buff);
    }
    
    private String cellText(final Cell cell, final Integer maxLength) {
        var temp = cell.text == null ? "" : cell.text;
        while (temp.length() < maxLength) {
            //if (cell.align == Align.LEFT) {
                temp = temp + " ";
//            } else {
//                temp = " " + temp;
//            }
        }
        return temp;
    }
    
    private void line() {
        sb.append("-".repeat(tableWidth)).append("\n");
    }
    
    private int[] lengthMap() {
        final var map = new int[columns];
        for (int i = 0; i < columns; i++) {
            final var cell = table.header.cells[i];
            if (cell.text != null && map[i] < cell.text.length()) {
                map[i] = cell.text.length();
            }
        }
        for (final Row row : table.rows) {
            for (int colNum = 0; colNum < row.cells.length; colNum++) {
                final var cell = row.cells[colNum];
                if (cell.text != null && map[colNum] < cell.text.length()) {
                    map[colNum] = cell.text.length();
                }
            }
        }
//        int sum = 0;
//        for (int i : map) {
//            sum += i;
//        }
        return map;
    }
    
    public static record Table(
        Header header,
        Row[] rows,
        int tableWidth,
        int paddingOfWidth,
        String columnSeparator
    ) {
        public Table(final Header header, final Row[] rows, final int tableWidth) {
            this(header, rows, tableWidth, 1, "|");
        }
    }
    
    public static record Header(
        HeaderCell[] cells
    ) {}
    
    public static record HeaderCell(
        String text,
        Align align,
        int width
    ) {
        
        public static final int WIDTH_MAX_VAL = 0;
        
        public static final int WIDTH_AUTO = -1;
        
        public HeaderCell(final String text) {
            this(text, Align.LEFT, WIDTH_MAX_VAL);
        }
        
        public HeaderCell(final String text, final Align align) {
            this(text, align, WIDTH_MAX_VAL);
        }
    }
    
    public static record Row(
        Cell[] cells
    ) {}
    
    public static record Cell(
        String text
    ) {}
    
    public static enum Align {
        LEFT, RIGHT;
    }
}
