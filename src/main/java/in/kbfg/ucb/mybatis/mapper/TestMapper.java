package in.kbfg.ucb.mybatis.mapper;

import in.kbfg.ucb.mybatis.entity.TestEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TestMapper {

    String helloName(Map<String, Object> params);

    List<TestEntity> testModel(TestEntity model);
}
