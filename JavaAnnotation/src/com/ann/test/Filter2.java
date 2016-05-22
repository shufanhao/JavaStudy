package com.ann.test;

/**
 * Created by haofan on 5/22/2016.
 */
@Table("department")
public class Filter2 {
    @Column("name")
    private String name;
    @Column("leader")
    private String leader;
    @Column("amount")
    private int amount;
    @Column("id")
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }





}

