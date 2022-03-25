package in.kbfg.ucb.keyword;

import in.kbfg.ucb.mybatis.entity.RecommendKeywordEntity;
import in.kbfg.ucb.mybatis.entity.UserKeywordEntity;
import in.kbfg.ucb.mybatis.mapper.KeywordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class KeywordService {

    @Autowired
    KeywordMapper keywordMapper;

    public List<String> getRecommendKeywordList() {
        List<RecommendKeywordEntity> keywordList = keywordMapper.getRecommendKeyword();
        List<String> returnValue = new ArrayList<>();
        for (RecommendKeywordEntity keyword : keywordList) {
            returnValue.add(keyword.getKeyword());
        }
        return returnValue;
    }

    public List<String> getUserKeywordList(int userIdx) {
        List<UserKeywordEntity> keywordList = keywordMapper.getUserKeyword(userIdx);
        List<String> returnValue = new ArrayList<>();
        for (UserKeywordEntity keyword : keywordList) {
            returnValue.add(keyword.getKeyword());
        }
        return returnValue;
    }

    public int updateKeywordList(int userIdx, List<String> keywordList) {
        keywordMapper.deleteUserKeyword(userIdx);

        int insertCount = 0;
        for (String keyword : keywordList) {
            insertCount += keywordMapper.insertUserKeyword(userIdx, keyword);
        }
        return insertCount;
    }
}
