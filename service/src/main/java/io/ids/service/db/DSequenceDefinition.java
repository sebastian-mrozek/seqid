package io.ids.service.db;

import io.ebean.Model;
import io.ebean.annotation.Index;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class DSequenceDefinition extends Model {

    @Id
    UUID id;

    @NotNull
    @Index(columnNames = {"namespace", "name"})
    @Column(length = 99)
    String namespace;

    @NotNull
    @Column(length = 99)
    String name;

    long start;

    public DSequenceDefinition(String namespace, String name, long start) {
        this.namespace = namespace;
        this.name = name;
        this.start = start;
    }

    public UUID getId() {
        return this.id;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }
}
