package com.noah.cluster;

import org.I0Itec.zkclient.ZkClient;

public class Client_For_Write_Node_All {

	public static void main(String[] args) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean flag = true;
				ZkClient zkClient = new ZkClient("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", 5000);
				//for multiple "ip:port"s in one ZkClient, any one of is it available, the connection can be successful
				//like "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183" is good
				//     "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2184" is good
				//     "127.0.0.1:2184,127.0.0.1:2185,127.0.0.1:2186" is bad
				try {
					clearData(zkClient);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				while (true) {
					try {
						if (flag) {
							writeData(zkClient);
						} else {
							clearData(zkClient);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						flag = !flag;
					}
				}
			}

			private void writeData(ZkClient zkClient) throws InterruptedException {
				zkClient.createPersistent("/parent", "I am parent");
				Thread.sleep(2000L);
				zkClient.createPersistent("/parent/child_one", "I am child one");
				Thread.sleep(2000L);
				zkClient.createPersistent("/parent/child_two", "I am child two");
				Thread.sleep(2000L);
			}
			
			private void clearData(ZkClient zkClient) throws InterruptedException {
				if(zkClient.exists("/parent/child_one")){
					zkClient.delete("/parent/child_one");
				}				
				Thread.sleep(2000L);
				if(zkClient.exists("/parent/child_two")){
					zkClient.delete("/parent/child_two");
				}
				Thread.sleep(2000L);
				if(zkClient.exists("/parent")){
					zkClient.delete("/parent");
				}
				Thread.sleep(2000L);
			}
		}).start();

	}

}
