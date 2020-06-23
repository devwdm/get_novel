package DB;

import entity.Fiction;
import org.apache.ibatis.session.SqlSession;

public class DBFiction {
    SqlSession sqlSession = null;

    public DBFiction(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public int addFiction(Fiction fiction) {
        int num = sqlSession.insert("xml.Mapper.FictionsMapper.addFiction", fiction);
        sqlSession.commit();
        return num;
    }

    public Integer selectMaxId() {
        Fiction fictions = sqlSession.selectOne("xml.Mapper.FictionsMapper.selectMaxId");
        return fictions != null ? fictions.getId() : 0;
    }

    public Integer selectFictionByName(String bookName) {
        Fiction fictions = sqlSession.selectOne("xml.Mapper.FictionsMapper.selectFictionByName", bookName);
        return fictions != null ? fictions.getId() : -1;

    }

    /**
     * 根据id查询小说信息
     *
     * @param id
     * @return FictionBean
     * @throws Exception
     */
    public Fiction selectFictionById(int id) {
        Fiction fiction = null;
        fiction = sqlSession.selectOne("xml.Mapper.FictionsMapper.selectFictionById", id);
        return fiction;
    }

    public void deleteAllFictions() {
        sqlSession.insert("xml.Mapper.FictionsMapper.deleteAllFictions");
        sqlSession.commit();
    }

    public Fiction selectOneFictions() {
        return sqlSession.selectOne("xml.Mapper.FictionsMapper.selectOneFictions");
    }

}
