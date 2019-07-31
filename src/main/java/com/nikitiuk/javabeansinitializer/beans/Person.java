package com.nikitiuk.javabeansinitializer.beans;

public class Person {
    private String name;
    private String cityOfResidence;
    private String job;
    private Integer salary;
    private Boolean wealthy;
    private Person boss;

    @Override
    public String toString() {
        if(boss != null && salary!=null && wealthy != null) {
            return "Name: " + name + "\nCity of Residence: " + cityOfResidence + "\nJob: " + job +
                    "\nSalary: " + salary.toString() + "\nIs Wealthy? " + wealthy.toString() + "\nBoss: " + boss.toString();
        } else if(boss != null && salary!=null) {
            return "Name: " + name + "\nCity of Residence: " + cityOfResidence + "\nJob: " + job +
                    "\nSalary: " + salary.toString() + "\nWho Knows Whether Is Wealthy" + "\nBoss: " + boss.toString();
        } else if (salary != null && wealthy != null){
            return "Name: " + name + "\nCity of Residence: " + cityOfResidence + "\nJob: " + job +
                    "\nSalary: " + salary.toString() + "\nIs Wealthy? " + wealthy.toString() + "\nNo Boss Assigned Yet";
        } else if (boss != null && wealthy != null){
            return "Name: " + name + "\nCity of Residence: " + cityOfResidence + "\nJob: " + job +
                    "\nNo Salary Assigned Yet" + "\nIs Wealthy? " + wealthy.toString() + "\nBoss: " + boss.toString();
        } else if (boss != null) {
            return "Name: " + name + "\nCity of Residence: " + cityOfResidence + "\nJob: " + job +
                    "\nNo Salary Assigned Yet" + "\nWho Knows Whether Is Wealthy" + "\nBoss: " + boss.toString();
        } else if (salary != null) {
            return "Name: " + name + "\nCity of Residence: " + cityOfResidence + "\nJob: " + job +
                    "\nSalary: " + salary.toString() + "\nWho Knows Whether Is Wealthy" + "\nNo Boss Assigned Yet";
        } else if (wealthy != null) {
            return "Name: " + name + "\nCity of Residence: " + cityOfResidence + "\nJob: " + job +
                    "\nNo Salary Assigned Yet" + "\nIs Wealthy? " + wealthy.toString() + "\nNo Boss Assigned Yet";
        } else {
            return "Name: " + name + "\nCity of Residence: " + cityOfResidence + "\nJob: " + job +
                    "\nNo Salary Assigned Yet" + "\nWho Knows Whether Is Wealthy" + "\nNo Boss Assigned Yet";
        }
    }

    public void init(){
        System.out.println("Now invoking main method of Person...");
        System.out.println(name + " lives in " + cityOfResidence);
        if(boss != null){
            System.out.println("And his boss is " + boss.name);
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
        this.salary = (Integer)salary.intValue();
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
