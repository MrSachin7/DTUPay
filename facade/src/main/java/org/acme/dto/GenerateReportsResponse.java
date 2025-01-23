package org.acme.dto;

import org.acme.events.ReportsRetrieved;

import java.util.List;

public record GenerateReportsResponse(List<ReportsRetrieved.PaymentData> payments) {
}
