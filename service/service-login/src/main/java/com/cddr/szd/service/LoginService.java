package com.cddr.szd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cddr.szd.loginModel.RegularUser;
import com.cddr.szd.model.User;
import com.cddr.szd.model.vo.UserVo;

public interface LoginService extends IService<User> {
    void getCaptcha(String emailNum);

    void register(RegularUser registerUser);

    String login(UserVo userVo);

    void updatePassword(String oldPwd, String newPwd, String rePwd);
}
