/*
 * Copyright (C) 2022 alex
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ss.agrolavka.service.impl;

import java.awt.Color;
import static java.awt.Color.LIGHT_GRAY;
import static java.awt.Color.WHITE;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vandeseer.easytable.TableDrawer;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;
import static org.vandeseer.easytable.settings.HorizontalAlignment.LEFT;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.TextCell;
import ss.agrolavka.service.OrderDocumentService;
import ss.entity.agrolavka.Order;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.*;
import static org.vandeseer.easytable.settings.HorizontalAlignment.*;
import static org.vandeseer.easytable.settings.VerticalAlignment.TOP;

/**
 *
 * @author alex
 */
@Service
class OrderDocumentServiceImpl implements OrderDocumentService {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(OrderDocumentServiceImpl.class);
    
        private final static Object[][] DATA = new Object[][]{
            {"Whisky", 134.0, 145.0},
            {"Beer",   768.0, 677.0},
            {"Gin",    456.2, 612.0},
            {"Vodka",  302.3, 467.0}
    };
        
            private final static Color BLUE_DARK = new Color(76, 129, 190);
    private final static Color BLUE_LIGHT_1 = new Color(186, 206, 230);
    private final static Color BLUE_LIGHT_2 = new Color(218, 230, 242);

    private final static Color GRAY_LIGHT_1 = new Color(245, 245, 245);
    private final static Color GRAY_LIGHT_2 = new Color(240, 240, 240);
    private final static Color GRAY_LIGHT_3 = new Color(216, 216, 216);

    @Override
    public byte[] generateOrderPdf(final Order order) throws Exception {
        try (PDDocument document = new PDDocument()) {
            document.getDocumentInformation().setAuthor("agrolavka.by");
            document.getDocumentInformation().setTitle("Заказ №" + order.getId());
            final PDPage page = new PDPage();
            addTable(document, page);
            document.addPage(page);
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            LOG.info("Order PDF " + order + " has been generated");
            return baos.toByteArray();
        }
    }
    
    private void addTable(final PDDocument document, final PDPage page) throws IOException {
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                final TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(400, 50, 50, 50)
                .fontSize(8)
                .font(HELVETICA)
                .borderColor(Color.WHITE);

        // Add the header row ...
        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().text("Product").horizontalAlignment(LEFT).borderWidth(1).build())
                .add(TextCell.builder().text("2018").borderWidth(1).build())
                .add(TextCell.builder().text("2019").borderWidth(1).build())
                .add(TextCell.builder().text("Total").borderWidth(1).build())
                .backgroundColor(BLUE_DARK)
                .textColor(Color.WHITE)
                .font(HELVETICA_BOLD)
                .fontSize(9)
                .horizontalAlignment(CENTER)
                .build());
        
        double grandTotal = 0;
        for (int i = 0; i < DATA.length; i++) {
            final Object[] dataRow = DATA[i];
            final double total = (double) dataRow[1] + (double) dataRow[2];
            grandTotal += total;

            tableBuilder.addRow(Row.builder()
                    .add(TextCell.builder().text(String.valueOf(dataRow[0])).horizontalAlignment(LEFT).borderWidth(1).build())
                    .add(TextCell.builder().text(dataRow[1] + " €").borderWidth(1).build())
                    .add(TextCell.builder().text(dataRow[2] + " €").borderWidth(1).build())
                    .add(TextCell.builder().text(total + " €").borderWidth(1).build())
                    .backgroundColor(i % 2 == 0 ? BLUE_LIGHT_1 : BLUE_LIGHT_2)
                    .horizontalAlignment(RIGHT)
                    .build());
        }

        // Add a final row
        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().text("This spans over 3 cells, is right aligned and its text is so long that it even breaks. " +
                        "Also it shows the grand total in the next cell and furthermore vertical alignment is shown:")
                        .colSpan(3)
                        .lineSpacing(1f)
                        .borderWidthTop(1)
                        .textColor(WHITE)
                        .backgroundColor(BLUE_DARK)
                        .fontSize(6)
                        .font(HELVETICA_OBLIQUE)
                        .borderWidth(1)
                        .build())
                .add(TextCell.builder().text(grandTotal + " €").backgroundColor(LIGHT_GRAY)
                        .font(HELVETICA_BOLD_OBLIQUE)
                        .verticalAlignment(TOP)
                        .borderWidth(1)
                        .build())
                .horizontalAlignment(RIGHT)
                .build());


                // Set up the drawer
                TableDrawer tableDrawer = TableDrawer.builder()
                        .contentStream(contentStream)
                        .startX(20f)
                        .startY(page.getMediaBox().getUpperRightY() - 20f)
                        .table(tableBuilder.build())
                        .build();

                // And go for it!
                tableDrawer.draw();
            }
    }
    
    
}
