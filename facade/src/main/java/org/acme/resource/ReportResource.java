package org.acme.resource;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.acme.service.ReportService;

@Path("/reports")
public class ReportResource {

    private final ReportService reportService;

    public ReportResource(ReportService reportservice) {
        this.reportService = reportservice;
    }

    @GET
    @Consumes("application/json")
    public Response getReports() {
        try {
            reportService.getReportsForAllPayments();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

}