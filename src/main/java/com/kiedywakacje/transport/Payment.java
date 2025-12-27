package com.kiedywakacje.transport;

import com.kiedywakacje.transport.models.PaymentType;

public class Payment {
    private double price;
    private PaymentType paymentType;

    public Payment() {
    }

    public Payment(double price, PaymentType paymentType) {
        setPrice(price);
        this.paymentType = paymentType;
    }

    public void setPrice(double price) {
        if (price > 0) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Cena musi być większa od zera.");
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    @Override
    public String toString() {
        return String.format("Payment{price=%.2f PLN, type=%s}",
                price, paymentType != null ? paymentType.getDisplayName() : "NIEUSTAWIONY");
    }
}

