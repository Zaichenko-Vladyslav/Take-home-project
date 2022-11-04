package com.spatial.laser.model;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Data
public class Houses implements Serializable {

    // As tables does not contain primary key, @EmbaddedId and @Embeddable in HouseId can help
    @EmbeddedId
    private HousesId id;

    public HousesId getId() {
        return id;
    }

    public void setId(HousesId id) {
        this.id = id;
    }
}