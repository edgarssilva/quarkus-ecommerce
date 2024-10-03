package me.edgarssilva.shoppingcart.exception;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Error response")
public record ErrorResponse(String message) {
}
