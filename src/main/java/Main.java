import org.apache.ibatis.session.SqlSession;
import sql.MysqlSesssonMyself;

public class Main {



    public static void main(String[] args) throws Exception {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//        long startTime = System.currentTimeMillis();   //获取开始时间

        System.out.println("Start……");

        MysqlSesssonMyself mysqlSqlSessson = new MysqlSesssonMyself();
        SqlSession sqlSession = mysqlSqlSessson.getSqlSession();
//        String urlHtmlIndex = "https://www.qidian.com/free/all";
////        String urlHtmlIndex = "https://www.qidian.com/all?orderId=&page=1&vip=0&style=1&pageSize=20&siteid=1&pubflag=0&hiddenField=0";
//        String urlOneNove = "https://book.qidian.com/info/1014978974";//25.27 w 字 100
        String bookName = "车间传";//书名
        String export_path = "/user/novel";//导出本地路径
//        int sleepTime = 43200; //时间最好是大于爬取完成的时间 10800s=3h 43200s=12h

//        获取某个网站的所有小说
//        timeAfterRunGetAllNovelHtml(urlHtmlIndex, 0, sleepTime, sqlSession);
//                "获取该书的所有章节"
//        timeAfterRunReadHtmlGetInfo(urlOneNove, 0, 128, sqlSession);
//导出某一本小说到本地，并且存储为 txt 格式
        export_txt(bookName, export_path,sqlSession);
        // 演示打印
//        long endTime = System.currentTimeMillis(); //获取结束时间 59s
//        System.out.println("Done 主线程耗时： " + (endTime - startTime) / 1000 + " s");
//        System.out.println("曲终人离！");


    }


    /**
     * 导出某一本小说到本地，并且存储为 txt 格式
     *
     * @param bookName    书名
     * @param export_path 导出本地路径
     */
    private static void export_txt(String bookName, String export_path, SqlSession sqlSession) {
        try {
            new ExportTxt(sqlSession).generateFile(bookName, export_path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param url BookIndex
     *            获取一本小说
     *            给定时长倒计时 重新启动
     *            GetAllNovelHtml
     */
    private static void timeAfterRunReadHtmlGetInfo(String url, int visitCount, int sleepTime, SqlSession sqlSession) {
        if (visitCount >= 1) {
            int time = sleepTime;
            System.out.println("主线程第 " + visitCount + " 次暂停" + sleepTime + " s");
            while (time > 0) {
                time--;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            visitCount++;
            System.out.println("主线程第 " + visitCount + " 次启动");
            try {
                new ReadHtmlGetInfo(sqlSession).getBookAllInfo(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            timeAfterRunReadHtmlGetInfo(url, visitCount, sleepTime, sqlSession);
        } else {
            visitCount++;
            System.out.println("主线程第 " + visitCount + " 次启动");
            try {
                new ReadHtmlGetInfo(sqlSession).getBookAllInfo(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            timeAfterRunReadHtmlGetInfo(url, visitCount, sleepTime, sqlSession);
        }

    }

    /**
     * @param url       one node
     * @param sleepTime 启动间隔时间
     *                  获取某个网站的所有小说
     *                  给定时长倒计时 重新启动
     */

    private static void timeAfterRunGetAllNovelHtml(String url, int visitCount, int sleepTime, SqlSession sqlSession) {
        if (visitCount >= 1) {
            int time = sleepTime;
            System.out.println("主线程第" + visitCount + " 次暂停" + sleepTime + " s");
            while (time > 0) {
                time--;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            visitCount++;
            System.out.println("主线程第" + visitCount + " 次启动");
            try {
                new GetAllNovelHtml(sqlSession).startCrawler(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            timeAfterRunGetAllNovelHtml(url, visitCount, sleepTime, sqlSession);
        } else {
            visitCount++;
            System.out.println("主线程第" + visitCount + " 次启动");
            try {
                new GetAllNovelHtml(sqlSession).startCrawler(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            timeAfterRunGetAllNovelHtml(url, visitCount, sleepTime, sqlSession);
        }

    }


}
