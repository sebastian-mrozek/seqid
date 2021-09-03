package io.sequenceservice.service;

import io.ebean.annotation.Platform;
import io.ebean.dbmigration.DbMigration;
import java.io.IOException;

public class GenerateDbMigration {

    /**
     * Generate the DDL for the next DB migration.
     */
    public static void main(String[] args) throws IOException {

        DbMigration dbMigration = DbMigration.create();
        dbMigration.setPlatform(Platform.H2);
        dbMigration.setPathToResources("service/src/main/resources");

        dbMigration.generateMigration();
    }
}