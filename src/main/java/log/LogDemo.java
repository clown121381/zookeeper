package log;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * log4j
 *
 *
 *
 */
public class LogDemo {
    //第一行规范
    private static final Logger logger = Logger.getLogger(LogDemo.class);


    public static void main(String[] args) {

        //log4j:debug < info < warn < error
//        logger.debug("hello ");
//        logger.info("info");
//        logger.warn("warning");
//        logger.error("error");

        int age = 0;
        try{
            logger.debug("1111");
            age = 10/0;
            logger.debug("2222");
        }catch (Exception e){
            logger.error(e.getMessage(),e.getCause());
            //throw
        }
    }
}
