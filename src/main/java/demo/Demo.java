package demo;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * zookeeper是类似linux文件系统的目录树结构，挂载znode结点（默认1M的存储数据量）每个znode节点通过路径唯一标识（不能重复）
 *
 * znode=path+value+stat结构体
 * numChildren：子节点数量
 * dataLength：数据长度
 * czxid：创建节点的事务的id
 * ctime：创建的毫秒数（1970）
 * mzxid：修改事务id
 * 。。。。。。
 *
 * zoo.cfg 配置文件
 * tickTime: 心跳间隔时间
 * initLimit: 初始连接接受的最大的心跳数
 *
 *  基本命令
 *  ls [path]
 *  ls2 [path] ls+stat
 *  create [path]
 *  set [path][value]
 *  delete [path]
 *  rmr [path] 递归删除
 *  znode节点类型持久/临时
 *  重启服务之后会不会存在
 *
 *  create -e [path]    临时节点           临时
 *  create -s [path]    持久序列化节点      持久化加编号
 *  create    [path]    持久化节点         持久化
 *  create -e -s [path]  临时序列化        临时加编号
 *  默认不写为持久化节点
 *
 *  echo [命令] |nc [ip] [port]
 *  envi
 *  ruok
 *
 *  session 会话类似java中的session会话机制
 *  watch（观察） 通知机制 客户端注册监听他关心的目录节点，当目录节点发生变化（数据改变，被删除，子目录几点增加删除）时，zookeeper会通知客户端
 *        是什么：观察者机制，异步+回调机制
 *        事件理解： 一次性触发 只监控一次
 *                  发往客户端
 *                  为数据设置watch
 *                  时序性一致性
 *  zookeeper 集群
 *      位置 /conf/zoo.cfg
 *          dataDir=
 *          dataLOgDir=
 *          clientPort=
 *          server.1=223.34.9.144:2008:6008
 *          server.2=223.34.9.145:2008:6008
 *          在dataDir创建myid文件 1
 *          zkCli.sh -server [ip]:[port]
 *
 *      server.N=YYY:A:B
 *      N 服务器编号
 *      YYY 服务器ip
 *      A为LF端口，表示该服务器集群中leader交换信息的端口
 *      B为选举端口，表示选举新的leader时服务器之间相互通讯的端口
 *      一般来说集群中A端口是一样的，B端口是一样的
 *
 *      server.1=223.34.9.144:2008:6008 （leader）
 *      server.2=223.34.9.145:2008:6008
 *
 *      选举半数机制
 */
public class Demo {

    public final static String CONNECT_STRING = "192.168.44.131:2181";
    public final static int SESSION_TIMEOUT = 50*1000;

    public ZooKeeper startZK() throws IOException {
        return new ZooKeeper(CONNECT_STRING,SESSION_TIMEOUT,(event)->{});
    }

    public void stopZK(ZooKeeper zooKeeper) throws InterruptedException {
        if(zooKeeper != null) {
            zooKeeper.close();
        }
    }

    public void createZnode(ZooKeeper zooKeeper,String nodePath,String nodeValue) throws KeeperException, InterruptedException {
        zooKeeper.create(nodePath, nodeValue.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    public String get(ZooKeeper zooKeeper,String path) throws KeeperException, InterruptedException {
        return new String(zooKeeper.getData(path,false,new Stat()));
    }


    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        Demo demo = new Demo();
        ZooKeeper zooKeeper = null;
        try {
            zooKeeper = demo.startZK();
            String path = "/yang";

            if(null == zooKeeper.exists(path,false)) {
                demo.createZnode(zooKeeper,path,"hahahhah");
                String ha = demo.get(zooKeeper,path);
                System.out.println(ha+"++++++++++++++++++++++++++++++");
            }else{
                System.out.println("++++++++++++++exit++++++++++");
            }
        } finally {
            demo.stopZK(zooKeeper);
        }
    }
}
