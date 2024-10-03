package me.edgarssilva.shoppingcart.resource;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("/readiness")
@Produces("application/json")
public class ReadinessResource {

    @GET
    @Operation(summary = "Readiness check")
    @APIResponse(responseCode = "200", description = "Check if the application is ready", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    public Response readiness() {
        return Response.ok("Application Ready").build();
    }
}
