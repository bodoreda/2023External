package msa.external.v1.nicepay.dto;

import lombok.Data;
import msa.external.v1.nicepay.model.Order;

import java.util.List;

@Data
public class OrderGetDto {
    private List<Order> list;
}
