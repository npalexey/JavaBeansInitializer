package com.nikitiuk.javabeansinitializer.beans;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class Person {

    private String name;
    private String cityOfResidence;
    private String job;
    private Integer salary;
    private Boolean wealthy;
    private Person boss;

    @Override
    public String toString() {
        return String.format("Name: %1$s\nCity of Residence: %2$s\nJob: %3$s\nSalary: %4$s\nIs %1$s Wealthy? %5$s\nBoss: %6$s",
                StringUtils.defaultIfBlank(name, "no name"),
                StringUtils.defaultIfBlank(cityOfResidence, "no city of residence"),
                StringUtils.defaultIfBlank(job, "no job"),
                StringUtils.defaultIfBlank(Objects.toString(salary, null), "no salary assigned yet"),
                StringUtils.defaultIfBlank(Objects.toString(wealthy, null), "Don't know whether is wealthy"),
                StringUtils.defaultIfBlank(Objects.toString(boss, null), "no boss assigned yet"));
    }

    public void init() {
        System.out.println("Now invoking main method of Person...:");
        System.out.println(name + " lives in " + cityOfResidence);
        if (boss != null) {
            System.out.println("And his boss is " + boss.name + ";");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityOfResidence() {
        return cityOfResidence;
    }

    public void setCityOfResidence(String cityOfResidence) {
        this.cityOfResidence = cityOfResidence;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Number salary) {
        this.salary = (Integer) salary.intValue();
    }

    public Boolean isWealthy() {
        return wealthy;
    }

    public void setWealthy(Boolean wealthy) {
        this.wealthy = wealthy;
    }

    public Person getBoss() {
        return boss;
    }

    public void setBoss(Person boss) {
        this.boss = boss;
    }
}