package com.lamfire.jmongo.test;

import com.lamfire.code.PUID;
import com.lamfire.jmongo.dao.DAO;
import com.lamfire.jmongo.test.dao.UserDAO;
import com.lamfire.jmongo.test.entity.User;
import com.lamfire.utils.OPSMonitor;
import com.lamfire.utils.RandomUtils;
import com.lamfire.utils.Threads;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class TestDataWriter implements Runnable {
	private static AtomicInteger count = new AtomicInteger();
	private static OPSMonitor monitor = new OPSMonitor("write");

	public int getCount() {
		return count.get();
	}


	@Override
	public void run() {
		long begin = System.currentTimeMillis();
		while (true) {
            int i = count.getAndIncrement();
			write();
			monitor.done();
		}
	}
	
	private void write(){
		try{
			insertRandom();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void insertRandom(){
		DAO<User,String> dao = new UserDAO();
		User user = new User();
		user.setId(PUID.makeAsString());
		user.setNickname(PUID.makeAsString());
		user.setAge(10+RandomUtils.nextInt(100));
		dao.save(user);
	}

	public static void main(String[] args) {
		int threads = 10;

		if(args.length == 2){
			threads = Integer.parseInt(args[0]);
		}

		ThreadPoolExecutor executor = Threads.newFixedThreadPool(threads);
		for(int i=0;i<threads;i++){
			executor.submit(new TestDataWriter());
		}

		monitor.debug(true);
		monitor.startup();

	}
}
