package com.lamfire.jmongo.test;

import com.lamfire.jmongo.query.UpdateResults;
import com.lamfire.jmongo.test.dao.UserDAO;
import com.lamfire.jmongo.test.entity.User;
import com.lamfire.json.JSON;
import com.lamfire.utils.Asserts;
import com.lamfire.utils.Maps;
import com.lamfire.utils.RandomUtils;

import java.util.Map;

public class MultIncrementTest {


    public static void main(String[] args) {
        final String uid = "U9999";
        final int age = 10;
        final int coins = 100;
        final int count = 0;
        final int version =1;

        UserDAO dao = new UserDAO();
        User user = new User();
        user.setAge(age);
        user.setCoins(coins);
        user.setCount(count);
        user.setId("U9999");
        user.setVersion(version);
        dao.save(user);
        System.out.println(JSON.toJSONString(user));

        Map<String,Number> incMap = Maps.newHashMap();
        incMap.put("age",1);
        incMap.put("coins",100);
        incMap.put("count",2);
        incMap.put("version",1);

        //test incrementAndGet
        user = dao.incrementAndGet(uid,incMap);
        System.out.println(JSON.toJSONString(user));
        Asserts.equalsAssert(user.getAge(),age + 1);
        Asserts.equalsAssert(user.getCoins(),coins + 100);
        Asserts.equalsAssert(user.getCount(),count + 2);
        Asserts.equalsAssert(user.getVersion(),version + 1);

        //test decrementAndGet
        user = dao.decrementAndGet(uid,incMap);
        System.out.println(JSON.toJSONString(user));
        Asserts.equalsAssert(user.getAge(),age);
        Asserts.equalsAssert(user.getCoins(),coins);
        Asserts.equalsAssert(user.getCount(),count);
        Asserts.equalsAssert(user.getVersion(),version);

        //test incrementAndUpdate
        String nickname = RandomUtils.randomTextWithFixedLength(6);
        Map<String,Object> updateMap = Maps.newHashMap();
        updateMap.put("nickname", nickname);
        UpdateResults results = dao.incrementAndUpdate(uid,incMap,updateMap,"version",user.getVersion());
        System.out.println("Update : " + results.getUpdatedCount());
        user = dao.get(uid);
        System.out.println(JSON.toJSONString(user));
        Asserts.equalsAssert(user.getAge(),age + 1);
        Asserts.equalsAssert(user.getCoins(),coins + 100);
        Asserts.equalsAssert(user.getCount(),count + 2);
        Asserts.equalsAssert(user.getVersion(),version + 1);
        Asserts.equalsAssert(user.getNickname(),nickname);


       //test decrementAndUpdate
        nickname = RandomUtils.randomTextWithFixedLength(6);
        updateMap.put("nickname", nickname);
        results = dao.decrementAndUpdate(uid,incMap,updateMap);
        System.out.println("Update : " + results.getUpdatedCount());
        user = dao.get(uid);
        System.out.println(JSON.toJSONString(user));
        Asserts.equalsAssert(user.getAge(),age );
        Asserts.equalsAssert(user.getCoins(),coins);
        Asserts.equalsAssert(user.getCount(),count);
        Asserts.equalsAssert(user.getVersion(),version);
        Asserts.equalsAssert(user.getNickname(),nickname);
    }
}
