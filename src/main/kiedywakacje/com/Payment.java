package src.main.kiedywakacje.com;

import src.main.kiedywakacje.com.models.PaymentType;

public class Payment {
    double price;
    private PaymentType paymentType;


    public void setPrice(double price) {
        if(price > 0) {
            this.price = price;
        }
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
}


