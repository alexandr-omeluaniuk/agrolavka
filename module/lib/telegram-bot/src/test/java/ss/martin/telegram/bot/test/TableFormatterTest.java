package ss.martin.telegram.bot.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ss.martin.telegram.bot.formatter.TableFormatter;
import static ss.martin.telegram.bot.formatter.TableFormatter.*;

public class TableFormatterTest {
    
    @Test
    public void testFormatTable() {
        final var header = new Header(new HeaderCell[] {
            new HeaderCell("Наименование", Align.LEFT, HeaderCell.WIDTH_AUTO),
            new HeaderCell("Обьем", Align.RIGHT),
            new HeaderCell("Кол-во", Align.RIGHT),
            new HeaderCell("Цена", Align.RIGHT),
            new HeaderCell("Сумма", Align.RIGHT)
        });
        final var table = new Table(header, new Row[] {
            new Row(new Cell[] {
                new Cell("Комбикорм 123 супер натуральный"),
                new Cell(null),
                new Cell("20"),
                new Cell("50.00"),
                new Cell("1000.00")
            }),
            new Row(new Cell[] {
                new Cell("Семена льна"),
                new Cell(null),
                new Cell("2"),
                new Cell("40.00"),
                new Cell("80.00")
            })
        }, 60, 1, "");
        
        final var result = new TableFormatter(table).format();
        
        Assertions.assertFalse(result.isBlank());
    }
}
