package com.lamfire.jmongo.test;

import com.lamfire.code.PUID;
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
public class MapTest {
    public static void main(String[] args) {
        UserDAO dao = new UserDAO();

        long count = dao.count();
        System.out.println("count : " + count);

        User user = new User();
        user.setId(PUID.makeAsString());
        user.setNickname("lamfire");
        user.setAge(18);

        JSON js = JSON.fromJavaObject(user);
        System.out.println("JSON : " + js);
        dao.save(js);

        String id = user.getId();


        Map<String,Object> map =  dao.getAsMap(id);
        System.out.println("query as map : " +map);

    }
}
