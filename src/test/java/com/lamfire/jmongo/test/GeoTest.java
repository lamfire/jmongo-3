package com.lamfire.jmongo.test;

import com.lamfire.jmongo.JMongo;
import com.lamfire.jmongo.dao.DAO;
import com.lamfire.jmongo.test.entity.GeoEntity;
import com.lamfire.json.JSON;

import java.util.List;

/**
 * Created by linfan on 2017/8/9.
 */
public class GeoTest {
    public static void main(String[] args) {
        DAO<GeoEntity,String> dao = JMongo.getDAO("db0","test","GeoEntity",GeoEntity.class);
        for(int i=0;i<100;i++){
            GeoEntity e = new GeoEntity();
            e.setId(""+i);
            e.setName("GEO-" + i);
            double[] los = new double[2];
            los[0] = i;
            los[1] = i;
            e.setLocation(los);

            dao.save(e);
        }

        List<GeoEntity> list = dao.createQuery().field("location").near(10,10).limit(5).asList();
        for(GeoEntity e : list){
            System.out.println(JSON.toJSONString(e));
        }
    }
}
