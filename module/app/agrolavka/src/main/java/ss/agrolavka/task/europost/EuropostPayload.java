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

/**
 *
 * @author alex
 */
public class EuropostPayload {
    private String CRC = "";
    private EuropostPacket Packet = new EuropostPacket();
    /**
     * @return the CRC
     */
    public String getCRC() {
        return CRC;
    }
    /**
     * @param CRC the CRC to set
     */
    public void setCRC(String CRC) {
        this.CRC = CRC;
    }
    /**
     * @return the Packet
     */
    public EuropostPacket getPacket() {
        return Packet;
    }
    /**
     * @param Packet the Packet to set
     */
    public void setPacket(EuropostPacket Packet) {
        this.Packet = Packet;
    }
}
