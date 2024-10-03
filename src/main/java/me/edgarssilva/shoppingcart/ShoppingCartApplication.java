package me.edgarssilva.shoppingcart;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@ApplicationPath("/api")
@OpenAPIDefinition(
        info = @Info(
                title = "Shopping Cart API",
                version = "1.0.0",
                description = "Simple shopping cart API for altice labs exercise"
        )
)
public class ShoppingCartApplication extends Application {
}
