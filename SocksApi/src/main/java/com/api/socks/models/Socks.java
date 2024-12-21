package com.api.socks.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Socks {
    public enum SocksColors{
        white,
        black,
        red,
        orange,
        yellow,
        green,
        blue,
        violate
    };
    @Id
    @GeneratedValue
    private long id;
    private SocksColors color;
    private int cotton;
    private int count;

    @Override
    public String toString(){
        return "{\"id\":" + id + "," +
                "\"color\":" + "\"" + color + "\"" + "," +
                "\"cotton\":" + cotton + "," +
                "\"count\":" + count + "}";
    }

    public Socks(SocksColors color, int cotton, int count){
        this.color = color;
        this.cotton = cotton;
        this.count = count;
    }

    public Socks(){}

    public long getId() {
        return id;
    }

    public SocksColors getColor() {
        return color;
    }

    public int getCotton() {
        return cotton;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setColor(SocksColors color) {
        this.color = color;
    }

    public void setCotton(int cotton) {
        this.cotton = cotton;
    }
}
