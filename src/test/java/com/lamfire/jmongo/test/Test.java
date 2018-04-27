package com.lamfire.jmongo.test;

import com.lamfire.jmongo.JMongo;
import com.lamfire.jmongo.dao.DAO;
import com.lamfire.jmongo.test.entity.User;

/**
 * Created by linfan on 2017/4/21.
 */
public class Test {
    public static void main(String[] args) {
        DAO<User,String> dao = JMongo.getDAO("db0","test","USER0",User.class);
        long count = dao.count();
        System.out.println("count : " + count);

        User user = new User();
        user.setId("1000001");
        user.setNickname("lamfire");
        user.setAge(18);
        //dao.save(user);

        dao.removeField(user.getId(),"queue");

        for(int i=0;i<10;i++) {
            dao.addFieldValue(user.getId(), "queue", i);
        }

        System.out.println(dao.getFieldValue(user.getId(),"queue"));

        //System.out.println(dao.removeLastFieldValue(user.getId(),"queue"));


        //System.out.println(dao.getFieldValue(user.getId(),"queue"));

//        DBObject query = new BasicDBObject();
//        DBObject fields  = new BasicDBObject();
//        DBObject opts = new BasicDBObject();
//
//        opts.put("$slice",-2);
//
//        query.put("_id",user.getId());
//        fields.put("queue",opts);
//
//        DBObject ret = dao.getCollection().findOne(query,fields);
        System.out.println(dao.getFieldValueSlice(user.getId(),"queue",15));

    }
}
