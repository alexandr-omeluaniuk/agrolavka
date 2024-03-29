package ss.martin.telegram.bot.formatter;

import java.util.ArrayList;
import java.util.List;
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
        this.tableWidth = table.tableWidth;
        this.columns = table.rows[0].cells.length;
        this.lengthMap = lengthMap();
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
            buff.append(printCell(cells[i].text, i));
        }
        buff.append("\n");
        sb.append(buff);
        line();
    }
    
    private void printRow(final Row row) {
        final var buff = new StringBuilder(table.columnSeparator);
        final var tokensList = breakRowText(row);
        var lineNum = 0;
        var maxLineNum = tokensList.get(0).size();
        while (lineNum < maxLineNum) {
            for (int i = 0; i < columns; i++) {
                buff.append(printCell(tokensList.get(i).get(lineNum), i));
            }
            lineNum++;
            buff.append("\n").append(table.columnSeparator);
        }
        buff.setLength(buff.length() - 1);
        sb.append(buff);
    }
    
    private List<List<String>> breakRowText(final Row row) {
        final var result = new ArrayList<List<String>>(columns);
        final var textsMap = new String[columns];
        for (int i = 0; i < columns; i++) {
            result.add(new ArrayList<>());
            textsMap[i] = row.cells[i].text == null ? "" : row.cells[i].text;
        }
        while (true) {
            var countComplete = 0;
            for (int i = 0; i < columns; i++) {
                final var text = textsMap[i];
                final var len = lengthMap[i];
                if (text.length() > len) {
                    final var token = text.substring(0, len);
                    textsMap[i] = text.substring(len);
                    result.get(i).add(token);
                } else {
                    textsMap[i] = "";
                    result.get(i).add(text);
                    countComplete++;
                }
            }
            if (countComplete == columns) {
                break;
            }
        }
        return result;
    }
    
    private StringBuilder printCell(final String text, int colNum) {
        final var buff = new StringBuilder();
        buff.append(" ".repeat(table.paddingOfWidth));
        buff.append(cellText(text, colNum));
        buff.append(" ".repeat(table.paddingOfWidth)).append(table.columnSeparator);
        return buff;
    }
    
    private String cellText(final String text, final int columnNum) {
        var temp = text == null ? "" : text.trim();
        final var headerCell = table.header.cells[columnNum];
        final var len = lengthMap[columnNum];
        if (temp.length() > len) {
            return temp.substring(0, len);
        } else {
            while (temp.length() < len) {
                if (headerCell.align == Align.LEFT) {
                    temp = temp + " ";
                } else {
                    temp = " " + temp;
                }
            }
        }
        return temp;
    }
    
    private void line() {
        if (!sb.toString().endsWith("\n")) {
            sb.append("\n");
        }
        sb.append("-".repeat(tableWidth)).append("\n");
    }
    
    private int[] lengthMap() {
        final var map = new int[columns];
        for (int i = 0; i < columns; i++) {
            final var cell = table.header.cells[i];
            if (cell.width == HeaderCell.WIDTH_AUTO) {
                map[i] = HeaderCell.WIDTH_AUTO;
            } else if (cell.width == HeaderCell.WIDTH_MAX_VAL) {
                if (cell.text != null && map[i] < cell.text.length()) {
                    map[i] = cell.text.length();
                }
            } else if (cell.width > 0) {
                map[i] = cell.width;
            }
        }
        for (final Row row : table.rows) {
            for (int colNum = 0; colNum < row.cells.length; colNum++) {
                final var cell = row.cells[colNum];
                final var headerCell = table.header.cells[colNum];
                if (headerCell.width == HeaderCell.WIDTH_AUTO) {
                    map[colNum] = HeaderCell.WIDTH_AUTO;
                } else if (headerCell.width == HeaderCell.WIDTH_MAX_VAL) {
                    if (cell.text != null && map[colNum] < cell.text.length()) {
                        map[colNum] = cell.text.length();
                    }
                } else {
                    map[colNum] = headerCell.width;
                }
            }
        }
        final var paddingAndBorderWidth = (table.paddingOfWidth * 2 * columns) 
            + (table.columnSeparator.length() * columns + 1);
        for (int i = 0; i < columns; i++) {
            if (map[i] == HeaderCell.WIDTH_AUTO) {
                final var separatorPadding = table.columnSeparator.length() == 0 ? 1 : 0;
                map[i] = tableWidth - sumPositive(map) - paddingAndBorderWidth + separatorPadding;
            }
        }
        return map;
    }
    
    private int sumPositive(int[] map) {
        var sumPositive = 0;
        for (int len : map) {
            if (len > 0) {
                sumPositive += len;
            }
        }
        return sumPositive;
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
