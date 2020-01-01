package com.project.wdx.distributedsession.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private String name ;
    private String age;

    @Override
    public String toString() {
        return "{\"name\"=\""+name+"\",\"age\"=\""+age+"\"}";
    }
}
