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
package ss.agrolavka.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ss.agrolavka.task.europost.EuropostPayload;
import ss.agrolavka.task.europost.EuropostResponse;
import ss.entity.agrolavka.EuropostLocation;
import ss.martin.platform.dao.CoreDAO;

/**
 * Europost locations scanner
 * @author alex
 */
@Component
class EuropostLocationScanner {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(EuropostLocationScanner.class);
    /** Europost URL. */
    private static final String EUROPOST_URL = "https://evropochta.by/api/directory.Json/?What=Postal.OfficesOut";
    /** REST template. */
    @Autowired
    private RestTemplate restTemplate;
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
    /**
     * Update Europost locations.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void updateLocations() throws Exception {
        LOG.info("Update Europost locations");
        final EuropostResponse response = restTemplate.exchange(
                EUROPOST_URL,
                HttpMethod.POST,
                new HttpEntity<>(new EuropostPayload()),
                EuropostResponse.class
        ).getBody();
        final List<EuropostLocation> locations = response.getTable();
        LOG.info(String.format("Found [%d] locations", locations.size()));
        final List<EuropostLocation> existingLocations = coreDAO.getAll(EuropostLocation.class);
        LOG.info(String.format("Existing [%d] locations", existingLocations.size()));
        final Map<String, EuropostLocation> locationsMap = existingLocations.stream()
                .collect(Collectors.toMap(EuropostLocation::getExternalId, Function.identity()));
        // create
        final Set<String> existingIds = locationsMap.keySet();
        final List<EuropostLocation> forCreate = locations.stream()
                .filter(location -> !existingIds.contains(location.getExternalId())).collect(Collectors.toList());
        coreDAO.massCreate(forCreate);
        LOG.info(String.format("Created [%d] locations", forCreate.size()));
        // update
        final List<EuropostLocation> forUpdate = new ArrayList<>();
        final Set<String> locationExternalIds = new HashSet<>();
        for (final EuropostLocation location : locations) {
            if (locationsMap.containsKey(location.getExternalId())) {
                final EuropostLocation existingLocation = locationsMap.get(location.getExternalId());
                existingLocation.setAddress(location.getAddress());
                existingLocation.setAltId(location.getAltId());
                existingLocation.setCity(location.getCity());
                existingLocation.setExternalId(location.getExternalId());
                existingLocation.setIsNew(location.getIsNew());
                existingLocation.setLatitude(location.getLatitude());
                existingLocation.setLongitude(location.getLongitude());
                existingLocation.setNote(location.getNote());
                existingLocation.setWarehouseId(location.getWarehouseId());
                existingLocation.setWorkingHours(location.getWorkingHours());
                forUpdate.add(existingLocation);
            }
            locationExternalIds.add(location.getExternalId());
        }
        coreDAO.massUpdate(forUpdate);
        LOG.info(String.format("Updated [%d] locations", forUpdate.size()));
        // delete
        final List<EuropostLocation> forDelete = existingLocations.stream()
                .filter(location -> !locationExternalIds.contains(location.getExternalId()))
                .collect(Collectors.toList());
        coreDAO.massDelete(forDelete);
        LOG.info(String.format("Deleted [%d] locations", forDelete.size()));
    }
    
}
