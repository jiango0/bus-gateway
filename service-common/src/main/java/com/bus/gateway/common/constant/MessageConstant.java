package com.bus.gateway.common.constant;

public class MessageConstant {

    /** 待发送 */
    public static Integer SEND_MESSAGE_WAIT = 0;
    /** 发送成功 */
    public static Integer SEND_MESSAGE_SUCCESS = 5;
    /** 消息确认成功（终态） */
    public static Integer SEND_MESSAGE_END = 6;
    /** 发送失败 */
    public static Integer SEND_MESSAGE_FAIL = 9;


    /** 接收成功 */
    public static Integer USE_MESSAGE_RECEIVE = 10;
    /** 处理成功 */
    public static Integer USE_MESSAGE_SUCCESS = 15;
    /** 回复确认成功 */
    public static Integer USE_MESSAGE_END = 16;
    /** 回复确认失败 */
    public static Integer USE_MESSAGE_FAIL = 19;


}
