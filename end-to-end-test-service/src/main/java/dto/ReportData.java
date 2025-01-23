package dto;

public record ReportData (String merchantId, String token, double amount, String paymentId, String customerId) {
}
