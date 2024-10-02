package me.edgarssilva.shoppingcart.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import me.edgarssilva.shoppingcart.model.CartEntry;
import me.edgarssilva.shoppingcart.model.ShoppingCart;
import me.edgarssilva.shoppingcart.service.ShoppingCartService;

@Path("/shopping-carts")
public class ShoppingCartResource {

    @Inject
    ShoppingCartService service;

    @GET
    public Response getAllCarts() {
        return Response.ok(service.getAllCarts()).build();
    }

    @POST
    public Response createCart() {
        ShoppingCart cart = service.createCart();
        return Response.status(Response.Status.CREATED).entity(cart).build();
    }

    @GET
    @Path("/{cartId}")
    public Response getCart(@PathParam("cartId") Long cartId) {
        return Response.ok(service.getCart(cartId)).build();
    }

    @DELETE
    @Path("/{cartId}")
    public Response deleteCart(@PathParam("cartId") Long cartId) {
        service.deleteCart(cartId);
        return Response.noContent().build();
    }

    @GET
    @Path("/{cartId}/items")
    public Response getCartItems(@PathParam("cartId") Long cartId) {
        return Response.ok(service.getCartItems(cartId)).build();
    }

    @GET
    @Path("/{cartId}/items/{itemId}")
    public Response getCartItem(@PathParam("cartId") Long cartId, @PathParam("itemId") Long itemId) {
        return Response.ok(service.getCartItem(cartId, itemId)).build();
    }

    @POST
    @Path("/{cartId}/items")
    public Response addItem(@PathParam("cartId") Long cartId, CartEntry entry) {
        service.addCartItem(cartId, entry);
        return Response.status(Response.Status.CREATED).entity(entry).build();
    }

    @PUT
    @Path("/{cartId}/items")
    public Response updateItem(@PathParam("cartId") Long cartId, CartEntry entry) {
        service.updateCartItem(cartId, entry);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{cartId}/items/{itemId}")
    public Response deleteItem(@PathParam("cartId") Long cartId, @PathParam("itemId") Long itemId) {
        service.removeCartItem(cartId, itemId);
        return Response.noContent().build();
    }

    @POST
    @Path("/{cartId}/clear")
    public Response clearCart(@PathParam("cartId") Long cartId) {
        service.clearCartItems(cartId);
        return Response.ok().build();
    }

    @POST
    @Path("/{cartId}/checkout")
    public Response checkout(@PathParam("cartId") Long cartId) {
        if (service.isCartEmpty(cartId))
            return Response.status(Response.Status.BAD_REQUEST).entity("Cart is empty").build();

        //TODO: Implement checkout logic
        return Response.ok().entity(service.getCart(cartId)).build();
    }
}