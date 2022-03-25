package in.kbfg.ucb.login;

import in.kbfg.ucb.mybatis.entity.UserEntity;
import in.kbfg.ucb.mybatis.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserMapper userMapper;

    public int mergeUserInfo(UserEntity user) {
        return userMapper.mergeUserInfo(user);
    }

    public UserEntity getUserInfo(String id) {
        return userMapper.getUserInfo(id);
    }
}
