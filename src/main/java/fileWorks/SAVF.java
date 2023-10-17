package fileWorks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SAVF
{

    private final String path;
    private ArrayList<String> params, values;

    public SAVF(String path)
    {
        this.path = path;
        params = new ArrayList<>();
        values = new ArrayList<>();
    }

    public void create(Map<String, String> connections)
    {
        clear();

        Set<String> params = connections.keySet();
        for (String param: params)
        {
            params.add(param);
            values.add(connections.get(param));
        }
    }

    public void scan()
    {
        clear();

        ArrayList<String> lines = TextCommunication.read(path);

        for (String ln: lines)
        {
            if (ln.trim().startsWith("@"))
            {
                int index = ln.trim().indexOf("=");
                String param = ln.trim().substring(1, index);

                int valIndex = ln.indexOf("=");
                String value = ln.substring(valIndex + 1);

                if (!params.contains(param))
                {
                    params.add(param);
                    values.add(value);
                }
            }
        }

        clear();
    }

    public void clear()
    {
        params.clear();
        values.clear();
    }

    public void save()
    {
        ArrayList<String> lines = new ArrayList<>();

        for (int i = 0; i < params.size(); i++)
            lines.add("@" + params.get(i) + "=" + values.get(i));

        TextCommunication.write(path, lines);
    }

    public ArrayList<String> getParams() {return params;}
    public ArrayList<String> getValues() {return values;}

    public String getValue(String param)
    {
        if (params.contains(param)) return values.get(params.indexOf(param));
        else return null;
    }

    public void addParam(String param, String value)
    {
        if (!params.contains(param) && param != null && value != null)
        {
            params.add(param);
            values.add(value);
        }
    }

    public void deleteParam(String param)
    {
        if (params.contains(param))
        {
            int index = params.indexOf(param);
            params.remove(index);
            values.remove(index);
        }
    }

    public void setParamName(String paramName, String newParamName)
    {
        if (params.contains(paramName))
            params.set(params.indexOf(paramName), newParamName);
    }

    public void setValue(String param, String newValue)
    {
        if (params.contains(param))
            values.set(params.indexOf(param), newValue);
    }

    public Map<String, String> getMap()
    {
        Map<String, String> relations = new HashMap<>();

        ArrayList<String> keys = new ArrayList<>();

        for (int i = 0; i < params.size(); i++)
            if (!keys.contains(params.get(i)))
            {
                keys.add(params.get(i));
                relations.put(params.get(i), values.get(i));
            }

        return relations;
    }

}
