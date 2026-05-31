package br.com.zenon.utils;

public interface IFactory<T> {
    T create(String[] row) throws Exception;
}
