package br.com.zenon.io.implementation;

import java.util.Locale;
import java.util.ResourceBundle;

public class PresenterConfiguration {
    private Locale locale;
    private ResourceBundle resourceBundle;
    private static PresenterConfiguration instance;

    private PresenterConfiguration() {
        this.locale = Locale.getDefault();
    }

    public static PresenterConfiguration getInstance() {
        if (instance == null) {
            instance = new PresenterConfiguration();
        }
        return instance;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        setBundle();
    }

    public void setCustomLocale(String language, String country) {
        setLocale(Locale.of(language, country));
    }

    public ResourceBundle getBundle() {
        return resourceBundle;
    }

    private void setBundle() {
        this.resourceBundle = ResourceBundle.getBundle("messages", this.locale);
    }
}
