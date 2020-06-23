import DB.DBChapters;
import DB.DBFiction;
import entity.Chapters;
import entity.Fiction;
import myselfBool.BooleanMune;
import org.apache.ibatis.session.SqlSession;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class ReadHtmlGetInfo {
    SqlSession sqlSession = null;

    public ReadHtmlGetInfo(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    //章节排序字段
    int chaptersSort = 0;
    //x小说id
    int fictionId = -1;
    //    url head 判断
    String urlHeadHttps = "https:";
    //    url head+url 匹配格式
    String regexTypeUrl = ".*";

    //网络处理请求次数 大于3 error 行待下一次爬取请求
    int net_re_coun = 0;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    long startTime;
    //获取某一本书并且存储到数据库
    public void getBookAllInfo(String url) throws ParseException {
        startTime = System.currentTimeMillis();   //获取开始时间
        Document doc = null;
        doc = readHtmlReturnDoc(url);
        Elements name = doc.select("[class=book-info] h1 em");
        Elements author = doc.select("[class=writer]");
        Elements type = doc.select("[class=tag] [class=red]");
        Elements states = doc.select("[class=tag] [class=blue]");
        Elements intro = doc.select("[class=book-intro] p");
        Elements lastestChapter = doc.select("[class=book-state] [class=detail] [class=blue]");
        Elements bookChapterUpdateTime = doc.select("[class=book-state] [class=detail] [class=time]");
        String urlFistChapters = getFistChaptersHref(url);
        String wordCount = getNovelWordCount(urlFistChapters);
        String statesDW = null;
        Date lastTime = df.parse(df.format(new Date()));
        if (!BooleanMune.elementsIsNull(states)) {
            statesDW = states.text().split(" ")[0];
        } else {
            statesDW = "未加载";
        }

        System.out.println("书名：" + name.text());
        System.out.println("作者：" + author.text());
        System.out.println("类型：" + type.text());
        System.out.println("状态：" + statesDW);
        if (BooleanMune.strIsNull(wordCount)) {
            System.out.println("字数：NUll");
        } else {
            wordCount = wordCount.split(" ")[1];
            System.out.println("字数：" + wordCount);
        }
        System.out.println("简介：" + intro.text());
        System.out.println("最近更新章节：" + lastestChapter.text());
        System.out.println("最近更新时间：" + bookChapterUpdateTime.text());

        Fiction fiction = new Fiction();
        fiction.setName(name.text());
        fiction.setAuthor(author.text());
        fiction.setState(statesDW);
        fiction.setType(type.text());
        fiction.setWordNumber(wordCount);
        fiction.setIntro(intro.text());
        fiction.setLastestChapter(lastestChapter.text());
        fiction.setLastestupdate(lastTime);
        fiction.setUrl(url);
        fiction.setCrawLastTime(lastTime);

        DBFiction dbFiction = new DBFiction(sqlSession);

        fictionId = dbFiction.selectFictionByName(name.text());
        if (fictionId == -1) {
            dbFiction.addFiction(fiction);
            //演示打印
            System.out.println("已存储《" + name.text() + " 》的主要信息");
            System.out.println("正在获取《 " + name.text() + " 》的章节信息……");
//            fictionId = dbFiction.selectFictionByName(name.text());
            fictionId = dbFiction.selectFictionByName(fiction.getName());
            getEverChapterContents(urlFistChapters, fictionId);

            //演示打印
            System.out.println("所有章节存储完成：" + name.text());
            long endTime = System.currentTimeMillis(); //获取结束时间 59s
            System.out.println("本次耗时： " + (endTime - startTime) + " ms");
            System.out.println("\n");
        } else {

            //演示打印
            System.out.println("已存更新《 " + name.text() + " 》的主要信息");
            System.out.println("正在获取《 " + name.text() + " 》的章节信息……\n");

            getEverChapterContents(urlFistChapters, fictionId);

            //演示打印
            System.out.println("所有章节更新完成：" + name.text());
            long endTime = System.currentTimeMillis(); //获取结束时间 59s
            System.out.println("本次耗时： " + (endTime - startTime) + " ms");
            System.out.println("\n");
        }
    }

    //获取下一章Href
    public void getNextChapterHref(String url, int ficId) {
        if (!BooleanMune.strIsNull(url)) {
            if (!Pattern.matches(urlHeadHttps + regexTypeUrl, url)) {
                url = urlHeadHttps + url;
            } else {
            }
            Document doc = null;
            doc = readHtmlReturnDoc(url);
            Elements chapterNextHref = doc.select("[id=j_chapterNext]");
            if (!BooleanMune.elementsIsNull(chapterNextHref)) {
                getEverChapterContents(url, ficId);
            } else {

                //演示打印

//                long endTime = System.currentTimeMillis(); //获取结束时间 59s
//                System.out.println("本次耗时： " + (endTime - startTime) + " ms");
                System.out.println(ficId + " 完成爬取时间：" + df.format(new Date()));
            }
        } else {
            System.out.println("链接url为空，停止对此书的获取！ " + ficId);
        }
    }


    /**
     * 获取章节文本内容 主入口
     * url 第一章的href
     */
    public void getEverChapterContents(String string_fist_href, int ficId) {
        String url = string_fist_href;
        while (!(BooleanMune.strIsNull(url))) {
            if (!Pattern.matches(urlHeadHttps + regexTypeUrl, url)) {
                url = urlHeadHttps + url;
            } else {
            }
            Document doc = null;
            doc = readHtmlReturnDoc(url);
            Elements chapterTitle = doc.select("[class=j_chapterName]");
            Elements contents = doc.select("[class=read-content j_readContent] p");
            Elements chapterNextHref = doc.select("[id=j_chapterNext]");
            if (!(BooleanMune.elementsIsNull(chapterTitle) && BooleanMune.elementsIsNull(contents))) {
                Chapters chapters = new Chapters();
                chapters.setTitle(chapterTitle.text());
                chapters.setContent(contents.text());
                chapters.setHtml(url);
                Date createDate = null;
                try {
                    createDate = df.parse(df.format(new Date()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                chapters.setCreateDate(createDate);
                chapters.setSort(chaptersSort);
                chapters.setFictionId(ficId);
                chaptersSort += 1;
                DBChapters dbChapters = new DBChapters(sqlSession);
//          判断连接是否重复
                if (dbChapters.selectChaptersHtml(url)) {
                    dbChapters.addChatpers(chapters);
                    // 演示打印
//                    System.out.println("已存储: " + chapters.getTitle());
                } else {
                    // 演示打印
//                    System.out.println("已更新: " + chapters.getTitle());
                }
                url = null;
                url = chapterNextHref.attr("href");

            } else {
                //演示打印
//                long endTime = System.currentTimeMillis(); //获取结束时间 59s
//                System.out.println("本次耗时： " + (endTime - startTime) + " ms");
                System.out.println(ficId + " 完成爬取时间：" + df.format(new Date()));
                break;
            }
        }
    }

    //获取下一个有字数href
    public String getNovelWordCountHref(String url) {
        if (!BooleanMune.strIsNull(url)) {
            if (!Pattern.matches(urlHeadHttps + regexTypeUrl, url)) {
                url = urlHeadHttps + url;
            } else {
            }
            Document doc = null;
            doc = readHtmlReturnDoc(url);
            Elements chapterNextHref = doc.select("[id=j_chapterNext]");

            if (!BooleanMune.elementsIsNull(chapterNextHref)) {
                return getNovelWordCount(chapterNextHref.attr("href"));
            } else {
                return getNovelWordCount(chapterNextHref.attr("href"));
            }
        } else {
            return null;
        }
    }

    //获取字数的入口
    public String getNovelWordCount(String url) {
        if (!BooleanMune.strIsNull(url)) {
            if (!Pattern.matches(urlHeadHttps + regexTypeUrl, url)) {
                url = urlHeadHttps + url;
            } else {
            }
            Document doc = null;
            doc = readHtmlReturnDoc(url);
            Elements bookWordCount = doc.select("[class=book-cover-wrap] [class=info-list cf] ul li p");
            if (!BooleanMune.elementsIsNull(bookWordCount)) {
                return bookWordCount.text();
            } else {
                return getNovelWordCountHref(url);
            }
        } else {
            return null;
        }
    }

    //获取首章href
    public String getFistChaptersHref(String url) {
        if (!BooleanMune.strIsNull(url)) {
            if (!Pattern.matches(urlHeadHttps + regexTypeUrl, url)) {
                url = urlHeadHttps + url;
            } else {
            }
            Document doc = null;
            doc = readHtmlReturnDoc(url);
            Elements elUrlFist = doc.select("[id=readBtn]");
            if (!BooleanMune.elementsIsNull(elUrlFist)) {
                return elUrlFist.attr("href");
            } else {
                System.out.println("获取首章链接失败！");
                return null;
            }
        } else {
            System.out.println("链接无效：" + url);
            return null;
        }
    }

    //读取html并且返回 Document 对象

    public Document readHtmlReturnDoc(String url) {
        if (!Pattern.matches(urlHeadHttps + regexTypeUrl, url)) {
            url = urlHeadHttps + url;
        } else {
        }
        Document document = null;
        //如果网络请求时发生异常，则再次发送一次请求
        try {
            document = Jsoup.connect(url).get();
        } catch (Exception e) {
            try {
                //如果网络请求时发生异常，则3s后再次发送一次请求
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            net_re_coun++;
            if (net_re_coun <= 3) {
                document = readHtmlReturnDoc(url);
            } else {
                System.out.println("Error：网络请求超时！");
                return document = null;
            }

            e.printStackTrace();
        }
        net_re_coun = 0;
        return document;
    }


}
