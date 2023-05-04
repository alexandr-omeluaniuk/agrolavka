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
package ss.agrolavka.task.europost;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import ss.entity.agrolavka.EuropostLocation;

/**
 *
 * @author alex
 */
public class EuropostResponse {
    @JsonProperty("Table")
    private List<EuropostLocation> table;
    /**
     * @return the Table
     */
    public List<EuropostLocation> getTable() {
        return table;
    }
    /**
     * @param Table the Table to set
     */
    public void setTable(List<EuropostLocation> Table) {
        this.table = Table;
    }
}
