# auth-jwt
A project demonstrating JWT based Authentication and Authorization

This application supports two API's.
One to authenticate, which will return a token.
One to refresh a given token.

The tokens are signed with RSA 2048 bit private/public keypair.

The token uses the default JWT claims:

  * subject
  * issue time
  * expiration time
  
Additionally it adds the following claim for easy one go authorization:

  * roles

This version uses a Stub for authentication and authorization but can be replaced by e.g. an LDAP based authentication solution.


See application.properties for additional config options.


## To run the application
```gradle bootRun```

## Swagger documentation
```http://localhost:8080/swagger-ui.html```


