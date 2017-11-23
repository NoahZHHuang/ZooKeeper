package com.noah.zkclient_api;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class ZooKeeperBasic {
	
	//In this demo, I set Thread.sleep(1000L) between every two steps, in order to see how the subscribed listeners work.
	//Because the listener is not in real time, it need some time to react. the Detail can refer to  zkClient.subscribeDataChanges()

	public static void main(String[] args) throws InterruptedException {

		ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);

		// Even the node is not created yet, we can register a watcher to it in advance
		subscribeListener(zkClient);

		// we can write data after create the node
		zkClient.createPersistent("/parent");
		Thread.sleep(1000L);
		zkClient.writeData("/parent", "I am parent");
		Thread.sleep(1000L);
		zkClient.writeData("/parent", "I am new parent");
		// we can also write data when creating the node
		zkClient.createPersistent("/parent/child_one", "I am child one");
		Thread.sleep(1000L);
		zkClient.createPersistent("/parent/child_two", "I am child two");
		Thread.sleep(1000L);
		//List<String> children = zkClient.getChildren("/parent");
		System.out.println("node \"/parent\" exist :" + zkClient.exists("/parent"));
		System.out.println("node \"/parent_xxxx\" exist :" + zkClient.exists("/parent_xxxx"));

		cleanUp(zkClient);
		
		Thread.sleep(1000L);
		unSubscrisbeListener(zkClient);

	}

	private static void subscribeListener(ZkClient zkClient) {
		
		zkClient.subscribeChildChanges("/parent", new IZkChildListener() {
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				System.out.println("Detect that under path \"" + parentPath + "\" there are childen " + currentChilds + " contents are");
				currentChilds.forEach(child->{
					System.out.println(child + " : "+ (String)zkClient.readData(parentPath+"/"+child));
				});
			}
		});
		
		IZkDataListener dataListener = new IZkDataListener() {
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println(dataPath + " is deleted");
			}
			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				System.out.println(dataPath + "'s new data is " + data);
			}
		};
		zkClient.subscribeDataChanges("/parent", dataListener);
		
		//please DONT share the listener among different nodes like :
		//zkClient.subscribeDataChanges("/parent/child_one", dataListener); 
		//zkClient.subscribeDataChanges("/parent/child_two", dataListener); 
		//because in order to make the listener thread safe, it is synchronized
		//which means it will have time delay if it is shared. 
		//details can refer to the implementation of zkClient.subscribeDataChanges()

		zkClient.subscribeStateChanges(new IZkStateListener() {
			@Override
			public void handleStateChanged(KeeperState state) throws Exception {
				System.out.println("new status: " + state);

			}
			@Override
			public void handleNewSession() throws Exception {
				System.out.println("There is new session is built");
			}
		});
		
	}
	
	private static void unSubscrisbeListener(ZkClient zkClient){
		zkClient.unsubscribeAll();
		//we can also use the method like zkClient.unsubscribeChildChanges(path, childListener)
	}

	private static void cleanUp(ZkClient zkClient) throws InterruptedException{
		zkClient.delete("/parent/child_one");
		Thread.sleep(1000L);
		zkClient.delete("/parent/child_two");
		Thread.sleep(1000L);
		zkClient.delete("/parent");
	}

}
