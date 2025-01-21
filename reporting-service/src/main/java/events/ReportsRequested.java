package events;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ReportsRequested {
    private String correlationId;
    private ReportType reportType;
    private String id;

    public ReportsRequested() {
    }

    public ReportsRequested(String correlationId) {
        this.correlationId = correlationId;
        this.reportType = ReportType.ALL_REPORTS;
    }

    public ReportsRequested(String correlationId, ReportType reportType, String id) {
        this.correlationId = correlationId;
        this.reportType = reportType;
        this.id = id;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
