package com.chris.order.service.impl;

import com.chris.order.dataobject.OrderDetail;
import com.chris.order.dataobject.OrderMaster;
import com.chris.order.dataobject.ProductInfo;
import com.chris.order.dto.CartDTO;
import com.chris.order.dto.OrderDTO;
import com.chris.order.enums.OrderStatusEnum;
import com.chris.order.enums.PayStatusEnum;
import com.chris.order.repository.OrderDetailRepository;
import com.chris.order.repository.OrderMasterRepository;
import com.chris.order.service.OrderService;
import com.chris.order.utils.KeyUtil;
import com.chris.product.client.ProductClient;
import com.chris.product.common.DecreaseStockInput;
import com.chris.product.common.ProductInfoOutput;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductClient productClient;

    @Override
    public OrderDTO create(OrderDTO orderDTO) {
        String orderId = KeyUtil.genUniqueKey();

        //查询商品信息（调用商品服务）
        List<String> productIdList = orderDTO.getOrderDetailList().stream().map(OrderDetail::getProductId).collect(Collectors.toList());
        List<ProductInfoOutput> productInfoList = productClient.listForOrder(productIdList);

        //计算总价
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            for (ProductInfoOutput productInfoOutput : productInfoList) {
                //单价*数量
                orderAmount = productInfoOutput.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(orderAmount);
                BeanUtils.copyProperties(productInfoOutput, orderDetail);
                orderDetail.setOrderId(orderId);
                orderDetail.setDetailId(KeyUtil.genUniqueKey());
                //订单详情入库
                orderDetailRepository.save(orderDetail);
            }
        }

        //扣库存（调用商品服务）
        List<DecreaseStockInput> decreaseStockInputList = orderDTO.getOrderDetailList().stream().map(o -> new DecreaseStockInput(o.getProductId(), o.getProductQuantity())).collect(Collectors.toList());
        productClient.decreaseStock(decreaseStockInputList);

        //订单入库
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);
        return orderDTO;
    }
}
