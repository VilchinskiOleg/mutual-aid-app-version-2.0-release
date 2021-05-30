package order.orderservice.rest.service;

import static org.springframework.http.HttpStatus.OK;

import io.swagger.annotations.ApiImplicitParam;
import order.orderservice.domain.service.OrderService;
import order.orderservice.rest.message.OrdersResponse;
import order.orderservice.rest.message.SearchOrderDetails;
import order.orderservice.rest.model.Order;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/order-service/worker")
public class WorkerRest {

    @Resource
    private Mapper mapper;
    @Resource
    private OrderService orderService;

    @ApiImplicitParam(name = "member-id", dataType = "string", paramType = "query", defaultValue = "123")
    @GetMapping
    @ResponseStatus(OK)
    public OrdersResponse getAllOrdersByExecutorOrCandidateIds(@RequestParam("member-id") String memberId) {

        return null;
    }

    @ApiImplicitParam(name = "order-id", dataType = "string", paramType = "path", defaultValue = "123")
    @GetMapping(path = "/{order-id}")
    @ResponseStatus(OK)
    public Order getOrderByOrderId(@PathVariable("order-id") String orderId) {
        var result = orderService.findByOrderId(orderId);
        return mapper.map(result, Order.class);
    }

    @GetMapping(path = "/title-fragment/{line}")
    @ResponseStatus(OK)
    public OrdersResponse getAllOrdersByPartOfTitle(@PathVariable("line") String line) {

        return null;
    }

    @PostMapping(path = "/search-by-filters")
    @ResponseStatus(OK)
    public OrdersResponse getAllOrdersByFilters(@Valid @RequestBody SearchOrderDetails searchDetails) {
        var details = mapper.map(searchDetails, order.orderservice.domain.model.search.SearchOrderDetails.class);
        var result = orderService.findByFilters(details);
        return mapper.map(result, OrdersResponse.class);
    }

    @PutMapping(path = "/choose/{order-id}")
    @ResponseStatus(OK)
    public Order chooseOrder(@PathVariable("order-id") String orderId) {

        return null;
    }
}
