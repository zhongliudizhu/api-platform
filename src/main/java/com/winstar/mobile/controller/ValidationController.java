package com.winstar.mobile.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.kaptcha.exception.KaptchaRenderException;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.winstar.mobile.domain.CmResult;
import com.winstar.mobile.domain.SendMessageRequestDomain;
import com.winstar.mobile.domain.VerifyRequestDomain;
import com.winstar.mobile.entity.ReceiveQualification;
import com.winstar.mobile.repository.ReceiveQualificationRepository;
import com.winstar.mobile.service.CmService;
import com.winstar.order.utils.DateUtil;
import com.winstar.redis.RedisTools;
import com.winstar.user.service.AccountService;
import com.winstar.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.winstar.utils.ValidatorParameterUtils.isMobile;

@RestController
@Slf4j
@RequestMapping("/api/v1/cbc/mobile/validation")
@AllArgsConstructor
public class ValidationController {
    RedisTools redisTools;

    CmService cmService;

    AccountService accountService;

    ReceiveQualificationRepository receiveQualificationRepository;

    DefaultKaptcha kaptcha;

    /**
     * 发送短信验证码
     */
    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public Result sendMessage(@RequestParam String mobile, @RequestParam String code){
        String redisCode = (String) redisTools.get(code);
        if (!isMobile(mobile)) {
            return Result.fail("verify_mobile_is_error", "请输入正确手机号码");
        }
        if (ObjectUtils.isEmpty(redisCode)) {
            return Result.fail("verify_code_is_error", "图形验证码错误");
        }
        SendMessageRequestDomain domain = new SendMessageRequestDomain();
        domain.setSerialNumber(mobile);
        domain.setBusinessCode("96044072");
        log.info("参数：" + JSON.toJSONString(domain));
        CmResult cmResult = cmService.sendMessage(domain);
        if (!"1000".equals(cmResult.getReturnCode())) {
            return Result.fail(cmResult.getReturnCode(), cmResult.getMessage());
        }
        return Result.success(null);
    }

    /**
     * 活动校验
     */
    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public Result sendVerify(@RequestParam String mobile,@RequestParam String platType,@RequestParam String smsCode) {
        log.info("mobile is {} and smsCode is {}", mobile, smsCode);
        List<ReceiveQualification> receiveQualifications = receiveQualificationRepository.findByPhoneAndPlatTypeAndReceiveTimeAfter(mobile,platType,DateUtil.getMonthBegin());
        if (!ObjectUtils.isEmpty(receiveQualifications)) {
            return Result.fail("user_has_received", "当月用户该月已领取");
        }
        VerifyRequestDomain domain = new VerifyRequestDomain();
        domain.setSerialNumber(mobile);
        domain.setSmsCode(smsCode);
        domain.setPackageId("96041141");
        domain.setProductId("69900043");
        CmResult cmResult = cmService.verify(domain);
        if (cmResult.getMessage().indexOf("同一个营销活动不能重复办理：您已经有一个有效的营销活动")!= -1) {
            redisTools.set("122_" + mobile, "YES");
            return Result.success("1005",null);
        } else {
            return Result.fail("user_no_transaction", "您的手机号暂无可领取权益，可先去办理权益哦");
        }

    }

    /**
     * 获取图片验证码
     */
    @RequestMapping(value = "/render", method = RequestMethod.POST)
    public void render(HttpServletResponse response){
        response.setDateHeader("Expires", 0L);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        //生成
        String code = kaptcha.createText();
        try {
            ServletOutputStream out = response.getOutputStream();
            Throwable t = null;
            try {
                redisTools.set(code, code, 120L);
                ImageIO.write(this.kaptcha.createImage(code), "jpg", out);
            } catch (Throwable throwable) {
                t = throwable;
                throw throwable;
            } finally {
                if (out != null) {
                    if (t != null) {
                        try {
                            out.close();
                        } catch (Throwable throwable) {
                            t.addSuppressed(throwable);
                        }
                    } else {
                        out.close();
                    }
                }
            }
        } catch (IOException ioException) {
            throw new KaptchaRenderException(ioException);
        }
        log.info("render()  code is {}", code);
    }
}
