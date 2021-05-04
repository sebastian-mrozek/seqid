package io.sequenceservice.service.db;

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
    @Index(columnNames = {"namespace", "name"}, unique = true)
    @Column(length = 99)
    String namespace;

    @NotNull
    @Column(length = 99)
    String name;

    long start;

    Short length;

    Long max;

    String prefix;

    String suffix;

    public DSequenceDefinition(String namespace, String name, long start, Short length, Long max, String prefix, String suffix) {
        this.namespace = namespace;
        this.name = name;
        this.start = start;
        this.length = length;
        this.max = max;
        this.prefix = prefix;
        this.suffix = suffix;
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

    public Short getLength() {
        return length;
    }

    public void setLength(Short length) {
        this.length = length;
    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {
        this.max = max;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public String toString() {
        return "DSequenceDefinition{" +
                "id=" + id +
                ", namespace='" + namespace + '\'' +
                ", name='" + name + '\'' +
                ", start=" + start +
                ", length=" + length +
                ", max=" + max +
                ", prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                '}';
    }
}
