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
import java.util.stream.Collectors;
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
import org.vandeseer.easytable.settings.VerticalAlignment;
import static org.vandeseer.easytable.settings.VerticalAlignment.TOP;
import ss.entity.agrolavka.Address;
import ss.entity.agrolavka.EuropostLocationSnapshot;
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
            final TableBuilder tableBuilder = createTableBuilder();
            addTable(document, order, tableBuilder);
            draw(document, page, tableBuilder);
            document.addPage(page);
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            LOG.info("Order PDF " + order + " has been generated");
            return baos.toByteArray();
        }
    }
    
    @Override
    public byte[] generateOrdersPdf(final List<Order> orders) throws Exception {
        try (PDDocument document = new PDDocument()) {
            document.getDocumentInformation().setAuthor("agrolavka.by");
            document.getDocumentInformation().setTitle("Заказы №№" + orders.stream().map(Order::getId).collect(Collectors.toList()).toString());
            final PDPage page = new PDPage();
            final TableBuilder tableBuilder = createTableBuilder();
            for (final Order order : orders) {
                addTable(document, order, tableBuilder);
            }
            draw(document, page, tableBuilder);
            document.addPage(page);
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }
    
    private TableBuilder createTableBuilder() {
        return  Table.builder()
                .addColumnsOfWidth(380, 60, 60, 60)
                .fontSize(10).borderColor(Color.WHITE).padding(6f);
    }
    
    private TableBuilder addTable(final PDDocument document, final Order order, final TableBuilder tableBuilder) throws IOException {
        // fonts
        final PDFont robotoBoldFont = PDType0Font.load(document, robotoBold.getInputStream());
        final PDFont robotoRegularFont = PDType0Font.load(document, robotoRegular.getInputStream());
        
        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder()
                        .text("Агролавка")
                        .colSpan(1)
                        .lineSpacing(1f)
                        .borderWidthTop(1)
                        .textColor(Color.ORANGE)
                        .backgroundColor(WHITE)
                        .fontSize(22)
                        .verticalAlignment(VerticalAlignment.BOTTOM)
                        .font(robotoBoldFont)
                        .borderWidth(1)
                        .build())
                .add(TextCell.builder().text("Информация о заказе:")
                        .backgroundColor(WHITE)
                        .textColor(BLUE_DARK)
                        .colSpan(3)
                        .font(robotoBoldFont).fontSize(10)
                        .horizontalAlignment(RIGHT)
                        .verticalAlignment(VerticalAlignment.BOTTOM)
                        .borderWidth(1)
                        .build())
                .horizontalAlignment(LEFT)
                .build());
        
        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder()
                        .text("agrolavka.by")
                        .colSpan(1)
                        .lineSpacing(1f)
                        .borderWidthTop(1)
                        .textColor(Color.ORANGE)
                        .backgroundColor(WHITE)
                        .fontSize(8)
                        .verticalAlignment(TOP)
                        .font(robotoBoldFont)
                        .borderWidth(1).paddingBottom(20f)
                        .build())
                .add(TextCell.builder().text(clientInfo(order))
                        .backgroundColor(WHITE)
                        .textColor(BLUE_DARK)
                        .colSpan(3)
                        .paddingBottom(20f)
                        .font(robotoRegularFont).fontSize(10)
                        .horizontalAlignment(RIGHT)
                        .verticalAlignment(TOP)
                        .borderWidth(1)
                        .build())
                .horizontalAlignment(LEFT)
                .build());
        
        // header
        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().text(PRODUCT_NAME).horizontalAlignment(LEFT).borderWidth(1).build())
                .add(TextCell.builder().text(QUANTITY).borderWidth(1).build())
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
            final double subtotal = price * quantity;
            grandTotal += subtotal;
            
            tableBuilder.addRow(Row.builder()
                    .add(TextCell.builder().text(String.valueOf(productName)).horizontalAlignment(LEFT).borderWidth(1).build())
                    .add(TextCell.builder().text(String.valueOf(quantity)).borderWidth(1).build())
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
                        .text(order.getComment() == null || order.getComment().isBlank() ? "" : "Комментарий от клиента:\n" + order.getComment())
                        .colSpan(3)
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
        return tableBuilder;
    }
    
    private void draw(final PDDocument document, final PDPage page, final TableBuilder tableBuilder) throws Exception {
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
    
    private String clientInfo(final Order order) {
                final StringBuilder sb = new StringBuilder();
        if (order.getAddress() != null) {
            final Address address = order.getAddress();
            sb.append(address.getRegion() != null ? address.getRegion() + ", " : "");
            sb.append(address.getDistrict() != null ? address.getDistrict() : "");
            sb.append("\n");
            sb.append(address.getPostcode() != null ? address.getPostcode() + ", " : "");
            sb.append(address.getCity() != null ? address.getCity() : "");
            sb.append("\n");
            sb.append(address.getStreet() != null && !address.getStreet().isBlank() ? address.getStreet() + " " : "");
            sb.append(address.getHouse() != null && !address.getHouse().isBlank() ? ", д. " + address.getHouse() : "");
            sb.append(address.getFlat() != null && !address.getFlat().isBlank() ? ", кв. " + address.getFlat() : "");
            sb.append("\n\n");
            sb.append(address.getLastname() != null ? address.getLastname() + " " : "");
            sb.append(address.getFirstname() != null ? address.getFirstname() + " " : "");
            sb.append(address.getMiddlename() != null ? address.getMiddlename() : "");
        } else if (order.getEuropostLocationSnapshot() != null) {
            final EuropostLocationSnapshot europost = order.getEuropostLocationSnapshot();
            sb.append("доставка Европочтой\n");
            sb.append(europost.getAddress() != null ? europost.getAddress() : "");
            sb.append("\n");
            sb.append(europost.getLastname() != null ? europost.getLastname() + " " : "");
            sb.append(europost.getFirstname() != null ? europost.getFirstname() + " " : "");
            sb.append(europost.getMiddlename() != null ? europost.getMiddlename() : "");
        } else {
            sb.append("самовывоз из магазина\n");
        }
        sb.append("\n");
        sb.append(order.getPhone());
        return sb.toString();
    }
    
}
