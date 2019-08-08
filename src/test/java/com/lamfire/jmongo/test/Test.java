package com.lamfire.jmongo.test;

import com.lamfire.jmongo.JMongo;
import com.lamfire.jmongo.dao.DAO;
import com.lamfire.jmongo.test.dao.UserDAO;
import com.lamfire.jmongo.test.entity.User;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        ServerAddress addr = new ServerAddress("192.168.56.11",27017);

        List<MongoCredential> credentials  = new ArrayList<MongoCredential>();
        credentials.add(MongoCredential.createCredential("root","admin","123456".toCharArray()));
        MongoClient mongo =  new MongoClient(addr,credentials);
        for(String s : mongo.listDatabaseNames()){
            System.out.println(s);
        }

    }
}
