package com.shubham16598.reportingapp;

import java.util.Date;

/**
 * Created by shubham16598 on 2/12/17.
 */

public class info {
    public String problem;
    public String description;
    public String date;

    public String getProblem() {
        return problem;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "info{" +
                "problem='" + problem + '\'' +
                ", description='" + description + '\'' +
                ", Area='" + date + '\'' +
                '}';
    }
}
