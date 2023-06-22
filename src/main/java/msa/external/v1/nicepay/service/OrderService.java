    package msa.external.v1.nicepay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import msa.external.v1.nicepay.dao.OrderDao;
import msa.external.v1.nicepay.dto.CallbackOutDto;
import msa.external.v1.nicepay.dto.OrderGetDto;
import msa.external.v1.nicepay.model.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderService {
    private final OrderDao orderDao;

    public int createOrder(CallbackOutDto outDto) {
        int resultCount = orderDao.createOrder(outDto);
        return resultCount;
    }

    public OrderGetDto getOrderList() {
        int cuid = 1;
        OrderGetDto getDto = new OrderGetDto();
        List<Order> orderList = orderDao.getOrderList(cuid);
        for(Order order : orderList) {
            // payMethod 치환
            if(order.getPay_method().equals("VBANK")) {
                order.setPay_method("가상계좌");
            }
            if(order.getPay_method().equals("CARD")) {
                order.setPay_method("신용카드");
            }
            if(order.getPay_method().equals("BANK")) {
                order.setPay_method("계좌이체");
            }
            if(order.getPay_method().equals("CELLPHONE")) {
                order.setPay_method("휴대폰결제");
            }
            // odStatus 치환
            if(order.getOd_status().equals("0")) {
                order.setOd_status("결제대기");
            }
            if(order.getOd_status().equals("1")) {
                order.setOd_status("결제완료");
            }
            if(order.getOd_status().equals("2")) {
                order.setOd_status("결제취소");
            }
            if(order.getOd_status().equals("3")) {
                order.setOd_status("주문만료");
            }
        }
        getDto.setList(orderList);
        return getDto;
    }
}
