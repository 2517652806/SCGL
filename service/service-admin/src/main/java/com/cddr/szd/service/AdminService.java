package com.cddr.szd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cddr.szd.model.RegularUser;
import com.cddr.szd.model.SearchUser;
import com.cddr.szd.model.User;
import com.cddr.szd.model.UserPermission;

public interface AdminService {
    void addUser(RegularUser regularUser);

    void updateUser(RegularUser regularUser);

    void deleteUser(Integer id);

    IPage<User> findUser(SearchUser searchUser);

    void updatePermission(UserPermission userPermission);

    void resetPassword(Integer id);
}
