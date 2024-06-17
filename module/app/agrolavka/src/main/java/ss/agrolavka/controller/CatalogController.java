package ss.agrolavka.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ss.agrolavka.constants.JspPage;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.constants.SiteUrls;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.service.AllProductGroupsService;
import ss.agrolavka.service.ProductAttributesService;
import ss.agrolavka.service.ProductService;
import ss.agrolavka.util.CartUtils;
import ss.agrolavka.wrapper.ProductsSearchRequest;
import ss.entity.agrolavka.OrderPosition;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.Product_;
import ss.entity.agrolavka.ProductsGroup;
import ss.entity.martin.DataModel;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static ss.agrolavka.constants.JspValue.*;

/**
 * Catalog page controller.
 * @author alex
 */
@Controller
class CatalogController extends BaseJspController {
    
    private static final String VIEW_TILES = "TILES";
    
    private static final String SORT_ALPHABET = "alphabet";
    private static final String SORT_CHEAP = "cheap";
    private static final String SORT_EXPENSIVE = "expensive";
    
    private static final String REDIRECT_TO_404 = "redirect:/error/page-not-found";
    
    /** Product DAO. */
    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private ProductService productService;

    @Autowired
    private AllProductGroupsService allProductGroupsService;

    @Autowired
    private ProductAttributesService productAttributesService;
        
    @RequestMapping(SiteUrls.PAGE_CATALOG)
    public Object catalog(
        final Model model, 
        final HttpServletRequest request,
        @RequestParam(name = "page", required = false) final Integer page,
        @RequestParam(name = "view", required = false) final String view,
        @RequestParam(name = "sort", required = false) final String sort,
        @RequestParam(name = "available", required = false) final boolean available
    ) {
        setCommonAttributes(request, model);
        setFilterAttributes(model, page, view, sort, available);
        final var url = request.getRequestURI();
        model.addAttribute(CANONICAL, url  + (page != null ? "?page=" + page : ""));
        if (SiteUrls.PAGE_CATALOG_ROOT.equals(url)) {
            setCatalogRootAttributes(model);
            setPurchaseHistoryProducts(model, request, null);
            return JspPage.CATALOG;
        } else {
            final var entity = resolveUrlToProductGroup(url);
            if (entity == null) {
                return new ModelAndView(REDIRECT_TO_404);
            }
            if (entity instanceof ProductsGroup group) {
                setCatalogAttributes(model, group);
                setPurchaseHistoryProducts(model, request, group);
                setProducts(model, group.getId(), page, sort, available);
                return JspPage.CATALOG;
            } else if (entity instanceof Product product) {
                model.addAttribute(CANONICAL, url);
                setProductAttributes(model, product, request);
                return JspPage.PRODUCT;
            } else {
                return new ModelAndView(REDIRECT_TO_404);
            }
        }
    }
    
    private void setProductAttributes(final Model model, final Product product, final HttpServletRequest request) {
        final var metaDescription = getMetaDescription(product);
        final var fullDescription = Optional.ofNullable(product.getDescription())
            .map(desc -> desc.replace("\"", "&quot;")).orElse("");
        final var variants = productService.getVariants(product);
        final var basePrice = product.getPrice();
        final var discount = product.getDiscount() != null && product.getDiscount().getDiscount() != null
            ? product.getDiscount().getDiscount() : null;
        final var orderPositions = sessionService.getCurrentOrder(request).getPositions();
        model.addAttribute(TITLE, product.getSeoTitle() != null
                ? product.getSeoTitle() : "Купить " + product.getGroup().getName() + " " + product.getName()
                + ". Способ применения, инструкция, описание " + product.getName());
        final var productWithLinks = productAttributesService.setAttributeLinks(Collections.singletonList(product)).get(0);
        model.addAttribute(PRODUCT, productWithLinks);
        model.addAttribute(ATTRIBUTE_LINKS, productWithLinks.getAttributeLinks().toString().replace("\"", "'"));
        model.addAttribute(STRUCTURED_IMAGE, product.getImages().isEmpty() 
                ? domainConfiguration.host() + "/assets/img/no-image.png"
                : domainConfiguration.host() + "/media/" + product.getImages().get(0).getFileNameOnDisk());
        model.addAttribute(STRUCTURED_DATA_NAME, product.getName().replace("\\", "").replace("\"", "'"));
        model.addAttribute(STRUCTURED_DATA_DESCRIPTION, metaDescription.replace("\\", "").replace("\"", "'"));
        model.addAttribute(BREADCRUMB_LABEL, product.getName());
        model.addAttribute(BREADCRUMB_PATH, productsGroupService.getBreadcrumbPath(product.getGroup()));
        model.addAttribute(PRODUCT_PRICE, String.format("%.2f", basePrice));
        model.addAttribute(PRODUCT_DISCOUNT, discount != null ? discount : "");
        model.addAttribute(PRODUCT_URL, domainConfiguration.host() + request.getRequestURI());
        model.addAttribute(IN_CART, CartUtils.inCart(product, orderPositions));
        model.addAttribute(IN_CART_VARIANTS, CartUtils.inCartVariants(product, orderPositions));
        model.addAttribute(VOLUMES, product.getVolumes() != null ? product.getVolumes().replace("\"", "'") : "");
        model.addAttribute(VARIANTS, variants.toString().replace("\"", "'"));
        model.addAttribute(META_DESCRIPTION, metaDescription);
        model.addAttribute(FULL_PRODUCT_DESCRIPTION, fullDescription);
        final var calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 1);
        model.addAttribute(PRICE_VALID_UNTIL, new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
    }
    
    private void setCatalogRootAttributes(final Model model) {
        model.addAttribute(TITLE, "Широкий выбор товаров для сада и огорода");
        model.addAttribute(META_DESCRIPTION, "Каталог товаров для сада и огорода");
        model.addAttribute(CATEGORIES, productsGroupService.getRootProductGroups());
    }

    private void setPurchaseHistoryProducts(
        final Model model,
        final HttpServletRequest request,
        final ProductsGroup group
    ) {
        final var purchaseHistory = orderService.getPurchaseHistory(request);
        final var uniqueProducts = purchaseHistory.stream()
            .flatMap(order -> order.getPositions().stream().map(OrderPosition::getProduct))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        if (group == null) {
            model.addAttribute(PURCHASE_HISTORY_PRODUCTS, uniqueProducts);
        } else {
            final var nestedGroups = productsGroupService.getAllNestedGroups(group);
            nestedGroups.add(group.getId());
            final var groupProducts = uniqueProducts.stream()
                .filter(product -> nestedGroups.contains(product.getGroup().getId()))
                .collect(Collectors.toSet());
            model.addAttribute(PURCHASE_HISTORY_PRODUCTS, groupProducts);
        }
    }
    
    private void setCatalogAttributes(final Model model, final ProductsGroup group) {
        model.addAttribute(TITLE, group.getSeoTitle() != null ? group.getSeoTitle() : group.getName());
        model.addAttribute(PRODUCT_GROUP, group);
        model.addAttribute(BREADCRUMB_LABEL, group.getName());
        final var breadcrumb = productsGroupService.getBreadcrumbPath(group);
        breadcrumb.remove(group);
        model.addAttribute(BREADCRUMB_PATH, breadcrumb);
        model.addAttribute(META_DESCRIPTION, getMetaDescription(group));
        final var categories = productsGroupService.getCategoriesTree().get(group.getExternalId());
        if (categories != null) {
            Collections.sort(categories);
        }
        model.addAttribute(CATEGORIES, Optional.ofNullable(categories).orElse(Collections.emptyList()));
    }
    
    private String getMetaDescription(final ProductsGroup group) {
        String meta = "Купить " + group.getName();
        if (group.getSeoDescription() != null && !group.getSeoDescription().isBlank()) {
            meta = group.getSeoDescription();
        } else if (group.getDescription() != null && !group.getDescription().isBlank()) {
            meta = group.getDescription();
        }
        return meta.length() > 255 ? meta.substring(0, 255) : meta;
    }
    
    private String getMetaDescription(final Product product) {
        String meta = "Купить " + product.getName();
        if (product.getSeoDescription() != null && !product.getSeoDescription().isBlank()) {
            meta = product.getSeoDescription();
        } else if (product.getDescription() != null && !product.getDescription().isBlank()) {
            meta = product.getDescription();
        }
        return meta.length() > 255 ? meta.substring(0, 255) : meta;
    }
    
    private void setFilterAttributes(
        final Model model,
        final Integer page,
        final String view,
        final String sort,
        final boolean available
    ) {
        model.addAttribute(PRODUCT_ATTRIBUTES, productAttributesService.getAllProductAttributes());
        model.addAttribute(PAGE, Optional.ofNullable(page).orElse(1));
        model.addAttribute(VIEW, Optional.ofNullable(view).orElse(VIEW_TILES));
        model.addAttribute(SORT, Optional.ofNullable(sort).orElse(SORT_ALPHABET));
        model.addAttribute(AVAILABLE, available);
    }
    
    private DataModel resolveUrlToProductGroup(String url) {
        String last = url.substring(url.lastIndexOf("/") + 1);
        for (final var group : allProductGroupsService.getAllGroups()) {
            if (last.equals(group.getUrl())) {
                return group;
            }
        }
        return productDAO.getProductByUrl(last);
    }
    
    private void setProducts(Model model, Long groupId, Integer page, String sort, boolean available) {
        final var searchRequest = new ProductsSearchRequest();
        searchRequest.setGroupId(groupId);
        searchRequest.setPage(page == null ? 1 : page);
        searchRequest.setAvailable(available);
        int pageSize = SiteConstants.SEARCH_RESULT_TILES_COLUMNS * SiteConstants.SEARCH_RESULT_TILES_ROWS;
        searchRequest.setPageSize(pageSize);
        if (SORT_ALPHABET.equals(sort)) {
            searchRequest.setOrder("asc");
            searchRequest.setOrderBy(Product_.NAME);
        } else if (SORT_CHEAP.equals(sort)) {
            searchRequest.setOrder("asc");
            searchRequest.setOrderBy(Product_.PRICE);
        } else if (SORT_EXPENSIVE.equals(sort)) {
            searchRequest.setOrder("desc");
            searchRequest.setOrderBy(Product_.PRICE);
        } else {
            searchRequest.setOrder("asc");
            searchRequest.setOrderBy(Product_.NAME);
        }
        final var products = productService.search(searchRequest);
        products.forEach(p -> p.setVariants(productService.getVariants(p)));
        model.addAttribute(PRODUCTS_SEARCH_RESULT, productAttributesService.setAttributeLinks(products));
        Long count = productDAO.count(searchRequest);
        model.addAttribute(PRODUCTS_SEARCH_RESULT_PAGES, Double.valueOf(Math.ceil((double) count / pageSize)).intValue());
    }
}
