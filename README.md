# ZooKeeper

cluster config

server 1
tickTime=2000
initLimit=10
syncLimit=5
dataDir=D:\\Program\\zookeepers\\zookeeper-1\\data
dataLogDir=D:\\Program\\zookeepers\\zookeeper-1\\log
clientPort=2181
server.1=127.0.0.1:2888:3888
server.2=127.0.0.1:2889:3889
server.3=127.0.0.1:2890:3890


server 2
tickTime=2000
initLimit=10
syncLimit=5
dataDir=D:\\Program\\zookeepers\\zookeeper-2\\data
dataLogDir=D:\\Program\\zookeepers\\zookeeper-2\\log
clientPort=2182
server.1=127.0.0.1:2888:3888
server.2=127.0.0.1:2889:3889
server.3=127.0.0.1:2890:3890


server 3
tickTime=2000
initLimit=10
syncLimit=5
dataDir=D:\\Program\\zookeepers\\zookeeper-3\\data
dataLogDir=D:\\Program\\zookeepers\\zookeeper-3\\log
clientPort=2183
server.1=127.0.0.1:2888:3888
server.2=127.0.0.1:2889:3889
server.3=127.0.0.1:2890:3890


our 3 zookeeper instances are running in same machine, the clientPort need to set different

the cluster config key is the last 3 rows
server.{serverID}={serverIP}:{sycnPort}:{electionPort}
serverID is the content in myid file(a file named "myid" which is stored in dataDir)
sycnPort is used to exchange the sync messge among all nodes 
i.e. if node1 has sth updated, it need to tell node2 and node3 about this change via sycnPort
electionPort is used to exchange the election message among all nodes 
i.e. if there are some nodes down or recover in the cluster, the election wil start again to make a new leader via electionPort
