package com.sena.senacomic.util;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class Migration implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        if (oldVersion == 0 && newVersion == 1) {
            RealmObjectSchema newSchema = schema.create("FavoriteBean");
            newSchema.addField("id", String.class);
            newSchema.addField("origin", int.class);
            newSchema.addField("comicId", String.class);
            newSchema.addField("coverUrl", String.class);
            newSchema.addField("title", String.class);
            newSchema.addField("anthor", String.class);
            newSchema.addField("lastChapterId", String.class);
            newSchema.addField("lastChapterId", String.class);
            newSchema.addField("date", Date.class);
            oldVersion++;
        }
    }
}