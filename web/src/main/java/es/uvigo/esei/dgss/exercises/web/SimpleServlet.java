package es.uvigo.esei.dgss.exercises.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import es.uvigo.esei.dgss.exercises.domain.entities.Photo;
import es.uvigo.esei.dgss.exercises.domain.entities.Comment;
import es.uvigo.esei.dgss.exercises.domain.entities.User;
import es.uvigo.esei.dgss.exercises.domain.entities.UserFriend;
import es.uvigo.esei.dgss.exercises.domain.entities.UserPostLike;
import es.uvigo.esei.dgss.exercises.domain.entities.Post;
import es.uvigo.esei.dgss.exercises.domain.entities.Video;
import es.uvigo.esei.dgss.exercises.service.PostEJB;
import es.uvigo.esei.dgss.exercises.service.UserEJB;

@WebServlet("/SimpleServlet")
public class SimpleServlet extends HttpServlet {

    @Inject
    private Facade facade;

    @Resource
    private UserTransaction transaction;

    @EJB
    private UserEJB userEJB;

    @EJB
    private PostEJB postEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        PrintWriter writer = resp.getWriter();

        writer.println("<html>");
        writer.println("<body>");
        writer.println("<h1>Facade tests</h1>");
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='0'>Task 2.0. Fill database (click once on each deploy)"
                + "</button></form>");

        /*Facade tasks*/

        //region Exercise 1 Task 2

        //1.
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='1'>Task 2.1. Create User"
                + "</button></form>");

        //2.
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='2'>Task 2.2. Create a friendship between two given users"
                + "</button></form>");

        //3.
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='3'>Task 2.3. Get all friends of a given user"
                + "</button></form>");

        //4.
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='4'>Task 2.4. Get all posts of the friends of a given user"
                + "</button></form>");

        //5.
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='5v1'>Task 2.5(v1). Get the posts that have been commented by the friends of a given user after a given date"
                + "</button></form>");
        //5.
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='5v2'>Task 2.5(v2). Get the posts that have been commented by the friends of a given user after a given date"
                + "</button></form>");
        //6.
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='6'>Task 2.6. Get the users which are friends of a given user who like a given post"
                + "</button></form>");
        //7.
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='7'>Task 2.7. Give me all the pictures a given user likes"
                + "</button></form>");

        //endregion Exercise 1 Task 2

        /*Exercise 2 EJB tasks*/

        //Exercise 2 Task 1
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='1e'>Task UserEJB. Create User Friend."
                + "</button></form>");

        //Exercise 2 Task 2
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='2e'>Task UserEJB. User Likes Post."
                + "</button></form>");

        //Exercise 2 Task 3
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='3e'>Task PostEJB. Add comments to a Post."
                + "</button></form>");
        //Update user
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='4e'>Update user manola."
                + "</button></form>");

        writer.println("</body>");
        writer.println("</html>");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        writer.println("<html><body>");
        if (req.getParameter("task").equals("0")) {
            task0(req, resp, writer);
        } else if (req.getParameter("task").equals("1")) {
            task1(req, resp, writer);
        } else if (req.getParameter("task").equals("2")) {
            task2(req, resp, writer);
        } else if (req.getParameter("task").equals("3")) {
            task3(req, resp, writer);
        } else if (req.getParameter("task").equals("4")) {
            task4(req, resp, writer);
        } else if (req.getParameter("task").equals("5v1")) {
            task5v1(req, resp, writer);
        } else if (req.getParameter("task").equals("5v2")) {
            task5v2(req, resp, writer);
        } else if (req.getParameter("task").equals("6")) {
            task6(req, resp, writer);
        } else if (req.getParameter("task").equals("7")) {
            task7(req, resp, writer);
        } else if (req.getParameter("task").equals("1e")) {
            taskUserEJBCreateFriend(req, resp, writer);
        } else if (req.getParameter("task").equals("2e")) {
            taskUserLikesPost(req, resp, writer);
        } else if (req.getParameter("task").equals("3e")) {
            taskPostEJBAddComments(req, resp, writer);
        } else if (req.getParameter("task").equals("4e")) {
            taskUpdateUser(req, resp, writer);
        }

        writer.println("</body></html>");
    }


    /*Facade Tasks*/

    //Task 0 Fill DataBase
    private void task0(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer)
            throws IOException {
        // work with Facade

        try {
            transaction.begin();

            facade.fillDataBase();

            writer.println("DataBase filled successfully");

            writer.println("<a href='SimpleServlet'>Go to menu</a>");

            transaction.commit();

        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                 | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    //Task 1 Create User
    private void task1(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer)
            throws IOException {
        // work with Facade

        try {
            transaction.begin();

            // Task 2.1
            User u = facade.addUser(UUID.randomUUID().toString(), "name", "password", new byte[]{});
            writer.println("User " + u.getLogin() + " created successfully");

            writer.println("<br><a href='SimpleServlet'>Go to menu</a>");

            transaction.commit();

        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                 | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    //Task 2 Create a friendship
    private void task2(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer)
            throws IOException {
        // work with Facade

        try {

            transaction.begin();

            // Task 2.2
            User userA = new User("pepe", "name", "pass", new byte[]{});
            User userB = new User("manolo", "name", "pass", new byte[]{});

            UserFriend uf = facade.addUserFriend(userA, userB);
            writer.println(uf.getUserA().getLogin() + " and " + uf.getUserB().getLogin() + " are friends now.");

            writer.println("<br><a href='SimpleServlet'>Go to menu</a>");

            transaction.commit();

        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                 | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }


    private void task3(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer)
            throws IOException {
        // work with Facade

        try {

            transaction.begin();
            User userMaria = new User("manola", "name", "pass", new byte[]{});


            // Task 2.3
            List<User> userFriends = facade.getUserFriends(userMaria);

            writer.println("<b>Friends of " + userMaria.getLogin() + ":</b><br><br>");

            for (User userFriend : userFriends) {
                writer.println(userFriend.getLogin() + "<br>");
            }

            writer.println("<br><a href='SimpleServlet'>Go to menu</a>");

            transaction.commit();

        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                 | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    private void task4(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer)
            throws IOException {
        // work with Facade

        try {

            transaction.begin();

            User userManola = new User("manola", "name", "pass", new byte[]{});

            // Task 2.4
            List<Post> userFriendsPosts = facade.getFriendsPostsFromUser(userManola);

            writer.println("<b>Posts of Friends of " + userManola.getLogin() + ":</b><br><br>");

            for (Post post : userFriendsPosts) {
                writer.println("User " + post.getUser().getLogin() + " post: " + post.getId() + "<br>");
            }

            writer.println("<br><a href='SimpleServlet'>Go to menu</a>");

            transaction.commit();

        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                 | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    private void task5v1(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer)
            throws IOException {
        // work with Facade

        try {

            transaction.begin();

            User userManola = new User("manola", "name", "pass", new byte[]{});
            Date commentDate = new Date();
            try {
                String string = "January 2, 2021";
                DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                commentDate = format.parse(string);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Task 2.5
            List<Post> userFriendsPosts = facade.getCommentedPostsByUserFriendsV1(userManola, commentDate);

            writer.println("<b>Posts commented by the Friends of   " + userManola.getLogin() + ":</b><br><br>");

            for (Post post : userFriendsPosts) {
                writer.println("User " + post.getUser().getLogin() + " commented post " + post.getId() + " from " + post.getUser().getLogin() + "   <br>");
            }

            writer.println("<br><a href='SimpleServlet'>Go to menu</a>");

            transaction.commit();

        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                 | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    private void task5v2(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer)
            throws IOException {
        // work with Facade

        try {

            transaction.begin();

            User userManola = new User("manola", "name", "pass", new byte[]{});
            Date commentDate = new Date();
            try {
                String string = "January 2, 2021";
                DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                commentDate = format.parse(string);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Task 2.5
            List<Comment> userFriendsPosts = facade.getCommentedPostsByUserFriendsV2(userManola, commentDate);

            writer.println("<b>Posts commented by the Friends of   " + userManola.getLogin() + ":</b><br><br>");

            for (Comment comment : userFriendsPosts) {
                writer.println("User " + comment.getUser().getLogin() + " commented post " + comment.getPost().getId() + " from " + comment.getPost().getUser().getLogin() + "   <br>");
            }

            writer.println("<br><a href='SimpleServlet'>Go to menu</a>");

            transaction.commit();

        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                 | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    private void task6(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer)
            throws IOException {
        // work with Facade

        try {

            transaction.begin();

            User userManola = new User("manola", "name", "pass", new byte[]{});
            User userPepa = new User("pepa", "name", "pass", new byte[]{});

            Video video1 = new Video();
            video1.setDuration(123);
            Post post1 = video1;
            post1.setUser(userPepa);
            post1.setId(1);

            UserPostLike userPostLike = new UserPostLike();
            userPostLike.setUser(userManola);
            userPostLike.setPost(post1);

            // Task 2.6
            List<User> userFriends = facade.getFriendsOfAnUserWhoLikesAPost(userPostLike);

            writer.println("<b>Friends of " + userManola.getLogin() + " who likes post " + post1.getId() + " are:</b><br><br>");

            for (User friends : userFriends) {
                writer.println("- " + friends.getLogin() + "<br>");
            }

            writer.println("<br><a href='SimpleServlet'>Go to menu</a>");

            transaction.commit();

        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                 | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    private void task7(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer)
            throws IOException {
        // work with Facade

        try {

            transaction.begin();

            User userManola = new User("manola", "name", "pass", new byte[]{});

            // Task 2.7
            List<Photo> photoList = facade.getPhotosUserLikes(userManola);

            writer.println("<b>Pictures who likes" + userManola.getLogin() + " are:</b><br><br>");

            for (Photo photo : photoList) {
                writer.println("-Photo " + photo.getId() + " from " + photo.getUser().getLogin() + "<br>");
            }

            writer.println("<br><a href='SimpleServlet'>Go to menu</a>");

            transaction.commit();

        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                 | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }


    //Task UserEJB Create a friendship
    private void taskUserEJBCreateFriend(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer)
            throws IOException {
        // work with Facade

        try {
            User userA = new User("pepeEJB", "name", "pass", new byte[]{});
            User userB = new User("manoloEJB", "name", "pass", new byte[]{});

            transaction.begin();

            userEJB.createUser(userA);
            userEJB.createUser(userB);

            UserFriend uf = userEJB.addUserFriend(userA, userB);
            writer.println(uf.getUserA().getLogin() + " and " + uf.getUserB().getLogin() + " are friends now by EJB.");

            writer.println("<br><a href='SimpleServlet'>Go to menu</a>");

            transaction.commit();

        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                 | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    private void taskUserLikesPost(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer)
            throws IOException {
        // work with Facade

        try {
            User userA = new User("pepeEJBLike", "name", "pass", new byte[]{});
            User userM = new User("manolaCreateAPost", "name", "pass", new byte[]{});
            Photo photo1 = new Photo();
            photo1.setContent("photo content");
            Post postP = photo1;
            postP.setUser(userM);

            transaction.begin();

            userEJB.createUser(userA);
            userEJB.createUser(userM);
            postEJB.createPost(postP);

            userEJB.addUserPostLike(userA, postP);
            writer.println(userA.getLogin() + " likes the post" + postP.getId() + " created by " + userM.getLogin());

            writer.println("<br><a href='SimpleServlet'>Go to menu</a>");

            transaction.commit();

        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                 | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    public void taskPostEJBAddComments(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer)
            throws IOException {

        try {
            User userM = new User("manola", "name", "pass", new byte[]{});

            Photo photo1 = new Photo();
            photo1.setContent("photo content");
            Post postP = photo1;
            postP.setUser(userM);

            Comment comment1FromUserM = new Comment();
            comment1FromUserM.setComment("Comment 1 from manola");
            comment1FromUserM.setUser(userM);

            Comment comment2FromUserM = new Comment();
            comment2FromUserM.setComment("Comment 2 from manola");
            comment2FromUserM.setUser(userM);

            transaction.begin();


            postEJB.createPost(postP);
            postEJB.addCommentToPost(postP, comment1FromUserM);
            postEJB.addCommentToPost(postP, comment2FromUserM);

            writer.println("Added comments to post" + postP.getId());


            writer.println("<br><a href='SimpleServlet'>Go to menu</a>");

            transaction.commit();

        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                 | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    private void taskUpdateUser(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer)
            throws IOException {
        // work with Facade

        try {
            transaction.begin();

            User userManola = userEJB.findUserByLogin("manola");
            userManola.setPassword("12345");

            userEJB.updateUser(userManola);

            writer.println("Update");
            writer.println("<br><a href='SimpleServlet'>Go to menu</a>");

            transaction.commit();

        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                 | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

}
