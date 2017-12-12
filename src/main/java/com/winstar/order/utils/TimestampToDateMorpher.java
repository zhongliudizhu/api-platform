package com.winstar.order.utils;

import net.sf.ezmorph.object.AbstractObjectMorpher;

import java.util.Date;

/**
 * @author shoo 2017/4/1.
 */
public class TimestampToDateMorpher extends AbstractObjectMorpher {
    public Object morph(Object value) {
        if( value != null){
            return new Date(Long.parseLong(String.valueOf(value)));
        }
        return null;
    }

    @Override
    public Class morphsTo() {
        return Date.class;
    }

    public boolean supports( Class clazz ){
        return Long.class.isAssignableFrom( clazz );
    }
}