package com.pp.perfectposture.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Sensor.
 */
@Entity
@Table(name = "SENSOR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Sensor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "device_id")
    private String deviceid;

    @OneToOne
    private User user;
    
    @OneToMany(mappedBy="sensor")
    private Set<SensorValue> sensorValues;

	public Set<SensorValue> getSensorValues() {
		return sensorValues;
	}

	public void setSensorValues(Set<SensorValue> sensorValues) {
		this.sensorValues = sensorValues;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDevice_id() {
        return deviceid;
    }

    public void setDevice_id(String device_id) {
        this.deviceid = device_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Sensor sensor = (Sensor) o;

        if ( ! Objects.equals(id, sensor.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", device_id='" + deviceid + "'" +
                '}';
    }
}
