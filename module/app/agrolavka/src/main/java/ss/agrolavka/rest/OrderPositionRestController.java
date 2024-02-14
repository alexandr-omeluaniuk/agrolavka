package ss.agrolavka.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ss.agrolavka.service.OrderPositionService;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.OrderPosition;
import ss.martin.core.dao.CoreDao;

import java.util.List;

@RestController
@RequestMapping("/api/agrolavka/protected/order-position")
public class OrderPositionRestController {

    @Autowired
    private CoreDao coreDAO;

    @Autowired
    private OrderPositionService orderPositionService;

    @GetMapping("/{id}")
    public List<OrderPosition> getPositions(@PathVariable("id") Long id) throws Exception {
        return orderPositionService.getPositions(coreDAO.findById(id, Order.class));
    }

    @PutMapping
    public void updatePosition(@RequestBody OrderPosition position) {
        final var positionDb = coreDAO.findById(position.getId(), OrderPosition.class);
        positionDb.setQuantity(position.getQuantity());
        positionDb.setPrice(position.getPrice());
        coreDAO.update(positionDb);
    }

    @DeleteMapping("/{id}")
    public void deletePosition(@PathVariable("id") Long id) {
        coreDAO.delete(id, OrderPosition.class);
    }
}
