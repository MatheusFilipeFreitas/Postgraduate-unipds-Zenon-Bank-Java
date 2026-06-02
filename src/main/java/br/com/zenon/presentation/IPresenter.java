package br.com.zenon.presentation;

import java.util.Locale;

public interface IPresenter {
    void displayMessageFromKey(String key);
    void displayFormattedMessage(String key, String data);
}
