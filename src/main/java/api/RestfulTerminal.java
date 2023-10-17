package api;

import fileWorks.SAVF;
import functionality.List;
import functionality.TerminalExceptions;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;

@Path("/terminal")
public class RestfulTerminal
{
    private ArrayList<String> initCommands = new ArrayList<>();
    @PostConstruct
    public void init()
    {
        SAVF savf = new SAVF("config.savf");
        savf.scan();
        String raw = savf.getValue("init-commands");
        String[] init = raw.split(",");
        for (String i: init) initCommands.add(i);
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response execute(String command)
    {
        String result = null;

        String[] temp = command.split(" ");
        ArrayList<String> words = new ArrayList<>();
        for (String w: temp) if (!w.isEmpty() && !w.isBlank()) words.add(w);

        if (!words.isEmpty() && initCommands.contains(words.get(0)))
        {
            if (words.get(0).equals("list")) result = new List(words).init().execute();
        }
        else result = TerminalExceptions.getException(TerminalExceptions.Failures.INVALID_INIT_COMMAND);

        return Response.ok(result).build();
    }

}
