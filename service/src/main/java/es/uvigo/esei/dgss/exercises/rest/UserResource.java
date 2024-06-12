package es.uvigo.esei.dgss.exercises.rest;

import es.uvigo.esei.dgss.exercises.domain.entities.User;
import es.uvigo.esei.dgss.exercises.domain.entities.UserFriend;
import es.uvigo.esei.dgss.exercises.service.UserEJB;

import javax.ejb.EJB;
import javax.persistence.EntityExistsException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Context
    private UriInfo uriInfo;

    @EJB
    private UserEJB userEJB;

    @POST
    public Response addUser(User user) {
        userEJB.createUser(user);

        String login = user.getLogin();
        URI loginUri = uriInfo.getAbsolutePathBuilder()
                .path(login)
                .build();

        return Response.created(loginUri).build();
    }

    @POST
    @Path("/{username}/friend")
    public Response friendshipRequest(@PathParam("username") String login,
                                      @QueryParam("login") String loginRequested) {
        UserFriend friendRequested = this.userEJB.requestFriendship(login, loginRequested);

        URI friendUri = uriInfo.getAbsolutePathBuilder().build();
        return Response.created(friendUri).build();
    }

    @GET
    @Path("/{username}/friend/friendshipRequests")
    public Response getFriendshipRequest(@PathParam("username") String login) {
        List<UserFriend> friendsRequest = this.userEJB.getFriendshipRequest(login);

        return Response.ok(friendsRequest).build();
    }

    @PUT
    @Path("/{username}/friend")
    public Response acceptFriendshipRequest(@PathParam("username") String login,
                                            @QueryParam("login") String loginRequested) {

        this.userEJB.acceptUserFriend(login, loginRequested);

        return Response.ok().build();
    }
}
