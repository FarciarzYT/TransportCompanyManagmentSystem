package com.kiedywakacje.transport;

import com.kiedywakacje.transport.models.Status;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderRepository {
    private Map<Integer, TransportOrder> ordersById;
    private List<TransportOrder> allOrders;

    public OrderRepository() {
        this.ordersById = new HashMap<>();
        this.allOrders = new ArrayList<>();
    }

    public void addOrder(TransportOrder order) {
        if (order != null) {
            ordersById.put(order.getId(), order);
            allOrders.add(order);
        }
    }

    public TransportOrder getOrderById(int id) {
        return ordersById.get(id);
    }

    public List<TransportOrder> getAllOrders() {
        return new ArrayList<>(allOrders);
    }

    public List<TransportOrder> getOrdersByStatus(Status status) {
        return allOrders.stream()
                .filter(order -> order.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<TransportOrder> getOrdersByClient(String clientName) {
        return allOrders.stream()
                .filter(order -> order.getClientName().equals(clientName))
                .collect(Collectors.toList());
    }

    public List<TransportOrder> getPendingOrders() {
        return getOrdersByStatus(Status.PENDING);
    }

    public List<TransportOrder> getAssignedOrders() {
        return getOrdersByStatus(Status.ASSIGNED);
    }

    public List<TransportOrder> getInProgressOrders() {
        return getOrdersByStatus(Status.IN_PROGRESS);
    }

    public List<TransportOrder> getCompletedOrders() {
        return getOrdersByStatus(Status.COMPLETED);
    }

    public int getTotalOrdersCount() {
        return allOrders.size();
    }

    public int getOrdersCountByStatus(Status status) {
        return (int) allOrders.stream()
                .filter(order -> order.getStatus() == status)
                .count();
    }
}

