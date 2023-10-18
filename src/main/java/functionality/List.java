package functionality;

import fileWorks.SAVF;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class List
{

    private ArrayList<String> words;

    public List(ArrayList<String> words) {this.words = words;}

    public List init()
    {
        if (words.size() == 1) return new List(words);
        else
        {
            SAVF savf = new SAVF("config.savf");
            savf.scan();

            String raw = savf.getValue("list-commands");
            String[] pieces = raw.split(",");

            for (String p: pieces) if (words.get(1).equals(p)) return new List(words);
        }

        return new List(null);
    }

    public String execute()
    {
        if (words != null)
        {
            String result = null;

            if (words.size() == 1) result = list();
            else if (words.get(1).equals("at")) result = at();
            else if (words.get(1).equals("which")) result = which();
            else if (words.get(1).equals("ends")) result = ends();
            else if (words.get(1).equals("starts")) result = starts();

            return result;
        }
        else return TerminalExceptions.getException(TerminalExceptions.Failures.INVALID_SUB_COMMAND);
    }


    private String list()
    {
        String result = null;

        SAVF savf = new SAVF("config.savf");
        savf.scan();

        Path path = Paths.get(savf.getValue("bubble-loc"));

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(path))
        {
            for (Path file: stream)
            {
                if (result == null) result = file.getFileName().toString();
                else result += "\n" + file.getFileName().toString();
            }
        }
        catch (IOException | DirectoryIteratorException e) {e.printStackTrace();}

        return result;
    }

    private String at()
    {
        String result = null;

        if (words.size() == 3)
        {
            SAVF savf = new SAVF("config.savf");
            savf.scan();

            Path path = Paths.get(savf.getValue("bubble-loc") + "/" + words.get(2));

            try(DirectoryStream<Path> stream = Files.newDirectoryStream(path))
            {
                for (Path file: stream)
                {
                    if (result == null) result = file.getFileName().toString();
                    else result += "\n" + file.getFileName().toString();
                }
            }
            catch (IOException | DirectoryIteratorException e) {result = e.getMessage();}
        }
        else result = TerminalExceptions.getException(TerminalExceptions.Failures.NO_PLACE);

        return result;
    }

    private String which()
    {
        String result = null;



        return result;
    }

    private String ends()
    {
        String result = null;

        if (words.size() == 3)
        {
            SAVF savf = new SAVF("config.savf");
            savf.scan();

            Path path = Paths.get(savf.getValue("bubble-loc") + "/" + words.get(2));

            try(DirectoryStream<Path> stream = Files.newDirectoryStream(path))
            {
                ArrayList<String> items = new ArrayList<>();
                for (Path file: stream) items.add(file.getFileName().toString());
                for (String i: items)
                    if (i.endsWith(words.get(2)))
                    {
                        if (result == null) result = i;
                        else result += "\n" + i;
                    }

                if (result == null) result = "";
            }
            catch (IOException | DirectoryIteratorException e) {result = e.getMessage();}
        }

        return result;
    }

    private String starts()
    {
        String result = null;

        if (words.size() == 3)
        {
            SAVF savf = new SAVF("config.savf");
            savf.scan();

            Path path = Paths.get(savf.getValue("bubble-loc") + "/" + words.get(2));

            try(DirectoryStream<Path> stream = Files.newDirectoryStream(path))
            {
                ArrayList<String> items = new ArrayList<>();
                for (Path file: stream) items.add(file.getFileName().toString());
                for (String i: items)
                    if (i.startsWith(words.get(2)))
                    {
                        if (result == null) result = i;
                        else result += "\n" + i;
                    }

                if (result == null) result = "";
            }
            catch (IOException | DirectoryIteratorException e) {result = e.getMessage();}
        }

        return result;
    }

}
