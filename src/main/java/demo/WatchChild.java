package demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * watch 之节点变化观察
 */
public class WatchChild {

    public final static String CONNECT_STRING = "192.168.44.131:2181";
    public final static int SESSION_TIMEOUT = 50*1000;
    public final static String PATH= "/yang";
    private ZooKeeper zooKeeper;
    public ZooKeeper startZK() throws IOException {
        return new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                List<String> list = showChild(PATH);
                if(watchedEvent.getType() == Event.EventType.NodeChildrenChanged && watchedEvent.getPath().equals(PATH)){
                    System.out.println("发生了变化");
                }
            }
        });
    }

    private List<String> showChild(String path){

        List<String> list = null;
        try {
            //父节点的watch也会向子节点传递
           list =  zooKeeper.getChildren(path,true,new Stat());
            System.out.println(list);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
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
        WatchChild watchChild = new WatchChild();
        watchChild.setZooKeeper(watchChild.startZK());
        String a = watchChild.get(PATH);
        System.out.println(a);
        TimeUnit.SECONDS.sleep(300);
    }
}
