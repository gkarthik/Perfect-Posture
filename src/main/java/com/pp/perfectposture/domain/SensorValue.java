package com.pp.perfectposture.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pp.perfectposture.domain.util.CustomDateTimeDeserializer;
import com.pp.perfectposture.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SensorValue.
 */
@Entity
@Table(name = "SENSORVALUE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SensorValue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "sen1")
    private Long sen1;

    @Column(name = "sen2")
    private Long sen2;

    @Column(name = "sen3")
    private Long sen3;

    @Column(name = "sen4")
    private Long sen4;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "timestamp")
    private DateTime timestamp;

    @ManyToOne
    private Sensor sensor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSen1() {
        return sen1;
    }

    public void setSen1(Long sen1) {
        this.sen1 = sen1;
    }

    public Long getSen2() {
        return sen2;
    }

    public void setSen2(Long sen2) {
        this.sen2 = sen2;
    }

    public Long getSen3() {
        return sen3;
    }

    public void setSen3(Long sen3) {
        this.sen3 = sen3;
    }

    public Long getSen4() {
        return sen4;
    }

    public void setSen4(Long sen4) {
        this.sen4 = sen4;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SensorValue sensorValue = (SensorValue) o;

        if ( ! Objects.equals(id, sensorValue.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SensorValue{" +
                "id=" + id +
                ", sen1='" + sen1 + "'" +
                ", sen2='" + sen2 + "'" +
                ", sen3='" + sen3 + "'" +
                ", sen4='" + sen4 + "'" +
                ", timestamp='" + timestamp + "'" +
                '}';
    }
}
