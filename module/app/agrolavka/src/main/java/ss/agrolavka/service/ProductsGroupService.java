package ss.agrolavka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import ss.agrolavka.util.AppCache;
import ss.agrolavka.util.UrlProducer;
import ss.entity.agrolavka.ProductsGroup;
import ss.martin.core.dao.CoreDao;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Products group service implementation.
 * @author alex
 */
@Service
public class ProductsGroupService {
    
    @Autowired
    private MySkladIntegrationService mySklad;

    @Autowired
    private AllProductGroupsService allProductGroupsService;
    
    @Autowired
    private CacheManager cacheManager;
    
    @Autowired
    private CoreDao coreDao;

    public ProductsGroup create(final ProductsGroup group) {
        group.setUrl(UrlProducer.transliterate(group.getName()));
        group.setExternalId(mySklad.createProductsGroup(group));
        final var newEntity = coreDao.create(group);
        resetCache();
        return newEntity;
    }

    public ProductsGroup update(final ProductsGroup group) {
        group.setUrl(UrlProducer.transliterate(group.getName()));
        mySklad.updateProductsGroup(group);
        final var updatedEntity = coreDao.update(group);
        resetCache();
        return updatedEntity;
    }

    public void delete(final Long id) {
        final var group = coreDao.findById(id, ProductsGroup.class);
        Optional.ofNullable(group).ifPresent(entity -> {
            mySklad.deleteProductsGroup(group);
            coreDao.delete(id, ProductsGroup.class);
            resetCache();
        });
    }

    public List<ProductsGroup> getTopCategories() {
        return getActiveProductGroups().stream()
            .filter(group -> group.isTopCategory() != null && group.isTopCategory())
            .sorted()
            .collect(
                Collectors.toList()
            );
    }
    
    public List<ProductsGroup> getRootProductGroups() {
        final var rootGroups = getActiveProductGroups().stream().filter(group -> {
            return group.getParentId() == null;
        }).collect(Collectors.toList());
        Collections.sort(rootGroups);
        return rootGroups;
    }

    public List<ProductsGroup> getActiveProductGroups() {
        final var hiddenGroups = getHiddenGroupIds();
        return allProductGroupsService.getAllGroups().stream().filter(group -> !hiddenGroups.contains(group.getId()))
            .collect(Collectors.toList());
    }
    
    public List<ProductsGroup> getBreadcrumbPath(final ProductsGroup group) {
        final var path = new ArrayList<ProductsGroup>();
        var current = group;
        final var groups = getActiveProductGroups();
        while (current != null) {
            path.add(current);
            final var parentId = current.getParentId();
            if (parentId != null) {
                current = groups.stream().filter(g -> {
                    return parentId.equals(g.getExternalId());
                }).findFirst().get();
            } else {
                current = null;
            }
        }
        Collections.reverse(path);
        return path;
    }
    
    public Map<String, List<ProductsGroup>> getCategoriesTree() {
        final var tree = new HashMap<String, List<ProductsGroup>>();
        for (final var group : allProductGroupsService.getAllGroups()) {
            final var parentId = Optional.ofNullable(group.getParentId()).orElse("-1");
            if (!tree.containsKey(parentId)) {
                tree.put(parentId, new ArrayList<>());
            }
            tree.get(parentId).add(group);
        }
        return tree;
    }

    public Set<Long> getHiddenGroupIds() {
        final var allGroupsList = allProductGroupsService.getAllGroups();
        return allGroupsList.stream().filter(
            gr -> gr.getHidden() != null && gr.getHidden()
        ).map(targetGroup -> {
            final var groupIds = getAllNestedGroups(targetGroup);
            groupIds.add(targetGroup.getId());
            return groupIds;
        }).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    public Set<Long> getAllNestedGroups(final ProductsGroup target) {
        final var treeMap = getCategoriesTree();
        Set<Long> groupIds = new HashSet<>();
        walkProductGroup(target, groupIds, treeMap);
        return groupIds;
    }

    private void walkProductGroup(ProductsGroup group, Set<Long> groupIds, Map<String, List<ProductsGroup>> groupsMap) {
        groupIds.add(group.getId());
        if (groupsMap.containsKey(group.getExternalId())) {
            groupsMap.get(group.getExternalId()).forEach(g -> {
                walkProductGroup(g, groupIds, groupsMap);
            });
        }
    }
    
    private void resetCache() {
        cacheManager.getCacheNames().forEach(name ->
            Optional.ofNullable(cacheManager.getCache(name)).ifPresent(Cache::clear)
        );
        AppCache.flushCache(getActiveProductGroups());
    }
}
