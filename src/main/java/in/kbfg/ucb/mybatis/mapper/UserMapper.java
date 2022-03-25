package in.kbfg.ucb.mybatis.mapper;

import in.kbfg.ucb.mybatis.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    int mergeUserInfo(UserEntity user);

    UserEntity getUserInfo(String id);
}
