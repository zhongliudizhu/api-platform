package com.winstar.event;

import com.winstar.cashier.entity.PayOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * Created by zl on 2018/4/3
 */
@Getter
@Setter
@ToString
public class ModifyOrderEvent extends ApplicationEvent {

    private PayOrder payOrder;

    public ModifyOrderEvent(Object source, PayOrder payOrder) {
        super(source);
        this.payOrder = payOrder;
    }

}
