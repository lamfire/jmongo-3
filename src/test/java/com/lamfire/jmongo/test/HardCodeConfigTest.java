package com.lamfire.jmongo.test;

import com.lamfire.jmongo.JMongo;
import com.lamfire.jmongo.JMongoZoneOptions;
import com.lamfire.jmongo.JMongoZoneRegistry;
import com.lamfire.jmongo.dao.DAO;
import com.lamfire.jmongo.test.entity.User;

import java.net.UnknownHostException;

/**
 * Created by linfan on 2018/5/9.
 */
public class HardCodeConfigTest {
    public static void main(String[] args) throws UnknownHostException {
        String zone = "hardcodeDB";
        JMongoZoneOptions options = new JMongoZoneOptions(zone);
        options.addHost("192.168.56.11:27017");
        options.setConnectionsPerHost(4);
        options.setAuth(true);
        options.setUser("root");
        options.setPassword("123456");
        JMongo.registerZoneOptions(options);

        DAO<User,String> userDAO  = JMongo.getDAO(zone,"HardCodeDB","User", User.class);

        User user = new User();
        user.setId("1000001");
        user.setNickname("lamfire");
        user.setAge(18);
        userDAO.save(user);

        System.out.println(userDAO.count());
    }
}
