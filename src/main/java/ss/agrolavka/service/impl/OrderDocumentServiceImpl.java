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
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import static org.vandeseer.easytable.settings.HorizontalAlignment.*;
import static org.vandeseer.easytable.settings.VerticalAlignment.TOP;
import ss.entity.agrolavka.OrderPosition;

/**
 *
 * @author alex
 */
@Service
class OrderDocumentServiceImpl implements OrderDocumentService {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(OrderDocumentServiceImpl.class);
    
    private final static String PRODUCT_NAME = "Наименование";
    private final static String QUANTITY = "Кол-во";
    private final static String PRICE = "Цена";
    private final static String DISCOUNT = "Скидка";
    private final static String TOTAL = "Сумма";
        
    private final static Color BLUE_DARK = new Color(76, 129, 190);
    private final static Color BLUE_LIGHT_1 = new Color(186, 206, 230);
    private final static Color BLUE_LIGHT_2 = new Color(218, 230, 242);
    
    @Value("classpath:Roboto-Bold.ttf")
    private Resource robotoBold;
    
    @Value("classpath:Roboto-Regular.ttf")
    private Resource robotoRegular;

    @Override
    public byte[] generateOrderPdf(final Order order) throws Exception {
        try (PDDocument document = new PDDocument()) {
            document.getDocumentInformation().setAuthor("agrolavka.by");
            document.getDocumentInformation().setTitle("Заказ №" + order.getId());
            final PDPage page = new PDPage();
            addTable(document, page, order);
            document.addPage(page);
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            LOG.info("Order PDF " + order + " has been generated");
            return baos.toByteArray();
        }
    }
    
    private void addTable(final PDDocument document, final PDPage page, final Order order) throws IOException {
        // fonts
        final PDFont robotoBoldFont = PDType0Font.load(document, robotoBold.getInputStream());
        final PDFont robotoRegularFont = PDType0Font.load(document, robotoRegular.getInputStream());
        
        final TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(360, 50, 50, 50, 50)
                .fontSize(10).font(robotoBoldFont).borderColor(Color.WHITE).padding(6f);
        // header
        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().text(PRODUCT_NAME).horizontalAlignment(LEFT).borderWidth(1).build())
                .add(TextCell.builder().text(QUANTITY).borderWidth(1).build())
                .add(TextCell.builder().text(DISCOUNT).borderWidth(1).build())
                .add(TextCell.builder().text(PRICE).borderWidth(1).build())
                .add(TextCell.builder().text(TOTAL).borderWidth(1).build())
                .backgroundColor(BLUE_DARK).textColor(Color.WHITE).font(robotoBoldFont).fontSize(10)
                .horizontalAlignment(CENTER).build()
        );
        double grandTotal = 0;
        final List<OrderPosition> positions = new ArrayList(order.getPositions());
        for (int i = 0; i < positions.size(); i++) {
            final OrderPosition position = positions.get(i);
            final String productName = position.getProduct().getName();
            final Integer quantity = position.getQuantity();
            final Double price = position.getPrice();
            final String discount = "";
            final double subtotal = price * quantity;
            grandTotal += subtotal;
            
            tableBuilder.addRow(Row.builder()
                    .add(TextCell.builder().text(String.valueOf(productName)).horizontalAlignment(LEFT).borderWidth(1).build())
                    .add(TextCell.builder().text(String.valueOf(quantity)).borderWidth(1).build())
                    .add(TextCell.builder().text(discount).borderWidth(1).build())
                    .add(TextCell.builder().text(String.format("%.2f", price)).borderWidth(1).build())
                    .add(TextCell.builder().text(String.format("%.2f", subtotal)).borderWidth(1).build())
                    .backgroundColor(i % 2 == 0 ? BLUE_LIGHT_1 : BLUE_LIGHT_2)
                    .horizontalAlignment(RIGHT)
                    .font(robotoRegularFont).fontSize(10)
                    .build());
        }
        
        // Add a final row
        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder()
                        .text(order.getComment() == null ? "" : "Комментарий от клиента:\n" + order.getComment())
                        .colSpan(4)
                        .lineSpacing(1f)
                        .borderWidthTop(1)
                        .textColor(WHITE)
                        .backgroundColor(BLUE_DARK)
                        .fontSize(6)
                        .font(robotoBoldFont)
                        .borderWidth(1)
                        .build())
                .add(TextCell.builder().text(String.format("%.2f", grandTotal)).backgroundColor(LIGHT_GRAY)
                        .font(robotoRegularFont).fontSize(10)
                        .verticalAlignment(TOP)
                        .horizontalAlignment(RIGHT)
                        .borderWidth(1)
                        .build())
                .horizontalAlignment(LEFT)
                .build());
        try ( PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            // Set up the drawer
            TableDrawer tableDrawer = TableDrawer.builder()
                    .contentStream(contentStream)
                    .startX(20f)
                    .startY(page.getMediaBox().getUpperRightY() - 20f)
                    .table(tableBuilder.build())
                    .build();
            tableDrawer.draw();
        }
    }
    
}
