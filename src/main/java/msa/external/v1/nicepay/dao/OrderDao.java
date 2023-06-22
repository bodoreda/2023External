package msa.external.v1.nicepay.dao;

import msa.external.v1.nicepay.dto.CallbackOutDto;
import msa.external.v1.nicepay.model.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDao {
    int createOrder(CallbackOutDto outDto);

    List<Order> getOrderList(int cuid);
}
