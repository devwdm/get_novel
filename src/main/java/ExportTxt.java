import DB.DBChapters;
import DB.DBFiction;
import entity.Chapters;
import entity.Fiction;
import org.apache.ibatis.session.SqlSession;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportTxt {
    SqlSession sqlSession = null;

    public ExportTxt(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    /**
     * 生成txt文件
     *
     * @param name 整型或字符串
     * @param url  存放目录地址
     * @throws Exception
     */
    public void generateFile(String name, String url) throws Exception {
        DBFiction dbFiction = new DBFiction(sqlSession);
        DBChapters dbChapters = new DBChapters(sqlSession);


//        FictionDatabaseOperation fictionDatabaseOperation = new FictionDatabaseOperation();
//        FictionDatabaseOperation fictionDatabaseOperation = new FictionDatabaseOperation();
//        FictionCatalogDatabaseOperation fictionCatalogDatabaseOperation = new FictionCatalogDatabaseOperation();
        Fiction fiction;
//        Chapters chapters;
        List<Chapters> chaptersList;
        String variableType = name.getClass().getName();
//        if (variableType.equals("java.lang.Integer")) {
////            fiction = fictionDatabaseOperation.((Integer) name);
//        } else if (variableType.equals("java.lang.String")) {
//            fiction = fictionDatabaseOperation.selectFictionByName((String) name);
//        } else {
//            System.out.println("参数类型错误，只能为整型或字符串！！");
//            return;
//        }
        int id = dbFiction.selectFictionByName((String) name);
        fiction = dbFiction.selectFictionById(id);
        chaptersList = dbChapters.selectAllChaptersById(id);
        System.out.println(chaptersList.size());

        String folderUrl = url + name + "/";
        File file = new File(folderUrl);
        if (!file.exists()) {//如果文件夹不存在
            file.mkdir();//创建文件夹
        }

        String fileUrl, content;

        fileUrl = folderUrl + "00小说简介00.txt";
        file = fileNew(fileUrl);

        content = "书名：" + fiction.getName() + "\r\n作者：" + fiction.getAuthor() + "\r\n简介："
                + fiction.getIntro().replaceAll(" 　　", "\r\n     　　     ")
                + "\r\n总字数：" + fiction.getWordNumber() + "\r\n进度：" + fiction.getState()
                + "\r\n类型：" + fiction.getType() + "\r\n更新时间：" + fiction.getLastestupdate()
                + "\r\n最新章节：" + fiction.getLastestChapter();
        fileWriter(file, content);

        for (Chapters chapters : chaptersList) {
            fileUrl = folderUrl + chapters.getTitle() + ".txt";
            file = fileNew(fileUrl);
//            content = chapters.getTitle() + "\r\n字数：" + chapters.getNumber() + "\r\n" + fictionCatalogBean.getContent().replaceAll(" 　　", "\r\n    ");
            content = chapters.getTitle() + "\r\n" + chapters.getContent().replaceAll(" 　　", "\r\n    ");
            fileWriter(file, content);
        }

    }

    private File fileNew(String fileUrl) throws IOException {
        File file = null;
        file = new File(fileUrl);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    private void fileWriter(File file, String content) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(content);
        bufferedWriter.close();
    }
}
