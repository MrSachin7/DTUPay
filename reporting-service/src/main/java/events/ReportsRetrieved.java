package events;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDateTime;
import java.util.List;

@RegisterForReflection
public class ReportsRetrieved {
    private String correlationId;
    private List<PaymentData> paymentData;

    public ReportsRetrieved() {
    }

    public ReportsRetrieved(String correlationId, List<PaymentData> paymentData) {
        this.correlationId = correlationId;
        this.paymentData = paymentData;
    }

    public ReportsRetrieved(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public List<PaymentData> getPaymentData() {
        return paymentData;
    }

    public static class PaymentData {

        private String paymentId;
        private String customerId;
        private String merchantId;
        private String token;
        private double amount;
        private LocalDateTime timeStamp;

        public String getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(String paymentId) {
            this.paymentId = paymentId;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public LocalDateTime getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(LocalDateTime timeStamp) {
            this.timeStamp = timeStamp;
        }
    }
}