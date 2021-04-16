/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.constants;

/**
 * Order status.
 * @author alex
 */
public enum OrderStatus {
    /** Waiting for approval. */
    WAITING_FOR_APPROVAL,
    /** Approved. */
    APPROVED,
    /** Delivery. */
    DELIVERY,
    /** Closed. */
    CLOSED;
}
