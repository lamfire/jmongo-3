package com.lamfire.jmongo.test;

import com.lamfire.jmongo.query.UpdateResults;
import com.lamfire.jmongo.test.dao.UserDAO;
import com.lamfire.jmongo.test.entity.User;
import com.lamfire.json.JSON;
import com.lamfire.utils.Asserts;
import com.lamfire.utils.Maps;

import java.util.Map;

public class MultIncrementTest {
    public static void testMultiInc(){
        UserDAO dao = new UserDAO();
        User user = new User();
        user.setAge(10);
        user.setCoins(10);
        user.setCount(10);
        user.setId("U9999");
        dao.save(user);


        Map<String,Number> incMap = Maps.newHashMap();
        incMap.put("age",1);
        incMap.put("coins",100);
        incMap.put("count",2);
        user = dao.incrementAndGet(user.getId(),incMap,true,true,true);

        System.out.println(JSON.toJSONString(user));

        Asserts.equalsAssert(user.getAge(),11);
        Asserts.equalsAssert(user.getCount(),12);
        Asserts.equalsAssert(user.getCoins(),110);
    }
    public static void main(String[] args) {
        String uid = "U00009";
        UserDAO dao = new UserDAO();
        User user = dao.get(uid);


        Map<String,Number> incMap = Maps.newHashMap();
        incMap.put("age",1);
        incMap.put("coins",100);
        incMap.put("count",2);
        incMap.put("version",1);

        UpdateResults results = dao.increment(uid,"count",2,"version",user.getVersion());
        System.out.println(JSON.toJSONString(user));
        System.out.println(results.getUpdatedCount());


    }
}
