package DB;


import entity.Chapters;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

import static myselfBool.BooleanMune.listIsNull;
import static myselfBool.BooleanMune.strIsNull;

public class DBChapters {
    public SqlSession sqlSession = null;

    public DBChapters(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public Integer selectMaxId() {
        Chapters chatpers = sqlSession.selectOne("xml.Mapper.ChatpersMapper.selectMaxId");
        return chatpers != null ? chatpers.getId() : 0;
    }

    public boolean selectChaptersHtml(String html) {
        String lsHtml=null;
        lsHtml = sqlSession.selectOne("xml.Mapper.ChatpersMapper.selectChaptersHtml", html);
        if (strIsNull(lsHtml)) {
            return true;
        } else {
            return false;
        }
    }

    public List<Chapters> selectAllChaptersById(int fictionId) {
        List<Chapters> lists = sqlSession.selectList("xml.Mapper.ChatpersMapper.selectAllChaptersById", fictionId);
        return lists;
    }

    public int addChatpers(Chapters chatpers) {
        int a = sqlSession.insert("xml.Mapper.ChatpersMapper.addChatpers", chatpers);
        sqlSession.commit();
        return a;
    }

    public void deleteAllChatpers() {
        sqlSession.insert("xml.Mapper.ChatpersMapper.deleteAllChatpers");
        sqlSession.commit();
    }

    public List<Chapters> selectContentById(int id) {
        return sqlSession.selectList("xml.Mapper.ChatpersMapperselectContentById", id);
    }

}
