package demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * watch 之监听永久并且通知给控制台
 */
public class WatchMore {

    public final static String CONNECT_STRING = "192.168.44.131:2181";
    public final static int SESSION_TIMEOUT = 50*1000;
    private ZooKeeper zooKeeper;
    private String oldValue;
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

    //递归一次放一个监听
    public String get(String path) throws KeeperException, InterruptedException {
        return new String(zooKeeper.getData(path, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                try {
                    oldValue = get(path);
                    System.out.println(oldValue);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, new Stat()));
    }


    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        WatchMore watchMore = new WatchMore();
        watchMore.setZooKeeper(watchMore.startZK());
        String a = watchMore.get("/yang");
        System.out.println(a);
        TimeUnit.SECONDS.sleep(300);
    }
}
