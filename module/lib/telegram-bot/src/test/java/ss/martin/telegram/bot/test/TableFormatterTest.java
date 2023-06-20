package ss.martin.telegram.bot.test;

import org.junit.jupiter.api.Test;
import ss.martin.telegram.bot.formatter.TableFormatter;
import static ss.martin.telegram.bot.formatter.TableFormatter.*;

public class TableFormatterTest {
    
    @Test
    public void testFormatTable() {
        final var header = new Row(new Cell[] {
            new Cell("Наименование", Align.LEFT),
            new Cell("Обьем", Align.RIGHT),
            new Cell("Количество", Align.RIGHT),
            new Cell("Цена", Align.RIGHT),
            new Cell("Сумма", Align.RIGHT),
        });
        final var table = new Table(new Row[] {
            header,
            new Row(new Cell[] {
                new Cell("Комбикорм 123 супер", Align.LEFT, 2),
                new Cell("20", Align.RIGHT),
                new Cell("50.00 BYN", Align.RIGHT),
                new Cell("1000.00 BYN", Align.RIGHT)
            }),
            new Row(new Cell[] {
                new Cell("Семена льна", Align.LEFT, 2),
                new Cell("2", Align.RIGHT),
                new Cell("40.00 BYN", Align.RIGHT),
                new Cell("80.00 BYN", Align.RIGHT)
            })
        }, 1, "|");
        
        final var result = TableFormatter.formatTable(table);
        
        System.out.println(result);
    }
}
