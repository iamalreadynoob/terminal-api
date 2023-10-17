package fileWorks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CSV
{

    private final String path;
    private String equivalent, separator, nullSign;
    private ArrayList<String> headers;
    private ArrayList<ArrayList<String>> columns;

    public CSV(String path)
    {
        this.path = path;
        headers = new ArrayList<>();
        columns = new ArrayList<>();
        setDefault();
    }

    public void setEquivalent(String equivalent) {this.equivalent = equivalent;}
    public String getEquivalent() {return equivalent;}
    public void setSeparator(String separator) {this.separator = separator;}
    public String getSeparator() {return separator;}
    public void setNull(String nullSign) {this.nullSign = nullSign;}
    public String getNull() {return nullSign;}

    public void setDefault()
    {
        equivalent = "%comma%";
        separator = ",";
        nullSign = "NULL";
    }

    public void create(ArrayList<ArrayList<String>> columnsWithHeaders)
    {
        clear();

        for (int i = 0; i < columnsWithHeaders.size(); i++)
        {
            headers.add(columnsWithHeaders.get(i).get(0));
            columns.add(new ArrayList<>());
        }

        for (int i = 1; i < columnsWithHeaders.get(0).size(); i++)
        {
            for (int j = 0; j < columnsWithHeaders.size(); j++)
                columns.get(j).add(columnsWithHeaders.get(j).get(i));
        }
    }

    public void create(ArrayList<String> headers, ArrayList<ArrayList<String>> columns)
    {
        clear();

        if (headers.size() == columns.size())
        {
            boolean flag = true;

            for (int i = 0; i < columns.size(); i++)
                if (columns.get(0).size() != columns.get(i).size())
                {
                    flag = false;
                    break;
                }

            if (flag)
            {
                this.headers = headers;
                this.columns = columns;
            }
        }


    }

    public void scan()
    {
        clear();

        ArrayList<String> rawLines = TextCommunication.read(path);
        ArrayList<String> lines = new ArrayList<>();
        for (String ln: rawLines) if (!ln.isBlank() && !ln.isEmpty()) lines.add(ln);

        boolean flag = isDB(lines);

        if (flag)
        {
            String[] hList = lines.get(0).split(separator);
            for (String h: hList) headers.add(h.replaceAll(equivalent, separator));

            for (int i = 0; i < headers.size(); i++) columns.add(new ArrayList<>());

            for (int i = 1; i < lines.size(); i++)
            {
                String[] cells = lines.get(i).split(separator);
                for (int j = 0; j < headers.size(); j++) columns.get(j).add(cells[j].replaceAll(equivalent, separator));
            }
        }
        else
        {
            System.err.println("The database is broken, contains much or less cells at least in one row");
            System.exit(1);
        }
    }

    public void clear()
    {
        headers.clear();
        columns.clear();
    }

    public ArrayList<String> getHeaders() {return headers;}

    public ArrayList<ArrayList<String>> getColumns() {return columns;}

    public ArrayList<String> getColumn(String header)
    {
        if (headers.contains(header))
        {
            int index = headers.indexOf(header);
            return columns.get(index);
        }

        return new ArrayList<>();
    }

    public void addColumn(String header, ArrayList<String> column, boolean isRemainNull)
    {
        if (!headers.contains(header))
        {
            if (isRemainNull && column.size() <= columns.get(0).size())
            {
                if (column.size() != columns.get(0).size())
                    while (column.size() != columns.get(0).size())
                        column.add(nullSign);

                headers.add(header);
                columns.add(column);
            }
            else if (!isRemainNull && columns.get(0).size() == column.size())
            {
                headers.add(header);
                columns.add(column);
            }
        }
    }

    public void deleteColumn(String header)
    {
        if (headers.contains(header))
        {
            int index = headers.indexOf(header);
            headers.remove(index);
            columns.remove(index);
        }

    }

    public void setHeaderName(String header, String newHeaderName)
    {
        if (headers.contains(header) && !headers.contains(newHeaderName))
            headers.set(headers.indexOf(header), newHeaderName);
    }

    public void addRow(ArrayList<String> rows)
    {
        boolean flag = true;

        for (String r: rows)
            if (r.split(separator).length != headers.size())
            {
                flag = false;
                break;
            }

        if (flag)
        {
            for (String r: rows)
            {
                String[] cells = r.split(separator);
                for (int j = 0; j < headers.size(); j++) columns.get(j).add(cells[j].replaceAll(equivalent, separator));
            }
        }
        else System.err.println("Rows weren't added because they don't have exact cell amount");
    }

    public void addRow(String[] rows)
    {
        ArrayList<String> temp = new ArrayList<>();
        for (String r: rows) temp.add(r);
        addRow(temp);
    }

    public void addRow(String row)
    {
        ArrayList<String> temp = new ArrayList<>();
        temp.add(row);
        addRow(temp);
    }

    public String getCell(String header, int row)
    {
        String cell = null;

        if (headers.contains(header) && row >= 0 && row < columns.get(0).size())
        {
            cell = columns.get(headers.indexOf(header)).get(row);
            if (cell.isEmpty() || cell.isBlank()) cell = nullSign;
        }


        return cell;
    }

    public void setCell(String header, int row, String newVal)
    {
        if (newVal == null) newVal = nullSign;

        if (headers.contains(header) && row >= 0 && row < columns.get(0).size())
            columns.get(headers.indexOf(header)).set(row, newVal);
    }

    public void deleteCell(String header, int row)
    {
        setCell(header, row, nullSign);
    }

    public boolean isNull(String header, int row)
    {
        boolean flag = false;

        String cell = getCell(header, row);
        if (cell.equals(nullSign)) flag = true;

        return flag;
    }

    public void deleteRow(int index)
    {
        if (index < columns.get(0).size() && index >= 0)
            for (int i = 0; i < headers.size(); i++)
                columns.get(i).remove(index);
    }

    public Map<String, String> getMap(String keyHeader, String valueHeader)
    {
        Map<String, String> map = new HashMap<>();

        if (headers.contains(keyHeader) && headers.contains(valueHeader))
        {
            ArrayList<String> keyColumn = getColumn(keyHeader);
            ArrayList<String> valColumn = getColumn(valueHeader);

            ArrayList<String> keys = new ArrayList<>();

            for (int i = 0; i < keyColumn.size(); i++)
            {
                if (!keys.contains(keyColumn.get(i)))
                {
                    keys.add(keyColumn.get(i));
                    map.put(keyColumn.get(i), valColumn.get(i));
                }
            }
        }

        return map;
    }

    public void save()
    {
        ArrayList<String> lines = new ArrayList<>();

        String headerLine = null;
        for (int i = 0; i < headers.size(); i++)
        {
            if (i == 0) headerLine = headers.get(i);
            else headerLine += "," + headers.get(i);
        }
        lines.add(headerLine);

        for (int i = 0; i < columns.get(0).size(); i++)
        {
            String line = null;

            for (int j = 0; j < headers.size(); j++)
            {
                if (j == 0) line = columns.get(j).get(i);
                else line += "," + columns.get(j).get(i);
            }

            lines.add(line);
        }

        TextCommunication.write(path, lines);

        clear();
    }

    private boolean isDB(ArrayList<String> lines)
    {
        boolean flag = true;

        int headerAmount = lines.get(0).split(separator).length;

        for (int i = 1; i < lines.size(); i++)
        {
            int cells = lines.get(i).split(separator).length;

            if (headerAmount != cells)
            {
                flag = false;
                break;
            }
        }

        return flag;
    }
}
