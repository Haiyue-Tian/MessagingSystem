package com.haiyue.messaging;

import lombok.Data;

@Data
public class Person {

    private String name;
    private String address;
    private int age;

    public String getHey() {
        return "Hey, " + this.name;
    }

    public void setNick(String nick) {
        this.name = "Nick" + nick;
    }
}
