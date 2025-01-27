package org.acme.resource;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.acme.dto.GenerateReportsResponse;
import org.acme.service.ReportService;

/**
 * @author: Tomas Durnek (s233788)
 */
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
            GenerateReportsResponse reportsForAllPayments = reportService.getReportsForAllPayments();
            return Response.ok(reportsForAllPayments).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

}