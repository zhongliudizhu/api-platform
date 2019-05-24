package com.winstar.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.winstar.coupon.repository.MyCouponRepository;
import com.winstar.couponActivity.entity.InviteTableLog;
import com.winstar.couponActivity.entity.MileageObtainLog;
import com.winstar.couponActivity.repository.InviteTableLogRepository;
import com.winstar.couponActivity.repository.MileageObtainLogRepository;
import com.winstar.couponActivity.utils.ActivityIdEnum;
import com.winstar.couponActivity.utils.TimeUtil;
import com.winstar.couponActivity.utils.UtilConstants;
import com.winstar.exception.NotFoundException;
import com.winstar.kafka.Product;
import com.winstar.order.entity.FlowOrder;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.FlowOrderRepository;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.order.service.OilOrderService;
import com.winstar.order.utils.Constant;
import com.winstar.order.utils.FlowOrderUtil;
import com.winstar.order.vo.FlowResult;
import com.winstar.order.vo.PayInfoVo;
import com.winstar.user.service.OneMoneyCouponRecordService;
import com.winstar.user.utils.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author shoo on 2017/12/14 9:54.
 * 付款成功回调订单
 * ！！！修改此类一定联系张林 ！！！
 */
@Service
public class OilOrderServiceImpl implements OilOrderService {
    private static Logger logger = LoggerFactory.getLogger(OilOrderServiceImpl.class);
    @Autowired
    private OilOrderRepository oilOrderRepository;
    @Autowired
    private FlowOrderRepository flowOrderRepository;
    @Autowired
    private OneMoneyCouponRecordService oneMoneyCouponRecordService;
    @Autowired
    private InviteTableLogRepository inviteTableLogRepository;
    @Autowired
    MileageObtainLogRepository mileageObtainLogRepository;
    @Autowired
    MyCouponRepository myCouponRepository;
    @Value("${info.flowUrl}")
    private String flowUrl;

    @Autowired
    Product product;

    @Value("${spring.kafka.template.default-topic}")
    private String topicName;

    @Override
    public String updateOrderCashier(PayInfoVo payInfo) {
        Date time = new Date();
        Integer payStatus = payInfo.getPayState();
        if (payStatus != 0 && payStatus != 1) {
            return "1";
        }
        String serialNumber = payInfo.getOrderSerialNumber();
        if (serialNumber.contains("wxyj")) {
            OilOrder oilOrder = oilOrderRepository.findBySerialNumber(serialNumber);
            if (ObjectUtils.isEmpty(oilOrder)) {
                return "2";
            }
            if (oilOrder.getItemId().equals(Constant.ONE_BUY_ITEMID)) {
                oneMoneyCouponRecordService.changeStatus(oilOrder.getAccountId());
            }
            //裂变活动订单回调
            fassionCallBack(oilOrder);
            oilOrder.setBankSerialNo(payInfo.getBankSerialNumber());
            oilOrder.setPayTime(payInfo.getPayTime());
            oilOrder.setPayType(payInfo.getPayType());
            oilOrder.setPayStatus(payInfo.getPayState());

            oilOrder.setSendStatus(3);
            oilOrder.setStatus(3);
            /******************/
            oilOrder.setIsAvailable("0");
            oilOrder.setUpdateTime(time);
            oilOrder.setFinishTime(time);
            oilOrderRepository.save(oilOrder);
            try {
                product.sendMessage(topicName, oilOrder.getSerialNumber(), JSON.toJSONString(oilOrder));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ServiceManager.orderRedPackageInfoService.generateOrdersRedPackageInfoByOrder(oilOrder);
        } else {
            FlowOrder flowOrder = flowOrderRepository.findBySerialNumber(serialNumber);
            if (ObjectUtils.isEmpty(flowOrder)) {
                return "2";
            }
            flowOrder.setBankSerialNo(payInfo.getBankSerialNumber());
            flowOrder.setPayTime(payInfo.getPayTime());
            flowOrder.setPayType(payInfo.getPayType());
            flowOrder.setPayStatus(payInfo.getPayState());//支付成功
            FlowResult flowResult = FlowOrderUtil.chargeFlow(flowOrder.getPhoneNo(), "M", "1024", "N", "30", flowUrl);

            if (ObjectUtils.isEmpty(flowResult) || flowResult.getOrderStatus().equals("SubmitFail")) {
                flowOrder.setSendStatus(4);//发货失败
                flowOrder.setStatus(3);//已完成
            } else {
                flowOrder.setSendStatus(3);//发货成功
                flowOrder.setStatus(3);//已完成
            }
            flowOrder.setUpdateTime(time);
            flowOrder.setFinishTime(time);
            flowOrderRepository.save(flowOrder);
            try {
                product.sendMessage(topicName, flowOrder.getSerialNumber(), JSON.toJSONString(flowOrder));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        /*//活动2发优惠券
        try{
            if(oilOrder.getActivityId().equals(Constant.CBC_ACTIVITY_SEC)){
                MyCoupon coupon = couponService.sendCoupon(oilOrder.getAccountId(),oilOrder.getActivityId(),oilOrder.getItemId());
                if(!ObjectUtils.isEmpty(coupon)){
                    logger.info("活动2，发优惠券，couponId："+coupon.getId());
                }
            }
        }catch (Exception ex){
            logger.error("发优惠券失败"+ex);
        }*/
        return "ok";
    }


    @Override
    public OilOrder getOneOrder(String serialNumber) throws NotFoundException {
        OilOrder oilOrder = new OilOrder();
        if (serialNumber.contains("wxyj")) {
            oilOrder = oilOrderRepository.findBySerialNumber(serialNumber);
            if (ObjectUtils.isEmpty(oilOrder) || oilOrder.getIsAvailable().equals(Constant.IS_NORMAL_CANCELED)) {
                throw new NotFoundException("oilOrder.order");
            }
        } else {
            FlowOrder flowOrder = flowOrderRepository.findBySerialNumber(serialNumber);
            if (ObjectUtils.isEmpty(flowOrder) || flowOrder.getIsAvailable().equals(Constant.IS_NORMAL_CANCELED)) {
                throw new NotFoundException("flowOrder.order");
            }
            BeanUtils.copyProperties(flowOrder, oilOrder);
        }

        return oilOrder;
    }

    @Override
    public OilOrder getOrder(String serialNumber) {
        OilOrder oilOrder = new OilOrder();
        if (serialNumber.contains("wxyj")) {
            oilOrder = oilOrderRepository.findBySerialNumber(serialNumber);
            if (ObjectUtils.isEmpty(oilOrder)) {
                return null;
            }
        } else {
            FlowOrder flowOrder = flowOrderRepository.findBySerialNumber(serialNumber);
            if (ObjectUtils.isEmpty(flowOrder)) {
                return null;
            }
            BeanUtils.copyProperties(flowOrder, oilOrder);
        }
        return oilOrder;
    }

    /**
     * 裂变活动回调方法
     *
     * @param oilOrder
     */
    public void fassionCallBack(OilOrder oilOrder) {
        if (Integer.parseInt(oilOrder.getActivityId()) == ActivityIdEnum.ACTIVITY_ID_667.getActivity() && !StringUtils.isEmpty(oilOrder.getCouponId()) && inviteTableLogRepository.findByInvitedUserAndStateAndInvtiteState(oilOrder.getAccountId(), 1, 1).size() > 0) {
            if (Integer.parseInt(myCouponRepository.findOne(oilOrder.getCouponId()).getActivityId()) == ActivityIdEnum.ACTIVITY_ID_667.getActivity()) {
                List<InviteTableLog> inviteList = inviteTableLogRepository.findByInvitedUserAndState(oilOrder.getAccountId(), 1);
                MileageObtainLog mileageObtainLog;
                List<MileageObtainLog> mileageObtainLogList = new ArrayList<MileageObtainLog>();
                Double mileageSum = 10.0;
                Integer optainType = 2;
                //使用优惠券赠送里程
                mileageObtainLog = new MileageObtainLog(UUID.randomUUID().toString(), oilOrder.getAccountId(), UtilConstants.FissionActivityConstants.COUPON_MILEAFE, optainType, TimeUtil.getCurrentDateTime2(), 1);
                mileageObtainLogList.add(mileageObtainLog);
                logger.info(oilOrder.getAccountId() + ":使用优惠券获得10里程！");
                if (inviteList.size() > 0) {
                    //邀请人和被邀请人里程赠送，以及邀请状态和时间的更新
                    for (int i = 0; i < inviteList.size(); i++) {
                        String inviteUsreid = inviteList.get(i).getAccountId();
                        if (inviteList.get(i).getInviteType() == 0) {
                            mileageSum = UtilConstants.FissionActivityConstants.DIRECT_INVTIE_MILEAFE;
                            optainType = 1;
                            logger.info(inviteList.get(i).getAccountId() + ":直接邀请成功！获得10里程！");
                        } else {
                            mileageSum = UtilConstants.FissionActivityConstants.INDIRECT_INVTIE_MILEAFE;
                            optainType = 0;
                            logger.info(inviteList.get(i).getAccountId() + ":直接邀请成功！获得5里程！");
                        }
                        inviteList.get(i).setInvtiteState(0);
                        inviteList.get(i).setUpdateTime(TimeUtil.getCurrentDateTime2());
                        mileageObtainLog = new MileageObtainLog(UUID.randomUUID().toString(), inviteUsreid, mileageSum, optainType, TimeUtil.getCurrentDateTime2(), 1);
                        mileageObtainLogList.add(mileageObtainLog);
                    }
                }
                inviteTableLogRepository.save(inviteList);
                mileageObtainLogRepository.save(mileageObtainLogList);
            }
        }
    }
}
