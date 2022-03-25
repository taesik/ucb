package in.kbfg.ucb.mybatis.mapper;

import in.kbfg.ucb.mybatis.entity.RecommendKeywordEntity;
import in.kbfg.ucb.mybatis.entity.UserKeywordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface KeywordMapper {

    List<RecommendKeywordEntity> getRecommendKeyword();

    List<UserKeywordEntity> getUserKeyword(int userIdx);

    int deleteUserKeyword(int userIdx);

    int insertUserKeyword(@Param("userIdx") int userIdx, @Param("keyword") String keyword);

}
