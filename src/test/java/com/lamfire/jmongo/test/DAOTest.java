package com.lamfire.jmongo.test;

import com.lamfire.code.PUID;
import com.lamfire.jmongo.dao.DAO;
import com.lamfire.jmongo.test.dao.UserDAO;
import com.lamfire.jmongo.test.entity.User;
import com.lamfire.json.JSON;
import com.lamfire.utils.Asserts;
import com.lamfire.utils.RandomUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by linfan on 2017/4/18.
 */
public class DAOTest {
    public static void main(String[] args) {
        DAO<User,String> dao = new UserDAO();

        long count = dao.count();
        System.out.println("count : " + count);

        User user = new User();
        user.setId(PUID.makeAsString());
        user.setNickname("lamfire");
        user.setAge(18);
        dao.save(user);

        String id = user.getId();

        Asserts.equalsAssert(count + 1,dao.count());

        System.out.println("save : " + JSON.toJSONString(user));

        ///////////////////////////////////////////////////////////
        int age = RandomUtils.nextInt(100);
        dao.setFieldValue(id,"age",age);
        Asserts.equalsAssert(age,dao.getFieldValue(id,"age"));

        String ip = RandomUtils.randomIPAddr();
        dao.setFieldValue(id,"ip",ip,true);
        Asserts.equalsAssert(ip,dao.getFieldValue(id,"ip"));




        dao = new UserDAO();

        Map<String,Object> map =  dao.getAsMap(id);
        System.out.println("query as map : " +map);
        Asserts.equalsAssert(id,map.get("_id"));
        Asserts.equalsAssert(map.get("ip"),ip);
        Asserts.equalsAssert(map.get("age"),age);
        Asserts.equalsAssert(map.get("nickname"),"lamfire");


        List<User> users = dao.createQuery().field("_id").equal(id).excludeFields("nickname").asList();
        for(User u : users){
            System.out.println("query not nickname: " + JSON.toJSONString(u));
            Asserts.nullAssert(u.getNickname());
        }
    }
}
