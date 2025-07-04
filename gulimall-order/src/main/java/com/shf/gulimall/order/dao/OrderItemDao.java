package com.shf.gulimall.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shf.gulimall.order.entity.OrderItemEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 *
 * @author 夏沫止水
 * @email HeJieLin@gulimall.com
 * @date 2020-05-22 19:49:53
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {

}
