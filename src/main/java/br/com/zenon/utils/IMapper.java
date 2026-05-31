package br.com.zenon.utils;

public interface IMapper<T> {
    T parse(String[] values) throws Exception;
    void initializeHeaders(String[] headers);
}
