package com.cddr.szd;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author ZY
 */

@Component
@FeignClient(value = "service-user")
public interface UserFeignClient {

    /**
     * 获取默认地址
     */
    @GetMapping("/api/user/inner/addressInfo/{userId}")
    public User getAddressInfo(@PathVariable Long userId);
}