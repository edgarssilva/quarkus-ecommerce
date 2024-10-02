package me.edgarssilva.shoppingcart.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import me.edgarssilva.shoppingcart.model.Item;
import me.edgarssilva.shoppingcart.model.ShoppingCart;
import me.edgarssilva.shoppingcart.service.ShoppingCartService;

import java.util.List;
import java.util.Map;

@Path("/shopping-carts")
public class ShoppingCartResource {

    @Inject
    ShoppingCartService service;

    @GET
    public List<ShoppingCart> getAllCarts() {
        return service.getAllCarts();
    }

    @POST
    public Response createCart() {
        ShoppingCart cart = service.createCart();
        return Response.status(Response.Status.CREATED).entity(cart).build();
    }

    @GET
    @Path("/{cartId}")
    public Response getCart(@PathParam("cartId") Long cartId) {
        return service.getCart(cartId)
                .map(cart -> Response.ok(cart).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{cartId}")
    public Response deleteCart(@PathParam("cartId") Long cartId) {
        if (service.deleteCart(cartId))
            return Response.noContent().build();

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{cartId}/items")
    public Response getCartItems(@PathParam("cartId") Long cartId) {
        return service.getCart(cartId)
                .map(cart -> Response.ok(cart.getItems().values()).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/{cartId}/items/{itemId}")
    public Response getCartItem(@PathParam("cartId") Long cartId, @PathParam("itemId") Long itemId) {
        return service.getCart(cartId)
                .flatMap(cart -> cart.getItem(itemId))
                .map(item -> Response.ok(item).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/{cartId}/items")
    public Response addItem(@PathParam("cartId") Long cartId, Item item) {
        if (service.addItem(cartId, item.getId(), item.getQuantity()))
            return Response.status(Response.Status.CREATED).build();

        return Response.status(Response.Status.NOT_FOUND).entity("Cart not found").build();
    }

    @PUT
    @Path("/{cartId}/items")
    public Response updateItem(@PathParam("cartId") Long cartId, Item item) {
        if (service.getCart(cartId).isEmpty())
            return Response.status(Response.Status.NOT_FOUND).entity("Cart not found").build();

        if (!service.updateItem(cartId, item))
            return Response.status(Response.Status.NOT_FOUND).entity("Item not found").build();

        return Response.ok().build();
    }

    @DELETE
    @Path("/{cartId}/items/{itemId}")
    public Response deleteItem(@PathParam("cartId") Long cartId, @PathParam("itemId") Long itemId) {
        if (service.getCart(cartId).isEmpty())
            return Response.status(Response.Status.NOT_FOUND).entity("Cart not found").build();

        if (!service.removeItem(cartId, itemId))
            return Response.status(Response.Status.NOT_FOUND).entity("Item not found").build();

        return Response.noContent().build();
    }

    @POST
    @Path("/{cartId}/clear")
    public Response clearCart(@PathParam("cartId") Long cartId) {
        service.clearCart(cartId);
        return Response.noContent().build();
    }

    @POST
    @Path("/{cartId}/checkout")
    public Response checkout(@PathParam("cartId") Long cartId) {
        if (service.isCartEmpty(cartId))
            return Response.status(Response.Status.BAD_REQUEST).entity("Cart is empty").build();

        return service.getCart(cartId)
                .map(cart -> {
                    if (cart.getItems().isEmpty())
                        return Response.status(Response.Status.BAD_REQUEST).entity("Cart is empty").build();

                    return Response.ok()
                            .entity(Map.of("items", cart.getItems().values(), "totalCost", cart.getTotal()))
                            .build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).entity("Cart not found").build());
    }
}