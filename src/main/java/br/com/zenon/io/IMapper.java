package br.com.zenon.io;

public interface IMapper<T> {
    T parse(String[] values) throws Exception;
    void initializeHeaders(String[] headers);
}
