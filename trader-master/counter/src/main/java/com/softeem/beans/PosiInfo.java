package com.softeem.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 持仓信息
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
public class PosiInfo {

    private int id;
    /**
     * 用户ID
     */
    private long uid;
    /**
     * 股票编号
     */
    private int code;
    private String name;
    private long cost;
    private long count;


}
