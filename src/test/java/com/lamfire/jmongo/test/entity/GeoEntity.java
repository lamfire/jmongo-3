package com.lamfire.jmongo.test.entity;

import com.lamfire.jmongo.annotations.Entity;
import com.lamfire.jmongo.annotations.Id;
import com.lamfire.jmongo.annotations.Indexed;
import com.lamfire.jmongo.utils.IndexDirection;

/**
 * Created by linfan on 2017/8/9.
 */
@Entity
public class GeoEntity {
    @Id
    private String id;

    private String name;

    @Indexed(IndexDirection.GEO2D)
    private double[] location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }
}
