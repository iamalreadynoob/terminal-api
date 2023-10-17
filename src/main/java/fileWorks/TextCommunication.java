package fileWorks;

import java.io.*;
import java.util.ArrayList;

public class TextCommunication
{

    public static void write(String path, ArrayList<String> lines)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path)));

            for (int i = 0; i < lines.size(); i++)
            {
                if (i == 0) writer.write(lines.get(i));
                else writer.write("\n" + lines.get(i));
            }

            writer.close();
        }
        catch (IOException e){e.printStackTrace();}
    }

    public static void write(String path, String[] lines)
    {
        ArrayList<String> temp = new ArrayList<>();
        for (String l: lines) temp.add(l);

        write(path, temp);
    }

    public static void write(String path, String line)
    {
        ArrayList<String> temp = new ArrayList<>();
        temp.add(line);
        write(path, line);
    }

    public static void append(String path, ArrayList<String> lines)
    {
        ArrayList<String> allLines = read(path);
        for (String ln: lines) allLines.add(ln);
        write(path, allLines);
    }

    public static void append(String path, String[] lines)
    {
        ArrayList<String> temp = new ArrayList<>();
        for (String ln: lines) temp.add(ln);
        append(path, temp);
    }

    public static void append(String path, String line)
    {
        ArrayList<String> temp = new ArrayList<>();
        temp.add(line);
        append(path, temp);
    }

    public static ArrayList<String> read(String path)
    {
        ArrayList<String> lines = new ArrayList<>();

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));

            String ln;
            while ((ln = reader.readLine()) != null) lines.add(ln);
        }
        catch (IOException e){e.printStackTrace();}

        return lines;
    }

    public static ArrayList<String> read(String path, int from, int to)
    {
        ArrayList<String> lines = new ArrayList<>();

        ArrayList<String> allLines = read(path);
        for (int i = from; i < to; i++) lines.add(allLines.get(i));

        return lines;
    }

}
