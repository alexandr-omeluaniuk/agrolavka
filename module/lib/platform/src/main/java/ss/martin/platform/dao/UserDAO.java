/*
 * Copyright (C) 2018 Wisent Media
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
package ss.martin.platform.dao;

import java.util.List;
import ss.entity.martin.SystemUser;
import ss.entity.martin.UserAgent;

/**
 * SystemUser DAO.
 * @author Alexandr Omeluaniuk.
 */
public interface UserDAO {
    /**
     * Find user by username.
     * @param username username (we use email).
     * @return user.
     */
    SystemUser findByUsername(String username);
    /**
     * Get super user.
     * @return super user or null.
     */
    SystemUser getSuperUser();
    /**
     * Get user by validation string.
     * @param validationString validation string.
     * @return system user or null.
     */
    SystemUser getUserByValidationString(String validationString);
    /**
     * Get user user agents.
     * @param user user.
     * @return user agents.
     */
    List<UserAgent> getUserAgents(SystemUser user);
}
