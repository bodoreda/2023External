package msa.external.v1.nicepay.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import msa.external.v1.nicepay.dto.*;
import msa.external.v1.nicepay.service.NicepayService;
import msa.external.v1.nicepay.service.OrderService;
import msa.external.v1.common.redis.RedisUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/external")
@RequiredArgsConstructor
@Log4j2
public class NicepayApi {
    private final NicepayService nicepayService;
    private final OrderService orderService;
    private final RedisUtils redisUtils;

    @PostMapping("/order")
    public ResponseEntity order(@RequestBody OrderInDto inDto) {
        log.traceEntry("{}", inDto);
        log.info("getData : {}", redisUtils.getData("refreshToken"));
        log.info("getUserInfo : {}", redisUtils.getUserInfo("userInfo"));
        OrderOutDto outDto = nicepayService.order(inDto);
        log.traceExit(outDto);
        return ResponseEntity.status(HttpStatus.OK).body(outDto);
    }

    @PostMapping("/callback")
    public ResponseEntity callback(@RequestBody Map<String, String> map) throws Exception {
        log.traceEntry("{}", map);
        CallbackOutDto outDto = nicepayService.callback(map);
        int resultCount = orderService.createOrder(outDto);
        log.debug("resultCount : {}", resultCount);
        log.traceExit(outDto);
        return ResponseEntity.status(HttpStatus.OK).body(outDto);
    }

    @PostMapping("/cancel")
    public ResponseEntity cancel(@RequestBody CancelInDto inDto) throws Exception {
        log.traceEntry("{}", inDto);
        CancelOutDto outDto = nicepayService.cancel(inDto);
        log.traceExit(outDto);
        return ResponseEntity.status(HttpStatus.OK).body(outDto);
    }

    @GetMapping("/getOrderList")
    public ResponseEntity getOrderList() {
        OrderGetDto getDto = new OrderGetDto();
        getDto = orderService.getOrderList();
        log.debug("getDto 확인 : {}",getDto.getList().get(0).toString());
        return ResponseEntity.status(HttpStatus.OK).body(getDto);
    }

}
