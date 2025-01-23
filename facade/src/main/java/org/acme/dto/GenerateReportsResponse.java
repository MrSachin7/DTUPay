package org.acme.dto;

import java.util.List;

public record GenerateReportsResponse(List<ReportData> payments) {
}
