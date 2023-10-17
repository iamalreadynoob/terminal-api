package functionality;

import fileWorks.SAVF;

import java.util.ArrayList;

public class List
{

    private ArrayList<String> words;

    public List(ArrayList<String> words) {this.words = words;}

    public List init()
    {
        SAVF savf = new SAVF("config.savf");
        savf.scan();

        String raw = savf.getValue("list-commands");
        String[] pieces = raw.split(",");

        for (String p: pieces) if (words.get(1).equals(p)) return new List(words);
        return new List(null);
    }

    public String execute()
    {
        if (words != null)
        {
            String result = null;
            

            return result;
        }
        else return TerminalExceptions.getException(TerminalExceptions.Failures.INVALID_SUB_COMMAND);
    }

}
