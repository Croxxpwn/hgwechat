package com.croxx.hgwechat.controller.didiumbrella.v1;

import com.croxx.hgwechat.model.didiumbrella.DidiUmbrellaOrder;
import com.croxx.hgwechat.req.didiumbrella.ReqDidiUmbrellaOrder;
import com.croxx.hgwechat.res.didiumbrella.ResDidiUmbrella;
import com.croxx.hgwechat.res.didiumbrella.ResDidiUmbrellaWithOrder;
import com.croxx.hgwechat.res.didiumbrella.ResDidiUmbrellaWithOrders;
import com.croxx.hgwechat.res.didiumbrella.ResJscode2Session;
import com.croxx.hgwechat.service.didiumbrella.DidiUmbrellaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/didiumbrella/v1")
public class DidiUmbrellaControllerV1 {

    @Autowired
    private DidiUmbrellaService didiUmbrellaService;

    @PostMapping("/order/new")
    public ResDidiUmbrella order(@RequestBody ReqDidiUmbrellaOrder request) {
        ResJscode2Session session = didiUmbrellaService.jscode2session(request.getJs_code());
        if (session != null) {
            if (session.getOpenid() != null) {
                DidiUmbrellaOrder order = didiUmbrellaService.order(request, session.getOpenid());
                return new ResDidiUmbrellaWithOrder(ResDidiUmbrella.ERRCODE_SUCCESS, ResDidiUmbrella.ERRMSG_SUCCESS, order);
            } else {
                return new ResDidiUmbrella(ResDidiUmbrella.ERRCODE_DATA_ERROR, ResDidiUmbrella.ERRMSG_DATA_ERROR);
            }
        } else {
            return new ResDidiUmbrella(ResDidiUmbrella.ERRCODE_SERVICE_ERROR, ResDidiUmbrella.ERRMSG_SERVICE_ERROR);
        }
    }

    @GetMapping("/order/latest/{js_code}")
    public ResDidiUmbrella hasOrder(@PathVariable("js_code") String js_code) {
        ResJscode2Session session = didiUmbrellaService.jscode2session(js_code);
        if (session != null) {
            if (session.getOpenid() != null) {
                DidiUmbrellaOrder order = didiUmbrellaService.getLatestFromOrderByOpenid(session.getOpenid());
                if (order == null) {
                    order = didiUmbrellaService.getLatestToOrderByOpenid(session.getOpenid());
                }
                return new ResDidiUmbrellaWithOrder(ResDidiUmbrella.ERRCODE_SUCCESS, ResDidiUmbrella.ERRMSG_SUCCESS, order);
            } else {
                return new ResDidiUmbrella(ResDidiUmbrella.ERRCODE_DATA_ERROR, ResDidiUmbrella.ERRMSG_DATA_ERROR);
            }
        } else {
            return new ResDidiUmbrella(ResDidiUmbrella.ERRCODE_SERVICE_ERROR, ResDidiUmbrella.ERRMSG_SERVICE_ERROR);
        }
    }

    @DeleteMapping("/order/{oid}/cancel/{js_code}")
    public ResDidiUmbrella cancel(@PathVariable("oid") Long oid, @PathVariable("js_code") String js_code) {
        ResJscode2Session session = didiUmbrellaService.jscode2session(js_code);
        if (session != null) {
            if (session.getOpenid() != null) {
                DidiUmbrellaOrder order = didiUmbrellaService.cancelOrderByOidAndOpenid(oid, session.getOpenid());
                if (order == null) {
                    return new ResDidiUmbrella(ResDidiUmbrella.ERRCODE_DATA_ERROR, ResDidiUmbrella.ERRMSG_DATA_ERROR);
                } else {
                    return new ResDidiUmbrellaWithOrder(ResDidiUmbrella.ERRCODE_SUCCESS, ResDidiUmbrella.ERRMSG_SUCCESS, order);
                }
            } else {
                return new ResDidiUmbrella(ResDidiUmbrella.ERRCODE_DATA_ERROR, ResDidiUmbrella.ERRMSG_DATA_ERROR);
            }
        } else {
            return new ResDidiUmbrella(ResDidiUmbrella.ERRCODE_SERVICE_ERROR, ResDidiUmbrella.ERRMSG_SERVICE_ERROR);
        }
    }

    @PostMapping("/order/{oid}/finish/{js_code}")
    public ResDidiUmbrella finish(@PathVariable("oid") Long oid, @PathVariable("js_code") String js_code) {
        ResJscode2Session session = didiUmbrellaService.jscode2session(js_code);
        if (session != null) {
            if (session.getOpenid() != null) {
                DidiUmbrellaOrder order = didiUmbrellaService.finishOrderByOidAndOpenid(oid, session.getOpenid());
                if (order == null) {
                    return new ResDidiUmbrella(ResDidiUmbrella.ERRCODE_DATA_ERROR, ResDidiUmbrella.ERRMSG_DATA_ERROR);
                } else {
                    return new ResDidiUmbrellaWithOrder(ResDidiUmbrella.ERRCODE_SUCCESS, ResDidiUmbrella.ERRMSG_SUCCESS, order);
                }
            } else {
                return new ResDidiUmbrella(ResDidiUmbrella.ERRCODE_DATA_ERROR, ResDidiUmbrella.ERRMSG_DATA_ERROR);
            }
        } else {
            return new ResDidiUmbrella(ResDidiUmbrella.ERRCODE_SERVICE_ERROR, ResDidiUmbrella.ERRMSG_SERVICE_ERROR);
        }
    }

    @GetMapping("/order/waiting")
    public ResDidiUmbrella getWaitingOrders() {
        List<DidiUmbrellaOrder> orders = didiUmbrellaService.getWaitingOrders();
        return new ResDidiUmbrellaWithOrders(ResDidiUmbrella.ERRCODE_SUCCESS, ResDidiUmbrella.ERRMSG_SUCCESS, orders);
    }

    @GetMapping("/order/history/{js_code}")
    public ResDidiUmbrella getHistoryOrders(@PathVariable("js_code") String js_code) {
        ResJscode2Session session = didiUmbrellaService.jscode2session(js_code);
        if (session != null) {
            if (session.getOpenid() != null) {
                List<DidiUmbrellaOrder> orders = didiUmbrellaService.getHistoryOrdersByOpenid(session.getOpenid());
                return new ResDidiUmbrellaWithOrders(ResDidiUmbrella.ERRCODE_SUCCESS, ResDidiUmbrella.ERRMSG_SUCCESS, orders);
            } else {
                return new ResDidiUmbrella(ResDidiUmbrella.ERRCODE_DATA_ERROR, ResDidiUmbrella.ERRMSG_DATA_ERROR);
            }
        } else {
            return new ResDidiUmbrella(ResDidiUmbrella.ERRCODE_SERVICE_ERROR, ResDidiUmbrella.ERRMSG_SERVICE_ERROR);
        }
    }

    @PostMapping("/order/{oid}/take/{js_code}/{nickname}")
    public ResDidiUmbrella takeOrder(@PathVariable("oid") long oid, @PathVariable("js_code") String js_code, @PathVariable("nickname") String nickname) {
        ResJscode2Session session = didiUmbrellaService.jscode2session(js_code);
        if (session != null) {
            if (session.getOpenid() != null) {
                DidiUmbrellaOrder order = didiUmbrellaService.takeOrder(oid, session.getOpenid(), nickname);
                if (order == null) {
                    return new ResDidiUmbrella(ResDidiUmbrella.ERRCODE_DATA_ERROR, ResDidiUmbrella.ERRMSG_DATA_ERROR);
                } else {
                    return new ResDidiUmbrellaWithOrder(ResDidiUmbrella.ERRCODE_SUCCESS, ResDidiUmbrella.ERRMSG_SUCCESS, order);
                }
            } else {
                return new ResDidiUmbrella(ResDidiUmbrella.ERRCODE_DATA_ERROR, ResDidiUmbrella.ERRMSG_DATA_ERROR);
            }
        } else {
            return new ResDidiUmbrella(ResDidiUmbrella.ERRCODE_SERVICE_ERROR, ResDidiUmbrella.ERRMSG_SERVICE_ERROR);
        }
    }

    @PostMapping("/order/{oid}/fresh/{openid}")
    public ResDidiUmbrella freshOrder(@PathVariable("oid") long oid, @PathVariable("openid") String openid) {
        DidiUmbrellaOrder order = didiUmbrellaService.freshOrderByOidAndOpenid(oid, openid);
        return new ResDidiUmbrellaWithOrder(ResDidiUmbrella.ERRCODE_SUCCESS, ResDidiUmbrella.ERRMSG_SUCCESS, order);
    }

}
