package br.com.zenon.io;

import java.util.Optional;

public interface IMapper<T> {
    Optional<T> parse(String[] values) throws Exception;
    void initializeHeaders(String[] headers);
}
