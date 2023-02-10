/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.service.OrderService;
import ss.agrolavka.util.AppCache;
import ss.agrolavka.util.UrlProducer;
import ss.agrolavka.wrapper.ProductsSearchRequest;
import ss.entity.agrolavka.EuropostLocation;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.OrderPosition;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductsGroup;
import ss.entity.agrolavka.Shop;
import ss.entity.agrolavka.Slide;
import ss.entity.martin.DataModel;
import ss.martin.platform.dao.CoreDAO;

/**
 * Site static pages controller.
 * @author alex
 */
@Controller
public class SiteController {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(SiteController.class);
    /** Product DAO. */
    @Autowired
    private ProductDAO productDAO;
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
    /** Order service. */
    @Autowired
    private OrderService orderService;
    /**
     * Home page.
     * @param model data model.
     * @param httpRequest HTTP request.
     * @return JSP page.
     * @throws Exception error.
     */
    @RequestMapping("/")
    public String home(Model model, HttpServletRequest httpRequest) throws Exception {
        insertCommonDataToModel(httpRequest, model);
        model.addAttribute("title", "Все для сада и огорода");
        List<Product> newProducts = AppCache.getNewProducts();
        if (newProducts == null) {
            ProductsSearchRequest searchRequest = new ProductsSearchRequest();
            searchRequest.setPage(1);
            searchRequest.setPageSize(12);
            searchRequest.setOrder("desc");
            searchRequest.setOrderBy("created_date");
            AppCache.setNewProducts(productDAO.search(searchRequest));
            newProducts = AppCache.getNewProducts();
        }
        model.addAttribute("newProducts", newProducts);
        List<Slide> slides = AppCache.getSlides();
        if (slides == null) {
            AppCache.flushSlidesCache(coreDAO.getAll(Slide.class));
            slides = AppCache.getSlides();
        }
        model.addAttribute("slides", slides);
        List<Product> withDiscount = getProductsWithDiscount();
        List<Product> withDiscountFirst12 = withDiscount.size() > 12 ? withDiscount.subList(0, 12) : withDiscount;
        model.addAttribute("productsWithDiscount", withDiscountFirst12);
        return "home";
    }
    /**
     * Cart page.
     * @param model page model.
     * @param httpRequest HTTP request.
     * @return page.
     * @throws Exception error.
     */
    @RequestMapping("/cart")
    public String cart(Model model, HttpServletRequest httpRequest) throws Exception {
        insertCommonDataToModel(httpRequest, model);
        return "cart";
    }
    /**
     * Order.
     * @param model page model.
     * @param httpRequest HTTP request.
     * @return page.
     * @throws Exception error.
     */
    @RequestMapping("/order")
    public String order(Model model, HttpServletRequest httpRequest) throws Exception {
        insertCommonDataToModel(httpRequest, model);
        final List<EuropostLocation> locations = coreDAO.getAll(EuropostLocation.class);
        Collections.sort(locations);
        model.addAttribute("europostLocations", locations);
        return "order";
    }
    /**
     * Delivery page.
     * @param model page model.
     * @param httpRequest HTTP request.
     * @return page.
     * @throws Exception error. 
     */
    @RequestMapping("/delivery")
    public String delivery(Model model, HttpServletRequest httpRequest) throws Exception {
        insertCommonDataToModel(httpRequest, model);
        return "delivery";
    }
    /**
     * Discount page.
     * @param model page model.
     * @param httpRequest HTTP request.
     * @return page.
     * @throws Exception error. 
     */
    @RequestMapping("/discount")
    public String discount(Model model, HttpServletRequest httpRequest) throws Exception {
        insertCommonDataToModel(httpRequest, model);
        return "discount";
    }
    /**
     * Promotions page.
     * @param model page model.
     * @param httpRequest HTTP request.
     * @return page.
     * @throws Exception error.
     */
    @RequestMapping("/promotions")
    public String promotions(Model model, HttpServletRequest httpRequest) throws Exception {
        insertCommonDataToModel(httpRequest, model);
        model.addAttribute("products", getProductsWithDiscount());
        return "promotions";
    }
    
    @RequestMapping("/shops")
    public String shops(Model model, HttpServletRequest httpRequest) throws Exception {
        insertCommonDataToModel(httpRequest, model);
        model.addAttribute("shops", coreDAO.getAll(Shop.class));
        return "shops";
    }
    /**
     * Feedback page.
     * @param model page model.
     * @param httpRequest HTTP request.
     * @return page.
     * @throws Exception error.
     */
    @RequestMapping("/feedback")
    public String feedback(Model model, HttpServletRequest httpRequest) throws Exception {
        insertCommonDataToModel(httpRequest, model);
        return "feedback";
    }
    /**
     * Page not found.
     * @param model page model.
     * @param httpRequest HTTP request.
     * @return page name.
     */
    @RequestMapping("/error/page-not-found")
    public String error404(Model model, HttpServletRequest httpRequest) throws Exception {
        insertCommonDataToModel(httpRequest, model);
        return "error/404";
    }
    /**
     * Product group page.
     * @param model data model.
     * @param request HTTP request.
     * @param page page number.
     * @param view catalog view type.
     * @param sort sort by.
     * @param available products os available.
     * @return JSP page.
     * @throws Exception error.
     */
    @RequestMapping("/catalog/**")
    public Object catalog(Model model, HttpServletRequest request,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "view", required = false) String view,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "available", required = false) boolean available) throws Exception {
        insertCommonDataToModel(request, model);
        String url = request.getRequestURI();
        model.addAttribute("page", page == null ? 1 : page);
        model.addAttribute("view", view == null ? "TILES" : view);
        model.addAttribute("sort", sort == null ? "alphabet" : sort);
        model.addAttribute("available", available);
        model.addAttribute("groups", AppCache.getProductsGroups());
        if ("/catalog".equals(url)) {
            model.addAttribute("canonical", url  + (page != null ? "?page=" + page : ""));
            model.addAttribute("title", "Широкий выбор товаров для сада и огорода");
            model.addAttribute("metaDescription", "Каталог товаров для сада и огорода");
            List<ProductsGroup> categories = AppCache.getRootProductGroups();
            if (categories != null) {
                Collections.sort(categories);
            }
            model.addAttribute("categories", categories);
            insertSearchResultToPage(model, null, page, sort == null ? "alphabet" : sort, available);
            return "catalog";
        }
        DataModel entity = resolveUrlToProductGroup(url);
        if (entity == null) {
            return new ModelAndView("redirect:/error/page-not-found");
        }
        if (entity instanceof ProductsGroup) {
            model.addAttribute("canonical", url + (page != null ? "?page=" + page : ""));
            ProductsGroup group = (ProductsGroup) entity;
            model.addAttribute("title", group.getSeoTitle() != null ? group.getSeoTitle() : group.getName());
            model.addAttribute("group", group);
            model.addAttribute("breadcrumbLabel", group.getName());
            List<ProductsGroup> path = UrlProducer.getBreadcrumbPath(group);
            path.remove(group);
            model.addAttribute("breadcrumbPath", path);
            insertSearchResultToPage(model, group.getId(), page, sort, available);
            String description = group.getDescription();
            String meta = "Купить " + group.getName();
            if (group.getSeoDescription() != null && !group.getSeoDescription().isBlank()) {
                meta = group.getSeoDescription();
            } else {
                if (description != null) {
                    meta = (description.length() > 255 ? description.substring(0, 255) : description);
                }
            }
            model.addAttribute("metaDescription", meta);
            List<ProductsGroup> categories = AppCache.getCategoriesTree().get(group.getExternalId());
            if (categories != null) {
                Collections.sort(categories);
            }
            model.addAttribute("categories", categories);
            return "catalog";
        } else if (entity instanceof Product) {
            model.addAttribute("canonical", url);
            Product product = (Product) entity;
            model.addAttribute("title", product.getSeoTitle() != null
                    ? product.getSeoTitle() : "Купить " + product.getGroup().getName() + " " + product.getName()
                    + ". Способ применения, инструкция, описание " + product.getName());
            model.addAttribute("id", product.getId());
            model.addAttribute("product", product);
            model.addAttribute("structuredImage", product.getImages().isEmpty() 
                    ? "https://agrolavka.by/assets/img/no-image.png"
                    : "https://agrolavka.by/media/" + product.getImages().get(0).getFileNameOnDisk());
            model.addAttribute("structuredDataName", product.getName().replace("\\", "").replace("\"", "'"));
            model.addAttribute("structuredDataDescription", product.getDescription() == null
                    ? "" : product.getDescription().replace("\\", "").replace("\"", "'"));
            model.addAttribute("groupId", product.getGroup() != null ? product.getGroup().getId() : null);
            model.addAttribute("breadcrumbLabel", product.getName());
            model.addAttribute("breadcrumbPath", UrlProducer.getBreadcrumbPath(product.getGroup()));
            model.addAttribute("productPrice", String.format("%.2f", product.getPrice()));
            model.addAttribute("productURL", "https://agrolavka.by" + request.getRequestURI());
            String description = product.getDescription();
            String meta = "Купить " + product.getName();
            if (product.getSeoDescription() != null && !product.getSeoDescription().isBlank()) {
                meta = product.getSeoDescription();
            } else {
                if (description != null) {
                    meta = (description.length() > 255 ? description.substring(0, 255) : description);
                }
            }
            model.addAttribute("metaDescription", meta);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(new Date());
            calendar.add(Calendar.MONTH, 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            model.addAttribute("priceValidUntil", sdf.format(calendar.getTime()));
            return "product";
        } else {
            return new ModelAndView("redirect:/error/page-not-found");
        }
    }
    // ================================================ PRIVATE =======================================================
    /**
     * Insert search result to page.
     * @param model page model.
     * @param groupId product group ID.
     * @param page page number.
     * @param sort sort by.
     */
    private void insertSearchResultToPage(Model model, Long groupId, Integer page, String sort, boolean available) {
        try {
            ProductsSearchRequest searchRequest = new ProductsSearchRequest();
            searchRequest.setGroupId(groupId);
            searchRequest.setPage(page == null ? 1 : page);
            searchRequest.setAvailable(available);
            int pageSize = SiteConstants.SEARCH_RESULT_TILES_COLUMNS * SiteConstants.SEARCH_RESULT_TILES_ROWS;
            searchRequest.setPageSize(pageSize);
            if ("alphabet".equals(sort)) {
                searchRequest.setOrder("asc");
                searchRequest.setOrderBy("name");
            } else if ("cheap".equals(sort)) {
                searchRequest.setOrder("asc");
                searchRequest.setOrderBy("price");
            } else if ("expensive".equals(sort)) {
                searchRequest.setOrder("desc");
                searchRequest.setOrderBy("price");
            } else {
                searchRequest.setOrder("asc");
                searchRequest.setOrderBy("name");
            }
            model.addAttribute("searchResult", productDAO.search(searchRequest));
            Long count = productDAO.count(searchRequest);
            model.addAttribute(
                    "searchResultPages", Double.valueOf(Math.ceil((double) count / pageSize)).intValue());
        } catch (Exception e) {
            LOG.error("Search products fail!", e);
            model.addAttribute("searchResult", new ArrayList<Product>());
            model.addAttribute("searchResultPages", 0);
        }
    }
    /**
     * Resolve URL to product group or product.
     * @param url url.
     * @return product group or product.
     */
    private DataModel resolveUrlToProductGroup(String url) {
        String last = url.substring(url.lastIndexOf("/") + 1);
        for (ProductsGroup group : AppCache.getProductsGroups()) {
            if (last.equals(group.getUrl())) {
                return group;
            }
        }
        return productDAO.getProductByUrl(last);
    }
    /**
     * Insert common to data model.
     * @param request HTTP request.
     * @param model page model.
     */
    private void insertCommonDataToModel(HttpServletRequest request, Model model) throws Exception {
        // cart
        final Order order = orderService.getCurrentOrder(request);
        Double total = 0d;
        for (OrderPosition pos : order.getPositions()) {
            total += pos.getPrice() * pos.getQuantity();
        }
        String totalStr = String.format("%.2f", total);
        String[] parts = totalStr.split("\\.");
        model.addAttribute("cart", order);
        model.addAttribute("totalInteger", parts[0]);
        model.addAttribute("totalDecimal", parts[1]);
        Long productsCount = AppCache.getProductsCount();
        if (productsCount == null) {
            ProductsSearchRequest requestX = new ProductsSearchRequest();
            requestX.setPage(1);
            requestX.setPageSize(Integer.MAX_VALUE);
            productsCount = productDAO.count(requestX);
            AppCache.setProductsCount(productsCount);
        }
        model.addAttribute("productsCount", productsCount);
        // shop
        List<Shop> shops = AppCache.getShops();
        if (shops == null) {
            AppCache.flushShopsCache(coreDAO.getAll(Shop.class));
            shops = AppCache.getShops();
        }
        model.addAttribute("shops", shops);
    }
    /**
     * Get products with discount.
     * @return products with discount.
     * @throws Exception error.
     */
    private List<Product> getProductsWithDiscount() throws Exception {
        List<Product> productsWithDiscounts = AppCache.getProductsWithDiscounts();
        if (productsWithDiscounts == null) {
            ProductsSearchRequest searchRequest = new ProductsSearchRequest();
            searchRequest.setPage(1);
            searchRequest.setPageSize(Integer.MAX_VALUE);
            searchRequest.setOrder("asc");
            searchRequest.setOrderBy("name");
            searchRequest.setWithDiscounts(true);
            final List<Product> products = productDAO.search(searchRequest).stream()
                    .filter(product -> product.getQuantity() != null && product.getQuantity() > 0)
                    .collect(Collectors.toList());
            AppCache.setProductsWithDiscounts(products);
            productsWithDiscounts = AppCache.getProductsWithDiscounts();
        }
        return productsWithDiscounts;
    }
}
