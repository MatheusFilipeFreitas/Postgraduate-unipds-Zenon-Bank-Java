package br.com.zenon;

import br.com.zenon.app.ZenonApplication;
import br.com.zenon.app.ZenonMigration;

public class Main {
    public static void main(String[] args) throws Exception {
        new ZenonApplication().run();
        new ZenonMigration().run();
    }
}
