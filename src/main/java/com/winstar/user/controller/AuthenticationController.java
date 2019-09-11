package com.winstar.user.controller;

import com.winstar.exception.NotRuleException;
import com.winstar.user.entity.Account;
import com.winstar.user.repository.AccountRepository;
import com.winstar.user.service.AccountService;
import com.winstar.vo.Result;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.winstar.utils.ValidatorParameterUtils.isMobile;

@RestController
@RequestMapping("/api/v1/cbc/authentication")
@AllArgsConstructor
public class AuthenticationController {
    @Autowired
    AccountRepository accountRepository;

    AccountService accountService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Result authentication(@RequestParam String mobile, @RequestParam String idCard, HttpServletRequest request) throws NotRuleException {
        String accountId = accountService.getAccountId(request);
        if (!isMobile(mobile)) {
            return Result.fail("verify_mobile_is_error", "请输入正确手机号码");
        }
        String regex = "\\d{15}(\\d{2}[0-9xX])?";
        if (idCard.matches(regex)) {
            System.out.println("是身份证号");
        } else {
            return Result.fail("verify_authentication_is_error", "请输入正确身份证号");
        }
        Account account = accountRepository.findAccountById(accountId);
        account.setMobile(mobile);
        account.setIdCard(idCard);
        accountRepository.save(account);
        return Result.success(null);
    }
    @RequestMapping(value = "/certification", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Result Account(HttpServletRequest request) throws NotRuleException {
        String accountId = accountService.getAccountId(request);
        Account account = accountRepository.findAccountById(accountId);
        if(ObjectUtils.isEmpty(account.getIdCard())){
            return Result.success(null);
        }else {
            return Result.fail("verify_user_is_error", "用户已经实名制过");
        }
    }
}
