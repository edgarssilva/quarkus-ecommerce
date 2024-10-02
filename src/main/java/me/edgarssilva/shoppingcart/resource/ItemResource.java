package me.edgarssilva.shoppingcart.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import me.edgarssilva.shoppingcart.model.Item;
import me.edgarssilva.shoppingcart.service.ItemService;

@Path("/items")
public class ItemResource {
    @Inject
    ItemService itemService;

    @GET
    public Response getAllItems() {
        return Response.ok(itemService.getAllItems()).build();
    }

    @POST
    public Response addItem(Item item) {
        itemService.addItem(item);
        return Response.status(Response.Status.CREATED).entity(item).build();
    }

    @GET
    @Path("/{itemId}")
    public Response getItem(@PathParam("itemId") Long itemId) {
        return Response.ok(itemService.getItem(itemId)).build();
    }

    @PUT
    @Path("/{itemId}")
    public Response updateItem(@PathParam("itemId") Long itemId, Item item) {
        item.setId(itemId);
        itemService.updateItem(item);
        return Response.ok(item).build();
    }

    @DELETE
    @Path("/{itemId}")
    public Response deleteItem(@PathParam("itemId") Long itemId) {
        itemService.removeItem(itemId);
        return Response.noContent().build();
    }
}
