package br.com.zenon.presentation.implementation;

import br.com.zenon.io.implementation.PresenterConfiguration;
import br.com.zenon.presentation.IPresenter;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class Presenter implements IPresenter {

    public Presenter() {
    }

    @Override
    public void displayMessageFromKey(String key) {
        IO.println(getResourceBundle(key));
    }

    @Override
    public void displayFormattedMessage(String key, String data) {
        String pattern = getResourceBundle(key);
        String message = MessageFormat.format(pattern, data);
        IO.println(message);
    }

    private String getResourceBundle(String key) {
        ResourceBundle resourceBundle = PresenterConfiguration.getInstance().getBundle();
        return  resourceBundle.getString(key);
    }
}
