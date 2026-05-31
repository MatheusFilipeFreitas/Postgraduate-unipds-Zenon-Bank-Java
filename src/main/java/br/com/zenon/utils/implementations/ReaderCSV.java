package br.com.zenon.utils.implementations;

import br.com.zenon.utils.IMapper;

import java.util.ArrayList;
import java.util.List;

public class ReaderCSV<T> extends AbstractReader<T> {
    @Override
    protected List<T> parse(String[] lines, IMapper<T> mapper) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String[] values =  line.split(",");
            if (i == 0) {
                mapper.initializeHeaders(values);
                continue;
            }
            try {
                list.add(mapper.parse(values));
            } catch (Exception e) {
                IO.println("Error: " + e.getMessage() + " -> " + line);
            }
        }
        return list;
    }
}
