package com.croxx.hgwechat.service.didiumbrella;

import com.croxx.hgwechat.model.didiumbrella.DidiUmbrellaOrder;
import com.croxx.hgwechat.model.didiumbrella.DidiUmbrellaOrderReposity;
import com.croxx.hgwechat.req.didiumbrella.ReqDidiUmbrellaOrder;
import com.croxx.hgwechat.res.didiumbrella.ResJscode2Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DidiUmbrellaService {

    @Value("${wechat.didiumbrella.appid}")
    private String appid;
    @Value("${wechat.didiumbrella.secret}")
    private String secret;
    @Value("${wechat.didiumbrella.grant_type}")
    private String grant_type;
    @Value("${wechat.didiumbrella.url}")
    private String url;

    @Autowired
    private DidiUmbrellaOrderReposity didiUmbrellaOrderReposity;

    public ResJscode2Session jscode2session(@NotNull String jscode) {

        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> params = new HashMap<>();
        params.put("appid", appid);
        params.put("secret", secret);
        params.put("grant_type", grant_type);
        params.put("js_code", jscode);
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, params);
        try {
            return mapper.readValue(response.getBody(), ResJscode2Session.class);
        } catch (IOException e) {
            return null;
        }
    }

    public DidiUmbrellaOrder order(ReqDidiUmbrellaOrder request, String openid) {
        DidiUmbrellaOrder order = new DidiUmbrellaOrder(
                openid, request.getFrom_nickname(), request.getFrom_longitude(), request.getFrom_latitude(), request.getTo_longitude(), request.getTo_latitude(), request.getFrom_position(), request.getTo_position(), request.getRemark(),
                new Date(), request.getEvent_time(), DidiUmbrellaOrder.STATUS_WAITING
        );
        didiUmbrellaOrderReposity.save(order);
        return order;
    }

    public DidiUmbrellaOrder getLatestFromOrderByOpenid(String openid) {
        List<DidiUmbrellaOrder> orders = didiUmbrellaOrderReposity.findLatestFromOrder(openid);
        if (orders.isEmpty()) return null;
        return orders.get(0);
    }

    public DidiUmbrellaOrder getLatestToOrderByOpenid(String openid) {
        List<DidiUmbrellaOrder> orders = didiUmbrellaOrderReposity.findLatestToOrder(openid);
        if (orders.isEmpty()) return null;
        return orders.get(0);
    }

    public DidiUmbrellaOrder getOrderByOidAndOpenid(long oid, String openid) {
        DidiUmbrellaOrder order = didiUmbrellaOrderReposity.findById(oid);
        if (order == null) return order;
        if (!order.getFrom_openid().equals(openid)) return null;
        return order;
    }

    public DidiUmbrellaOrder cancelOrderByOidAndOpenid(long oid, String openid) {
        DidiUmbrellaOrder order = didiUmbrellaOrderReposity.findById(oid);
        if (order == null) return order;
        if (!order.getFrom_openid().equals(openid)) return null;
        order.setStatus(DidiUmbrellaOrder.STATUS_CANCEL);
        didiUmbrellaOrderReposity.save(order);
        return order;
    }

    public List<DidiUmbrellaOrder> getWaitingOrders() {
        return didiUmbrellaOrderReposity.findByStatus(DidiUmbrellaOrder.STATUS_WAITING);
    }

    public List<DidiUmbrellaOrder> getHistoryOrdersByOpenid(String openid) {
        return didiUmbrellaOrderReposity.findHistory(openid);
    }

    public DidiUmbrellaOrder takeOrder(long oid, String openid, String nickname) {
        DidiUmbrellaOrder order = didiUmbrellaOrderReposity.findById(oid);
        if (order == null) return order;
        if (!order.getStatus().equals(DidiUmbrellaOrder.STATUS_WAITING)) return null;
        order.setStatus(DidiUmbrellaOrder.STATUS_RUNNING);
        order.setSolve_time(new Date());
        order.setTo_openid(openid);
        order.setTo_nickname(nickname);
        didiUmbrellaOrderReposity.save(order);
        return order;
    }

    public DidiUmbrellaOrder finishOrderByOidAndOpenid(long oid, String openid) {
        DidiUmbrellaOrder order = didiUmbrellaOrderReposity.findById(oid);
        if (order == null) return order;
        if (!order.getFrom_openid().equals(openid)) return null;
        order.setStatus(DidiUmbrellaOrder.STATUS_FINISH);
        didiUmbrellaOrderReposity.save(order);
        return order;
    }

    public DidiUmbrellaOrder freshOrderByOidAndOpenid(long oid, String openid) {
        DidiUmbrellaOrder order = didiUmbrellaOrderReposity.findById(oid);
        if (order == null) return order;
        if (!order.getFrom_openid().equals(openid) && !order.getTo_openid().equals(openid)) return null;
        return order;
    }

}
