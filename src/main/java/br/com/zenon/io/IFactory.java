package br.com.zenon.io;

public interface IFactory<T> {
    T create(String[] row) throws Exception;
}
