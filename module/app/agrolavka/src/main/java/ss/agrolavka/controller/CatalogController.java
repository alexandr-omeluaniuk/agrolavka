package ss.agrolavka.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ss.agrolavka.constants.JspPage;
import static ss.agrolavka.constants.JspValue.*;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.constants.SiteUrls;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.util.AppCache;
import ss.agrolavka.wrapper.ProductsSearchRequest;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.Product_;
import ss.entity.agrolavka.ProductsGroup;
import ss.entity.martin.DataModel;
import ss.martin.security.configuration.external.DomainConfiguration;

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
    
    @Autowired
    private DomainConfiguration domainConfiguration;
    
    /** Product DAO. */
    @Autowired
    private ProductDAO productDAO;
    
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
            return JspPage.CATALOG;
        } else {
            final var entity = resolveUrlToProductGroup(url);
            if (entity == null) {
                return new ModelAndView(REDIRECT_TO_404);
            }
            if (entity instanceof ProductsGroup group) {
                setCatalogAttributes(model, group);
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
        model.addAttribute(TITLE, product.getSeoTitle() != null
                ? product.getSeoTitle() : "Купить " + product.getGroup().getName() + " " + product.getName()
                + ". Способ применения, инструкция, описание " + product.getName());
        model.addAttribute(PRODUCT, product);
        model.addAttribute(STRUCTURED_IMAGE, product.getImages().isEmpty() 
                ? domainConfiguration.host() + "/assets/img/no-image.png"
                : domainConfiguration.host() + "/media/" + product.getImages().get(0).getFileNameOnDisk());
        model.addAttribute(STRUCTURED_DATA_NAME, product.getName().replace("\\", "").replace("\"", "'"));
        model.addAttribute(STRUCTURED_DATA_DESCRIPTION, product.getDescription() == null
                ? "" : product.getDescription().replace("\\", "").replace("\"", "'"));
        model.addAttribute(BREADCRUMB_LABEL, product.getName());
        model.addAttribute(BREADCRUMB_PATH, productsGroupService.getBreadcrumbPath(product.getGroup()));
        model.addAttribute(PRODUCT_PRICE, String.format("%.2f", product.getDiscountPrice()));
        model.addAttribute(PRODUCT_URL, domainConfiguration.host() + request.getRequestURI());
        final var inCart = orderService.getCurrentOrder(request).getPositions().stream()
            .filter(pos -> Objects.equals(product.getId(), pos.getProductId())).findFirst().isPresent();
        model.addAttribute(IN_CART, inCart);
        model.addAttribute(VOLUMES, product.getVolumes() != null ? product.getVolumes().replace("\"", "'") : "");
        model.addAttribute(META_DESCRIPTION, getMetaDescription(product));
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
        model.addAttribute(CATEGORIES, categories);
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
        model.addAttribute(PAGE, Optional.ofNullable(page).orElse(1));
        model.addAttribute(VIEW, Optional.ofNullable(view).orElse(VIEW_TILES));
        model.addAttribute(SORT, Optional.ofNullable(sort).orElse(SORT_ALPHABET));
        model.addAttribute(AVAILABLE, available);
    }
    
    private DataModel resolveUrlToProductGroup(String url) {
        String last = url.substring(url.lastIndexOf("/") + 1);
        for (ProductsGroup group : AppCache.getProductsGroups()) {
            if (last.equals(group.getUrl())) {
                return group;
            }
        }
        return productDAO.getProductByUrl(last);
    }
    
    private void setProducts(Model model, Long groupId, Integer page, String sort, boolean available) {
        ProductsSearchRequest searchRequest = new ProductsSearchRequest();
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
        model.addAttribute(PRODUCTS_SEARCH_RESULT, productDAO.search(searchRequest));
        Long count = productDAO.count(searchRequest);
        model.addAttribute(PRODUCTS_SEARCH_RESULT_PAGES, Double.valueOf(Math.ceil((double) count / pageSize)).intValue());
    }
}
