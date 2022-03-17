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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author alex
 */
public class EuropostPacket {
    private Map<String, String> Data = new HashMap<>();
    private String JWT = "";
    private String MethodName = "Postal.OfficesOut";
    private String ServiceNumber = "E811AE79-DFDE-4F85-8715-DD3A8308707E";
    /**
     * @return the Data
     */
    public Map<String, String> getData() {
        return Data;
    }
    /**
     * @param Data the Data to set
     */
    public void setData(Map<String, String> Data) {
        this.Data = Data;
    }
    /**
     * @return the JWT
     */
    public String getJWT() {
        return JWT;
    }
    /**
     * @param JWT the JWT to set
     */
    public void setJWT(String JWT) {
        this.JWT = JWT;
    }
    /**
     * @return the MethodName
     */
    public String getMethodName() {
        return MethodName;
    }
    /**
     * @param MethodName the MethodName to set
     */
    public void setMethodName(String MethodName) {
        this.MethodName = MethodName;
    }
    /**
     * @return the ServiceNumber
     */
    public String getServiceNumber() {
        return ServiceNumber;
    }
    /**
     * @param ServiceNumber the ServiceNumber to set
     */
    public void setServiceNumber(String ServiceNumber) {
        this.ServiceNumber = ServiceNumber;
    }
}
