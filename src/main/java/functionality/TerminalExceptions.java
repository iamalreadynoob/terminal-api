package functionality;

public class TerminalExceptions
{

    public static String getException(Failures failure)
    {
        if (failure == Failures.INVALID_INIT_COMMAND) return "INVALID INIT COMMAND: The main action is not defined.";
        else if (failure == Failures.INVALID_SUB_COMMAND) return "INVALID SUB COMMAND: The main action is defined but sub functionality is not defined.";
        else return "UNEXPECTED FAILURE: There is no trace for its reason.";
    }

    public enum Failures
    {
        INVALID_INIT_COMMAND, INVALID_SUB_COMMAND
    }

}
