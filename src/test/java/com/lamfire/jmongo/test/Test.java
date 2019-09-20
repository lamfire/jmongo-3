package com.lamfire.jmongo.test;

import com.lamfire.jmongo.JMongo;
import com.lamfire.jmongo.dao.DAO;
import com.lamfire.jmongo.query.CriteriaContainer;
import com.lamfire.jmongo.query.Query;
import com.lamfire.jmongo.test.dao.UserDAO;
import com.lamfire.jmongo.test.entity.User;
import com.lamfire.json.JSON;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        UserDAO dao = new UserDAO();
        System.out.println(dao.count());

        for(int i=0;i<100;i++){
            User user = new User();
            user.setId("user-"+i);
            user.setAge(i);
            user.setNickname("nickname-" +i);
            dao.save(user);
        }

        Query<User> query = dao.createQuery();
        CriteriaContainer c1 = query.criteria("nickname").endsWith("0");
        CriteriaContainer c2 = query.criteria("age").greaterThan(60);

        query.or(c1,c2);
        List<User> list = query.asList();
        for(User u : list){
            System.out.println(JSON.toJSONString(u));
        }

    }
}
