package questionnaire.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import questionnaire.CreationHelper;
import questionnaire.IOHelper;
import questionnaire.Result;
import questionnaire.data.Event;
import questionnaire.data.Event.EventType;
import questionnaire.data.PlayerStatus;
import questionnaire.data.Task;

import com.google.inject.Inject;
import com.sun.jersey.api.view.Viewable;

@Path("/")
public class QuestionService {
    private IOHelper ioHelper;
    private CreationHelper creation;

    @Inject
    public QuestionService(CreationHelper creation, IOHelper ioHelper) {
        this.creation = creation;
        this.ioHelper = ioHelper;
    }

    @GET
    public Viewable index() {
        return new Viewable("/index.jsp");
    }

    @POST
    @Path("exists")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Result<Boolean> exists(String auth) {
        try {
            if (!creation.isValid(auth)) {
                return Result.fail("invalid name");
            }
            return Result.ok(ioHelper.exists(auth));
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Result<String> register(String auth) {
        try {
            if (!creation.isValid(auth)) {
                return Result.fail("invalid name");
            }
            if (ioHelper.exists(auth)) {
                return Result.fail("name is already in use");
            }
            PlayerStatus p = creation.createPlayer(auth);
            return Result.ok(p.getName());
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @POST
    @Path("hasNext")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Result<Boolean> hasNext(String auth) {
        try {
            PlayerStatus player = ioHelper.readPlayer(auth);
            boolean hasNextTask = player.hasNextTask();
            return Result.ok(hasNextTask);
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @POST
    @Path("getNext")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Result<Task> getNext(String auth) {
        try {
            PlayerStatus player = ioHelper.readPlayer(auth);
            if (!player.hasNextTask()) {
                return Result.fail("There is no next page");
            }

            player.toggleNext();
            ioHelper.write(player);
            String taskId = player.getCurrentTaskId();
            Task next = ioHelper.readTaskWithoutSolution(taskId);
            return Result.ok(next);
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @POST
    @Path("submit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Result<Void> submit(Event event) {
        try {
            checkStatus(event);
            ioHelper.log(event);
            return Result.ok();
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @POST
    @Path("log")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Result<Void> log(Event event) {
        try {
            checkStatus(event);
            ioHelper.log(event);
            return Result.ok();
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * @throws IllegalStateException
     *             if auth isn't valid or page does not match the active page
     * @throws IllegalArgumentException
     *             if page is null
     */
    private void checkStatus(Event e) {
        if (!ioHelper.exists(e.auth)) {
            throw new IllegalStateException("Authentification is unknown");
        }
        String currentTaskId = ioHelper.readPlayer(e.auth).getCurrentTaskId();
        if (EventType.RELOAD.equals(e.type)) {
            e.taskId = currentTaskId;
        }
        boolean isCurrentPage = currentTaskId.equals(e.taskId);
        if (!isCurrentPage) {
            throw new IllegalStateException("The active page of the player doesn't match the given pageID");
        }
    }
}