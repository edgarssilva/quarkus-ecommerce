# Altice Labs E-Commerce Exercise

This project is a simple e-commerce application that allows the creation of shopping carts and the addition of items to them. 
All data is stored in memory and is lost when the application is stopped.

## OpenAPI

The OpenAPI documentation is available at [http://localhost:8080/q/openapi](http://localhost:8080/q/openapi).    
This is generated automatically by the application and provides a way to interact with the API.  \
But I've also added the file `openapi.yaml` for simplicity.
 
## Kubernetes
By running the `.\mvnw install` command, the application will build a container using Jib and push it to local Docker.  \
Then it will generate Kubernetes manifests at `/target/kubernetes`, I've also added minikube manifests that I've used for testing.  \
Also for simplicity, I've added the file `kubernetes.yaml` to the root of the project, for a quick example.

## Choices made

### Items

I was not sure if the items were meant to be created inside, but I've assumed that they are created outside the cart, and only add the quantity to the cart. 

### Checkout

I was also not clear on what the checkout should do since there would be no payment or users, 
I've assumed that it should only return the total price of each item and the total price of the cart.

