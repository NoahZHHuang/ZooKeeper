package com.noah.protosomatic_api;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class ZooKeeperBasic {
	
	public static void main(String [] args) throws IOException, KeeperException, InterruptedException{
		
		ZooKeeper zooKeeper = getZooKeeper();
		cleanUp(zooKeeper);
		reset(zooKeeper);
		cleanUp(zooKeeper);
		zooKeeper.close();
		
	}
	
	private static void reset(ZooKeeper zooKeeper) throws IOException, KeeperException, InterruptedException{
		zooKeeper.create("/parent", "I am parent".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		//If a node want to have child node, then parent node has to be PERSISTENT
		zooKeeper.create("/parent/child_one", "I am child one".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zooKeeper.create("/parent/child_two", "I am child two".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println("node \"/parent\" status is: " + zooKeeper.exists("/parent", true));
		System.out.println("node \"/parent\" data is: " + new String(zooKeeper.getData("/parent", true, null)));
		System.out.println("node \"/parent\" has childen: " + zooKeeper.getChildren("/parent", true));
		zooKeeper.setData("/parent", "I am the new parent".getBytes(), -1); //version -1 means ignore the version, set the new data for the node whatever version
		System.out.println("node \"/parent\" new data is: " + new String(zooKeeper.getData("/parent", true, null)));
		
	}

	
	private static void cleanUp(ZooKeeper zooKeeper) throws KeeperException, InterruptedException, IOException  {
		//delete node should start from the child, or it will say "Directory not empty for ${Node Name}"
		if(zooKeeper.exists("/parent/child_one", true) != null){
			zooKeeper.delete("/parent/child_one", -1); //version -1 means ignore the version, delete it whatever version
		}
		
		if(zooKeeper.exists("/parent/child_two", true) != null){
			zooKeeper.delete("/parent/child_two", -1); 
		}
		
		if(zooKeeper.exists("/parent", true) != null){
			zooKeeper.delete("/parent", -1); 
		}
	}
	

	private static ZooKeeper getZooKeeper() throws IOException {
		ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 5000, new Watcher(){
			public void process(WatchedEvent event) {
				System.out.println("Trigger "+ event.getType()+" Event");
			}
		});
		return zooKeeper;
	}
	
}
