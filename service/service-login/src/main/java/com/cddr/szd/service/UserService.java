package com.cddr.szd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cddr.szd.model.User;
import com.cddr.szd.model.vo.RegularUser;
import com.cddr.szd.model.vo.UserVo;

public interface UserService extends IService<User> {
    void getCaptcha(String emailNum);

    void register(RegularUser registerUser, Integer code);


    void updatePassword(String oldPwd, String newPwd, String rePwd);

    void logout();

    String login(UserVo userVo, Integer code);

    User getUserInfo();

    void updateUser(RegularUser regularUser);
}
