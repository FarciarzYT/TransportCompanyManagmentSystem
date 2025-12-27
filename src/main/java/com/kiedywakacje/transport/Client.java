package com.kiedywakacje.transport;

import com.kiedywakacje.transport.models.UserRole;
import java.util.ArrayList;
import java.util.List;

public class Client extends User {
    private String phoneNumber;
    private String address;
    private List<TransportOrder> orderHistory;

    public Client(String username, String password, String email, String fullName, 
                  String phoneNumber, String address) {
        super(username, password, email, fullName, UserRole.CLIENT);
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.orderHistory = new ArrayList<>();
    }

    public void addOrder(TransportOrder order) {
        if (order != null) {
            orderHistory.add(order);
        }
    }

    public List<TransportOrder> getOrderHistory() {
        return new ArrayList<>(orderHistory);
    }

    public int getOrderCount() {
        return orderHistory.size();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("Client{username='%s', fullName='%s', email='%s', phone='%s', orders=%d}",
                getUsername(), getFullName(), getEmail(), phoneNumber, orderHistory.size());
    }
}

