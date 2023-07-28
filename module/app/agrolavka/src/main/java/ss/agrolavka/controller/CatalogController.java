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
import ss.entity.agrolavka.ProductsGroup;
import ss.entity.martin.DataModel;

/**
 * Catalog page controller.
 * @author alex
 */
@Controller
class CatalogController extends BaseJspController {
    
    private static final String VIEW_TILES = "TILES";
    
    private static final String SORT_ALPHABET = "alphabet";
    
    private static final String REDIRECT_TO_404 = "redirect:/error/page-not-found";
    
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
                setProducts(model, group.getId(), page, sort, available);
                setCatalogAttributes(model, group);
                return JspPage.CATALOG;
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
                model.addAttribute("breadcrumbPath", productsGroupService.getBreadcrumbPath(product.getGroup()));
                model.addAttribute("productPrice", String.format("%.2f", product.getPrice()));
                model.addAttribute("productURL", "https://agrolavka.by" + request.getRequestURI());
                final var inCart = orderService.getCurrentOrder(request).getPositions().stream()
                    .filter(pos -> Objects.equals(product.getId(), pos.getProductId())).findFirst().isPresent();
                model.addAttribute("inCart", inCart);
                model.addAttribute("volumes", product.getVolumes() != null ? product.getVolumes().replace("\"", "'") : "");
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
        model.addAttribute(PRODUCTS_SEARCH_RESULT, productDAO.search(searchRequest));
        Long count = productDAO.count(searchRequest);
        model.addAttribute(PRODUCTS_SEARCH_RESULT_PAGES, Double.valueOf(Math.ceil((double) count / pageSize)).intValue());
    }
}
