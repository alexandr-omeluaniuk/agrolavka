/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.dao;

import java.util.List;
import java.util.Set;
import ss.entity.agrolavka.ExternalEntity;

/**
 * External entity DAO.
 * @author alex
 */
public interface ExternalEntityDAO {
    
    <T extends ExternalEntity> List<T> getExternalEntitiesByIds(Set<String> ids, Class<T> cl);
    
    <T extends ExternalEntity> void removeExternalEntitiesNotInIDs(Set<String> ids, Class<T> cl);
}
