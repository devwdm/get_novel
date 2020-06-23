import org.apache.ibatis.session.SqlSession;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class GetAllNovelHtml {

    SqlSession sqlSession = null;

    public GetAllNovelHtml(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    private String[] currentCrawlerUrls = new String[20];//每本小说url缓存器

    private int counter = 0;//目录地址计数器
    private int threadCounter = 5;//线程计数器，最大为5

    //返回目录标签
    private synchronized String counter() {
        counter++;
        return "fiction_" + counter;
    }

    //线程数减1
    private synchronized void threadCounter() {
        threadCounter++;
    }

    /**
     * 总控制爬虫，爬取每一页的小说url，分发给小爬虫
     *
     * @param url 传入url
     * @throws Exception
     */
    public void startCrawler(String url) throws Exception {
        Document document;
        Elements linkUrl, nextUrl;
        while (true) {
            document = getDocument(url);
            //判断缓存器里的地址是否使用完，是否全部为空
            boolean isNull = true;
            for (int i = 0; i < 20; i++) {
                if (currentCrawlerUrls[i] != null) {
                    isNull = false;
                    break;
                }
            }
            //如果缓存器里没有地址，则再次捕获分发地址和更新页面地址
            if (isNull) {
                //捕获分发地址存入缓存器中
                for (int i = 1; i <= 20; i++) {
                    linkUrl = document.select("[class=all-img-list cf] [data-rid=" + i + "] [class=book-mid-info] h4 a");
                    //如果缓存器不满，就说明后续没小说了
                    if (linkUrl == null || linkUrl.size() == 0) {
                        System.out.println("最后一页小说，即将爬完了！");
                        break;
                    }
                    //把每本小说地址存入缓存器
                    currentCrawlerUrls[i - 1] = "https:" + linkUrl.attr("href");
                }

                //更新页面地址
                nextUrl = document.select("[class=lbf-pagination-next ]");
                //如果下一页页面地址为空，则已爬完所有小说！
                if (nextUrl == null || nextUrl.size() == 0) {
                    System.out.println("已爬完所有小说！！！！");
                    return;
                } else {
                    url = "https:" + nextUrl.attr("href");
                }

            }


            for (int i = 0; i < 20; i++) {

                //遍历缓存器里的地址，如果地址不为空
                if (currentCrawlerUrls[i] != null) {

                    //判断当前剩余线程资源数是否为0，如果不为0
                    if (0 < threadCounter) {
                        //剩余可用线程数减1
                        threadCounter--;
                        //取出缓存器中的地址
                        String threadUrl = currentCrawlerUrls[i];
                        //取出后，把缓存器的地址置为null
                        currentCrawlerUrls[i] = null;
                        //开启新线程，分发小说url给新线程
                        new Thread(new ThreadsCrawler(threadUrl) {
                            @Override
                            public void run() {
                                try {
//                                    //调用小爬虫，爬取分发的小说url
//                             smallWebCrawler(this.threadUrl);
                                    //myself
                                    new ReadHtmlGetInfo(sqlSession).getBookAllInfo(threadUrl);
                                    //爬取完成后剩余可用线程数加1
                                    threadCounter();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    threadCounter();
                                }
                            }
                        }).start();
                    } else {
                        break;
                    }
                }
            }
            //休眠5秒，每过5秒检查有没有小爬虫完成工作
            Thread.sleep(5000);
        }
    }
//
//    /**
//     * 小爬虫：爬取整本小说的内容
//     *
//     * @param url 传入url
//     * @throws Exception
//     */
//    private void smallWebCrawler(String url) throws Exception {
//        Document document = getDocument(url);
//
//        //书名
//        Elements name = document.select("[class=book-info ] h1 em");
//
//        //作者
//        Elements writer = document.select("[class=book-info ] h1 a");
//
//        //简介
//        Elements intro = document.select("[class=book-intro]");
//
//        //进度
//        Elements schedule = document.select("[class=tag] [class=blue]");
//
//        //类型
//        Elements type = document.select("[class=tag] a");
//
//        //更新时间
//        Elements update_time = document.select("[class=time]");
//
//        //最新章节
//        Elements latest_chapters = document.select("[class=update] [class=cf] a");
//
//        //小说链接
//        Elements linkUrl = document.select("[class=red-btn J-getJumpUrl ]");
//        url = "https:" + linkUrl.attr("href");
//        document = getDocument(url);
//
//        //总字数
//        Elements number = document.select("[class=info-list cf] ul li p em");
//
//        //目录
//        String catalog = counter();
//
//        fictionBean = new FictionBean(name.text(), writer.text(), intro.text(), number.text() + "万", schedule.text().split(" ")[0], catalog, type.text(), update_time.text(), latest_chapters.text());
//
//        fictionDatabaseOperation.insertFiction(fictionBean);//新增小说信息
//        fictionDatabaseOperation.updateFictionCatalog(catalog);//创建小说目录表
//
//        while (true) {
//            //章节
//            Elements chapter = document.select("[class=j_chapterName]");
//
//            //章节字数
//            Elements numberChapter = document.select("[class=j_chapterWordCut]");
//
//            //内容
//            Elements content = document.select("[class=read-content j_readContent]");
//
//            fictionCatalogBean = new FictionCatalogBean(catalog, chapter.text(), numberChapter.text(), content.text());
//
//            fictionCatalogDatabaseOperation.insertFictionCatalog(fictionCatalogBean);
//
//            //下一章url
//            Elements next = document.select("[id=j_chapterNext]");
//            if (next == null || next.size() == 0) {
//                return;
//            } else {
//                url = "https:" + next.attr("href");
//                document = getDocument(url);
//            }
//
//        }
//    }

    /**
     * 获得document对象
     *
     * @param url 传入URL
     * @return
     * @throws Exception
     */
    private Document getDocument(String url) {
        Document document = null;
        //如果网络请求时发生异常，则再次发送一次请求
        try {
            document = Jsoup.connect(url).get();
        } catch (Exception e) {
            e.printStackTrace();
            document = getDocument(url);
        }

//        演示打印
//        System.out.println(document.title());

        return document;
    }


}
