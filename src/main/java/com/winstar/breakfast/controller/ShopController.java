package com.winstar.breakfast.controller;

import com.winstar.breakfast.entity.Shop;
import com.winstar.breakfast.repository.ShopRepository;
import com.winstar.communalCoupon.util.MyPage;
import com.winstar.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zl on 2020/1/6
 */
@RestController
@RequestMapping("/api/v1/noAuth/shop")
@AllArgsConstructor
@Slf4j
public class ShopController {

    private ShopRepository shopRepository;

    /**
     * 查询商品信息
     */

    @GetMapping(value = "/getAllShopInfo")
    public Result getAllShopInfo(@RequestParam(defaultValue = "0") Integer nextPage, @RequestParam(defaultValue = "10") Integer pageSize) {
        List<Shop> shopList = shopRepository.findByStatus(1);
        Page<Shop> page = new MyPage<>(nextPage, pageSize, shopList, CollectionUtils.isEmpty(shopList) ? 0 : shopList.size());
        return Result.success(page);
    }


}
