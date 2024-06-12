package es.uvigo.esei.dgss.exercises.rest;

import es.uvigo.esei.dgss.exercises.domain.entities.*;
import es.uvigo.esei.dgss.exercises.service.PostEJB;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("/post")
public class PostResource {

    @EJB
    PostEJB postEJB;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/{username}/wallPosts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWallPosts(@PathParam("username") String login) {

        return Response.ok(postEJB.getWallPosts(login)).build();

    }

    @GET
    @Path("/{username}")
    public Response getPost(@PathParam("username") String login) {

        return Response.ok(postEJB.getUserPosts(login)).build();

    }

    @POST
    @Path("/{username}/like")
    public Response addPostLike(@PathParam("username") String login,
                                @QueryParam("id") int id) {
        UserPostLike like = postEJB.addPostLike(login, id);

        return Response.ok(like).build();
    }

    @POST
    @Path("/{username}/link")
    public Response addLink(@PathParam("username") String login, Link link) {
        Link linkAdded = postEJB.addLink(login, link);
        return Response.ok(linkAdded).build();
    }

    @POST
    @Path("/{username}/photo")
    public Response addPhoto(@PathParam("username") String login, Photo photo) {
        Photo photoAdded = postEJB.addPhoto(login, photo);
        return Response.ok(photoAdded).build();
    }

    @POST
    @Path("/{username}/video")
    public Response addVideo(@PathParam("username") String login, Video video) {
        Video videoAdded = postEJB.addVideo(login, video);
        return Response.ok(videoAdded).build();
    }

    @PUT
    @Path("/{username}/link")
    public Response updateLink(@PathParam("username") String login, Link link) {
        Link linkAdded = postEJB.updateLink(login, link);
        return Response.ok(linkAdded).build();
    }

    @PUT
    @Path("/{username}/photo")
    public Response updatePhoto(@PathParam("username") String login, Photo photo) {
        Photo photoAdded = postEJB.updatePhoto(login, photo);
        return Response.ok(photoAdded).build();
    }

    @PUT
    @Path("/{username}/video")
    public Response updateVideo(@PathParam("username") String login, Video video) {
        Video videoUpdate = postEJB.updateVideo(login, video);
        return Response.ok(videoUpdate).build();
    }

    @DELETE
    @Path("/{username}/{postId}")
    public Response removePost(@PathParam("username") String login,
                               @PathParam("postId") int postId) {

        try {
            postEJB.removePost(login, postId);
        } catch (WebApplicationException e) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return Response.noContent().build();
    }

}
