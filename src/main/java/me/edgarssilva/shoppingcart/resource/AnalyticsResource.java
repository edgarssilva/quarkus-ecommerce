package me.edgarssilva.shoppingcart.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import me.edgarssilva.shoppingcart.model.KpiMetrics;
import me.edgarssilva.shoppingcart.service.AnalyticsService;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("/analytics")
public class AnalyticsResource {

    @Inject
    AnalyticsService service;

    @GET
    @APIResponse(responseCode = "200", description = "Retrieves key performance indicators (KPIs) for shopping carts.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = KpiMetrics.class)))
    public Response getKPIs() {
        return Response.ok(service.getKPIs()).build();
    }
}
