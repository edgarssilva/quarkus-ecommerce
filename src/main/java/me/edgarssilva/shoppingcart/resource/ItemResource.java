package me.edgarssilva.shoppingcart.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import me.edgarssilva.shoppingcart.model.Item;
import me.edgarssilva.shoppingcart.service.ItemService;
import me.edgarssilva.shoppingcart.exception.ErrorResponse;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("/items")
public class ItemResource {
    @Inject
    ItemService itemService;

    @GET
    @Operation(summary = "Get all items")
    @APIResponse(responseCode = "200", description = "List of items", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Item.class, type = SchemaType.ARRAY)))
    public Response getAllItems() {
        return Response.ok(itemService.getAllItems()).build();
    }

    @POST
    @Operation(summary = "Add a new item")
    @APIResponse(responseCode = "201", description = "Item added", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Item.class)))
    public Response addItem(Item item) {
        itemService.addItem(item);
        return Response.status(Response.Status.CREATED).entity(item).build();
    }

    @GET
    @Path("/{itemId}")
    @Operation(summary = "Get an item by ID")
    @APIResponse(responseCode = "200", description = "Item details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Item.class)))
    @APIResponse(responseCode = "404", description = "Item not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public Response getItem(@PathParam("itemId") Long itemId) {
        return Response.ok(itemService.getItem(itemId)).build();
    }

    @PUT
    @Path("/{itemId}")
    @Operation(summary = "Update an item by ID")
    @APIResponse(responseCode = "200", description = "Item updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Item.class)))
    @APIResponse(responseCode = "404", description = "Item not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public Response updateItem(@PathParam("itemId") Long itemId, Item item) {
        item.setId(itemId);
        itemService.updateItem(item);
        return Response.ok(item).build();
    }

    @DELETE
    @Path("/{itemId}")
    @Operation(summary = "Delete an item by ID")
    @APIResponse(responseCode = "204", description = "Item deleted")
    @APIResponse(responseCode = "404", description = "Item not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public Response deleteItem(@PathParam("itemId") Long itemId) {
        itemService.removeItem(itemId);
        return Response.noContent().build();
    }
}