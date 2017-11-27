package com.noah.cluster;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class Client_For_Read_Node_All {

	public static void main(String[] args) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				ZkClient zkClient = new ZkClient("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", 5000);
				subscribeListener(zkClient);
				while (true) {
					
				}
			}
			private void subscribeListener(ZkClient zkClient) {
				
				zkClient.subscribeChildChanges("/parent", new IZkChildListener() {
					@Override
					public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
						System.out.println("[127.0.0.1:2181,2182,2183] -- "+"Detect that under path \"" + parentPath + "\" there are childen " + currentChilds + " contents are");
						currentChilds.forEach(child->{
							System.out.println("[127.0.0.1:2181,2182,2183] -- "+child + " : "+ (String)zkClient.readData(parentPath+"/"+child));
						});
					}
				});
				
				IZkDataListener dataListener = new IZkDataListener() {
					@Override
					public void handleDataDeleted(String dataPath) throws Exception {
						System.out.println("[127.0.0.1:2181,2182,2183] -- "+dataPath + " is deleted");
					}
					@Override
					public void handleDataChange(String dataPath, Object data) throws Exception {
						System.out.println("[127.0.0.1:2181,2182,2183] -- "+dataPath + "'s new data is " + data);
					}
				};
				zkClient.subscribeDataChanges("/parent", dataListener);

				zkClient.subscribeStateChanges(new IZkStateListener() {
					@Override
					public void handleStateChanged(KeeperState state) throws Exception {
						System.out.println("[127.0.0.1:2181,2182,2183] -- "+"new status: " + state);
					}
					@Override
					public void handleNewSession() throws Exception {
						System.out.println("[127.0.0.1:2181,2182,2183] -- "+"There is new session is built");
					}
				});
			}
		}).start();

	}

}
