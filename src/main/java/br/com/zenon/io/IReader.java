package br.com.zenon.io;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public interface IReader<T> {
    List<T> read(IMapper<T> mapper, String path, int limit) throws IOException;
    void readLazy(IMapper<T> mapper, String path, Consumer<List<T>> consumer) throws IOException, Exception;
}
