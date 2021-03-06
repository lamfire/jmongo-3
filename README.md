jmongo
===========
This library provides clean and powerful mapping between Java POJOs and MongoDB DBObject.

step 1 setting jmongo.properties in resources and class path
-----------------------
    db0.connectionUri=mongodb://192.168.31.202:27017/admin
    db0.connectionsPerHost=64
    db0.threadsAllowedToBlockForConnectionMultiplier = 10
    db0.connectTimeout=60000
    db0.maxWaitTime=120000
    db0.socketTimeout=30000
    db0.socketKeepAlive=true
    db0.minConnectionsPerHost=8
    db0.maxConnectionsPerHost=64
    db0.serverSelectionTimeout=30000
    db0.maxConnectionIdleTime=0
    db0.maxConnectionLifeTime=0


step 2 define Entity
-----------------------

    import com.lamfire.jmongo.annotations.*;

    @Indexes(value={@Index(value = "nickname_age_index",fields = {@Field(value = "nickname"),@Field(value = "age")})})
    @Entity
    public class User {
        @Id
        private String id;

        @Indexed
        private String nickname;

        @Indexed
        private int age;

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
    }

step 3 Using DAO template
------------------
    User user = new User();
    user.setId("10001");
    user.setAge(18);

    DAO<User,String> dao = JMongo.getDAO("db0","UserDB","User", User.class);

    //save
    dao.save(user);
    System.out.println(dao.count());

    //get
    user = dao.get("10001");

    //query
    List<User> users = dao.createQuery().asList();

No use configure file sample
------------------
    //register mongodb host
    String zone = "db1";
    String connUri = "mongodb://root:123456@192.168.56.11:27017/admin";
    JMongoZoneOptions options = new JMongoZoneOptions(zone,connUri);
    JMongo.registerZoneOptions(options);

    //get dao instance
    DAO<User,String> dao = JMongo.getDAO("db1","test",User.class);
