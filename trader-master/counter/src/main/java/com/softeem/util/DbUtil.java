package com.softeem.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.softeem.beans.*;
import com.softeem.cache.CacheType;
import com.softeem.cache.RedisStringCache;
import com.softeem.beans.Account;
import com.thirdpart.order.OrderCmd;
import com.thirdpart.order.OrderStatus;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;


/**
 * 单例数据库工具类
 */
@Log4j2
//第二步
@Component
public class DbUtil {

    private static DbUtil dbUtil = null;

//    第一步
    private DbUtil() {
    }

    //第三部
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    //第四部
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    //第二步
    @PostConstruct
    private void init() {
        log.info("init dbutil now");
        dbUtil = new DbUtil();
        //第四部
        dbUtil.setSqlSessionTemplate(this.sqlSessionTemplate);
    }

    //////////////////////////////身份认证/////////////////////////////////////
    public static Account queryAccount(long uid, String password){
        return dbUtil.getSqlSessionTemplate().selectOne(
                "userMapper.queryAccount",
                ImmutableMap.of("UId",uid,"Password",password)
        );
    }

    public static void updateLoginTime(long uid,String nowDate,
                                       String nowTime){
        dbUtil.getSqlSessionTemplate().update(
                "userMapper.updateAccountLoginTime",
                ImmutableMap.of(
                        "UId",uid,
                        "ModifyDate",nowDate,
                        "ModifyTime",nowTime
                )
        );
    }

    public static int updatePwd(long uid,String oldPwd,
                                String newPwd){
        return dbUtil.getSqlSessionTemplate().update(
                "userMapper.updatePwd",
                ImmutableMap.of("UId",uid,
                        "NewPwd",newPwd,
                        "OldPwd",oldPwd));
    }

    //////////////////////////////资金类////////////////////////////////////////
    public static long getBalance(long uid){
        Long res = dbUtil.getSqlSessionTemplate()
                .selectOne("orderMapper.queryBalance",
                        ImmutableMap.of("UId",uid));
        if(res == null){
            return -1;
        }else {
            return res;
        }
    }

    //////////////////////////////持仓类////////////////////////////////////////
    public static List<PosiInfo> getPosiList(long uid){
        //查缓存
        String suid = Long.toString(uid);
        String posiS = RedisStringCache.get(suid, CacheType.POSI);
        if(StringUtils.isEmpty(posiS)){
            //未查到 查库
            List<PosiInfo> tmp = dbUtil.getSqlSessionTemplate().selectList(
                    "orderMapper.queryPosi",
                    ImmutableMap.of("UId", uid)
            );
            List<PosiInfo> result =
                    CollectionUtils.isEmpty(tmp) ? Lists.newArrayList()
                            : tmp;
            //更新缓存
            RedisStringCache.cache(suid,JsonUtil.toJson(result),CacheType.POSI);
            return result;
        }else {
            //查到 命中缓存
            return JsonUtil.fromJsonArr(posiS,PosiInfo.class);
        }
    }

    //////////////////////////////委托类////////////////////////////////////////
    public static List<OrderInfo> getOrderList(long uid){
        //查缓存
        String suid = Long.toString(uid);
        String orderS = RedisStringCache.get(suid, CacheType.ORDER);
        if(StringUtils.isEmpty(orderS)){
            //未查到 查库
            List<OrderInfo> tmp = dbUtil.getSqlSessionTemplate().selectList(
                    "orderMapper.queryOrder",
                    ImmutableMap.of("UId", uid)
            );
            List<OrderInfo> result =
                    CollectionUtils.isEmpty(tmp) ? Lists.newArrayList()
                            : tmp;
            //更新缓存
            RedisStringCache.cache(suid,JsonUtil.toJson(result),CacheType.ORDER);
            return result;
        }else {
            //查到 命中缓存
            return JsonUtil.fromJsonArr(orderS,OrderInfo.class);
        }
    }

    //////////////////////////////成交类////////////////////////////////////////
    public static List<TradeInfo> getTradeList(long uid){
        //查缓存
        String suid = Long.toString(uid);
        String tradeS = RedisStringCache.get(suid, CacheType.TRADE);
        if(StringUtils.isEmpty(tradeS)){
            //未查到 查库
            List<TradeInfo> tmp = dbUtil.getSqlSessionTemplate().selectList(
                    "orderMapper.queryTrade",
                    ImmutableMap.of("UId", uid)
            );
            List<TradeInfo> result =
                    CollectionUtils.isEmpty(tmp) ? Lists.newArrayList()
                            : tmp;
            //更新缓存
            RedisStringCache.cache(suid,JsonUtil.toJson(result),CacheType.TRADE);
            return result;
        }else {
            //查到 命中缓存
            return JsonUtil.fromJsonArr(tradeS,TradeInfo.class);
        }
    }

    //////////////////////////////订单处理类///////////////////////////////////////
    public static int saveOrder(OrderCmd orderCmd){
        Map<String, Object> param = Maps.newHashMap();
        param.put("UId",orderCmd.uid);
        param.put("Code",orderCmd.code);
        param.put("Direction",orderCmd.direction.getDirection());
        param.put("Type",orderCmd.orderType.getType());
        param.put("Price",orderCmd.price);
        param.put("OCount",orderCmd.volume);
        param.put("TCount",0);
        param.put("Status", OrderStatus.NOT_SET.getCode());

        param.put("Data",TimeformatUtil.yyyyMMdd(orderCmd.timestamp));
        param.put("Time",TimeformatUtil.hhMMss(orderCmd.timestamp));

        int count = dbUtil.getSqlSessionTemplate().insert(
                "orderMapper.saveOrder",param
        );
        //判断是否成功
        if(count > 0){
            return Integer.parseInt(param.get("ID").toString());
        }else {
            return -1;
        }
    }



    //////////////////////////////股票信息查询///////////////////////////////////////
    public static List<Map<String,Object>> queryAllSotckInfo(){
        return dbUtil.getSqlSessionTemplate()
                .selectList("stockMapper.queryStock");
    }




}
