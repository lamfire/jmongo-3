package com.lamfire.jmongo.test.entity;

import com.lamfire.jmongo.annotations.*;

/**
 * Created by linfan on 2017/4/18.
 */
@Indexes(value={@Index(value = "nickname_age_index",fields = {@Field(value = "nickname"),@Field(value = "age")})})
@Entity
public class User {
    @Id
    private String id;

    @Indexed
    private String nickname;

    @Indexed
    private int age;

    private long coins;

    private int count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
