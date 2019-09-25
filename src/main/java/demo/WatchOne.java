package demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * watch 之监听一次并且通知给控制台
 */
public class WatchOne {

    public final static String CONNECT_STRING = "192.168.44.131:2181";
    public final static int SESSION_TIMEOUT = 50*1000;
    private ZooKeeper zooKeeper;
    public ZooKeeper startZK() throws IOException {
        return new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });
    }

    public void stopZK() throws InterruptedException {
        if(zooKeeper != null) {
            zooKeeper.close();
        }
    }

    public void createZnode(String nodePath,String nodeValue) throws KeeperException, InterruptedException {
        zooKeeper.create(nodePath, nodeValue.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public String get(String path) throws KeeperException, InterruptedException {
        return new String(zooKeeper.getData(path, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                try {
                    System.out.println(new String(zooKeeper.getData(path, false,new Stat())));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, new Stat()));
    }


    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        WatchOne watchOne = new WatchOne();
        watchOne.setZooKeeper(watchOne.startZK());
        String a = watchOne.get("/yang");
        System.out.println(a);
        TimeUnit.SECONDS.sleep(300);
    }
}
