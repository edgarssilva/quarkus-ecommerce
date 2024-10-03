package me.edgarssilva.shoppingcart.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import me.edgarssilva.shoppingcart.exception.ErrorResponse;
import me.edgarssilva.shoppingcart.model.CartEntry;
import me.edgarssilva.shoppingcart.model.CheckoutDetails;
import me.edgarssilva.shoppingcart.model.ShoppingCart;
import me.edgarssilva.shoppingcart.service.ShoppingCartService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("/shopping-carts")
public class ShoppingCartResource {

    @Inject
    ShoppingCartService service;

    @GET
    @Operation(summary = "Get all shopping carts")
    @APIResponse(responseCode = "200", description = "List of shopping carts", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShoppingCart.class, type = SchemaType.ARRAY)))
    public Response getAllCarts() {
        return Response.ok(service.getAllCarts()).build();
    }

    @POST
    @Operation(summary = "Create a new shopping cart")
    @APIResponse(responseCode = "201", description = "Shopping cart created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShoppingCart.class)))
    public Response createCart() {
        ShoppingCart cart = service.createCart();
        return Response.status(Response.Status.CREATED).entity(cart).build();
    }

    @GET
    @Path("/{cartId}")
    @Operation(summary = "Get a shopping cart by ID")
    @APIResponse(responseCode = "200", description = "Shopping cart details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShoppingCart.class)))
    @APIResponse(responseCode = "404", description = "Cart not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public Response getCart(@PathParam("cartId") Long cartId) {
        return Response.ok(service.getCart(cartId)).build();
    }

    @DELETE
    @Path("/{cartId}")
    @Operation(summary = "Delete a shopping cart by ID")
    @APIResponse(responseCode = "204", description = "Shopping cart deleted")
    @APIResponse(responseCode = "404", description = "Cart not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public Response deleteCart(@PathParam("cartId") Long cartId) {
        service.deleteCart(cartId);
        return Response.noContent().build();
    }

    @GET
    @Path("/{cartId}/items")
    @Operation(summary = "Get all items in a shopping cart")
    @APIResponse(responseCode = "200", description = "List of cart items", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartEntry.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "404", description = "Cart not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public Response getCartItems(@PathParam("cartId") Long cartId) {
        return Response.ok(service.getCartItems(cartId)).build();
    }

    @GET
    @Path("/{cartId}/items/{itemId}")
    @Operation(summary = "Get an item in a shopping cart by ID")
    @APIResponse(responseCode = "200", description = "Cart item details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartEntry.class)))
    @APIResponse(responseCode = "404", description = "Item not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public Response getCartItem(@PathParam("cartId") Long cartId, @PathParam("itemId") Long itemId) {
        return Response.ok(service.getCartItem(cartId, itemId)).build();
    }

    @POST
    @Path("/{cartId}/items")
    @Operation(summary = "Add an item to a shopping cart")
    @APIResponse(responseCode = "201", description = "Item added to cart", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartEntry.class)))
    @APIResponse(responseCode = "404", description = "Cart not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public Response addItem(@PathParam("cartId") Long cartId, CartEntry entry) {
        service.addCartItem(cartId, entry);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{cartId}/items")
    @Operation(summary = "Update an item in a shopping cart")
    @APIResponse(responseCode = "200", description = "Item updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartEntry.class)))
    @APIResponse(responseCode = "404", description = "Item not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public Response updateItem(@PathParam("cartId") Long cartId, CartEntry entry) {
        service.updateCartItem(cartId, entry);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{cartId}/items/{itemId}")
    @Operation(summary = "Remove an item from a shopping cart")
    @APIResponse(responseCode = "204", description = "Item removed from cart")
    @APIResponse(responseCode = "404", description = "Item not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public Response deleteItem(@PathParam("cartId") Long cartId, @PathParam("itemId") Long itemId) {
        service.removeCartItem(cartId, itemId);
        return Response.noContent().build();
    }

    @POST
    @Path("/{cartId}/clear")
    @Operation(summary = "Clear all items in a shopping cart")
    @APIResponse(responseCode = "200", description = "Cart cleared")
    @APIResponse(responseCode = "404", description = "Cart not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public Response clearCart(@PathParam("cartId") Long cartId) {
        service.clearCartItems(cartId);
        return Response.ok().build();
    }

    @POST
    @Path("/{cartId}/checkout")
    @Operation(summary = "Checkout a shopping cart")
    @APIResponse(responseCode = "200", description = "Cart details with total price of the items and the cart", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CheckoutDetails.class)))
    @APIResponse(responseCode = "400", description = "Cart is empty", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public Response checkout(@PathParam("cartId") Long cartId) {
        return Response.ok(service.checkout(cartId)).build();
    }
}