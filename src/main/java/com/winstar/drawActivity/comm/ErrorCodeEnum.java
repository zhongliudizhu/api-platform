package com.winstar.drawActivity.comm;

/**
 * Created by zl on 2019/3/13
 */
public enum ErrorCodeEnum {

    ERROR_CODE_ACTIVITY_END("activity_end", "该活动已结束！"),
    ERROR_CODE_ACTIVITY_PARTAKE("activity_partake", "抱歉您已参与过该活动，无法重复参与哦！"),
    ERROR_CODE_ACTIVITY_HASPRIZE_99("activity_has_prize_99", "恭喜您成功抽中1元购买100元加油券资格！"),
    ERROR_CODE_ACTIVITY_HASPRIZE_999("activity_has_prize_999", "恭喜您成功抽中1元购买1000元加油券资格！"),
    ERROR_CODE_ACTIVITY_NOPRIZE("activity_no_prize", "很遗憾，您没有中奖~"),
    ERROR_CODE_ACTIVITY_ONLY_ONE("activity_only_one", "您的手速太快了，不带这么玩！"),
    ERROR_CODE_ACTIVITY_USER_NOT_RULE("activity_user_not_rule", "非交安卡用户不能参与！"),
    ERROR_CODE_ACTIVITY_USER_DID_NOT_BIND("activity_user_did_not_bind", "用户未认证交安卡"),
    ;

    private final String value;
    private final String description;

    ErrorCodeEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String value() {
        return this.value;
    }

    public String description() {
        return this.description;
    }

}
