package com.onyx.concurrency.example.singleton;

/**
 * @author zk
 * @Description:
 * @date 2018-12-17 10:46
 */
public enum  EnumSingletonDemo {
    INSTANCE;

    private String name;

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

}
