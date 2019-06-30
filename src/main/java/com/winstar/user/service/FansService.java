package com.winstar.user.service;

import com.winstar.redis.RedisTools;
import com.winstar.user.repository.FansRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author UU
 * @Classname FansUtil
 * @Description TODO
 * @Date 2019/6/19 10:38
 */
@Slf4j
//@SuppressWarnings("all")
@Service
public class FansService {
    @Autowired
    public FansService(FansRepository fansRepository, RedisTools redisTools) {
        this.fansRepository = fansRepository;
        this.redisTools = redisTools;
    }

    private FansRepository fansRepository;
    private final RedisTools redisTools;


    public Map<String, Object> getFans(String next_openid, boolean flush) {
        String access_token = getAccessToken(flush);
        String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + access_token;
        if (!ObjectUtils.isEmpty(next_openid)) {
            url = url + "&next_openid=" + next_openid;
        }
        ResponseEntity<Map> map = new RestTemplate().getForEntity(url, Map.class);
        log.info(map.getBody().toString());
        return map.getBody();
    }

    private ArrayList<String> allList = new ArrayList<>();

    public List<String> getOpenIds(String next) {
        Map<String, Object> map = getFans(next, false);
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        if (!ObjectUtils.isEmpty(map.get("errcode")) && 40001 == (int) map.get("errcode")) {
            map = getFans(next, true);
            data = (Map<String, Object>) map.get("data");
        }
        if (!ObjectUtils.isEmpty(data) && !ObjectUtils.isEmpty(data.get("openid"))) {
            List<String> res = (List<String>) data.get("openid");
            allList.addAll(res);
        }
        String new_next_openid = (String) map.get("next_openid");
        if (!ObjectUtils.isEmpty(new_next_openid) && !ObjectUtils.isEmpty(map.get("count")) && 10000 == (int) map.get("count")) {
            getOpenIds(new_next_openid);
        }
        List<String> list = new ArrayList<>(allList);
        allList.clear();
        return list;
    }

    public String getAccessToken(boolean flush) {
        String access_token = (String) redisTools.get("access_token");
//        if (= (String) redisTools.get("access_token"))
        if (flush || ObjectUtils.isEmpty(access_token)) {
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxcf71d6832b8e3ebe&secret=9401a98f94e021d3f006c3831f8d0e05";
            ResponseEntity<Map> map = new RestTemplate().getForEntity(url, Map.class);
            log.info(map.getBody().toString());
            access_token = (String) map.getBody().get("access_token");
            redisTools.set("access_token", access_token, 7200L);
        }
        return access_token;
    }

    public String getAccessToken() {
        return getAccessToken(false);
    }

    public Map getFansInfo(String openid, boolean flag) {
        String access_token = getAccessToken(flag);
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN";
        ResponseEntity<Map> map = new RestTemplate().getForEntity(url, Map.class);
        log.info(map.getBody().toString());
        if (!ObjectUtils.isEmpty(map.getBody().get("errcode")) && 40001 == (int) map.getBody().get("errcode")) {
            return getFansInfo(openid, true);
        }
        if (!ObjectUtils.isEmpty(map.getBody().get("errcode")) && 40003 == (int) map.getBody().get("errcode")) {
            return null;
        }
        return map.getBody();
    }


}
