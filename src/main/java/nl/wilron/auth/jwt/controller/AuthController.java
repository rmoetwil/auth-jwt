package nl.wilron.auth.jwt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nl.wilron.auth.jwt.business.AuthBusiness;
import nl.wilron.auth.jwt.domain.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Authentication and Authorisation controller.
 *
 * @author Ronald Moetwil
 */
@Api(value = "Auth")
@RestController
public class AuthController {

    @Autowired
    private AuthBusiness authBusiness;


    @ApiOperation(value = "Authenticate")
    @RequestMapping(value = "/auth", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public Credentials authenticate(@RequestBody @Valid Credentials credentials) {
        return authBusiness.authenticate(credentials);
    }

    @ApiOperation(value = "Refresh Token")
    @RequestMapping(value = "/refresh", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public Credentials refreshToken(@RequestBody @Valid Credentials credentials) {
        return authBusiness.refresh(credentials);
    }
}
