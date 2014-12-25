package com.bv.zzpmaatschap.rest;

import com.bv.zzpmaatschap.eao.inferface.IOfferEAO;
import com.bv.zzpmaatschap.model.User;
import com.bv.zzpmaatschap.services.UserService;

import javax.ejb.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read the contents of the members
 * table.
 */
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
@Path("/auth")
public class LoginRESTService {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    @EJB(name = "userService")
    UserService userService;

    @EJB(beanName = "offerEAO", beanInterface = IOfferEAO.class)
    IOfferEAO offerEAO;

    @GET
    @Path("/logout")
    public Response logout(@Context HttpServletResponse response,
                           @Context HttpServletRequest request) {
        URI uri = URI.create("/app2/main.html");
        try {
            request.logout();
        } catch (ServletException e) {
            uri = URI.create("/app2/main.html?logoutfailed");
        }
        return Response.seeOther(uri).build();
    }

    @POST
    @Path("/login")
    @Consumes(value = "application/x-www-form-urlencoded")
    public Response postLoginCredentials(@Context HttpServletResponse response,
                                         @Context HttpServletRequest request, @FormParam(USERNAME) String username,@FormParam(PASSWORD) String password) {
        URI uri;

        try {



            User user = userService.getCurrentUser(username);
            if (user == null) {
                throw new IllegalAccessException("Geen user bekend in database.");
            }
            if (user.getRole().equals("user_temp")) {
                if (offerEAO.getUserOffers(user.getId()).size() > 2) {
                    throw new IllegalAccessException("Maximum aantal offers bereikt.");
                }
            }

            request.login(username, password);
            uri = URI.create("/app2/main.html");

        } catch (Exception e) {
            e.printStackTrace();
            uri = URI.create("/app2/login.html?status=failed");
        }


        return Response.seeOther(uri).build();

    }
}
