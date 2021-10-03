package order.orderservice.rest.service;

import static org.springframework.http.HttpStatus.OK;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import order.orderservice.domain.service.OrderService;
import order.orderservice.rest.message.OrderResponse;
import order.orderservice.rest.message.OrdersPageResponse;
import order.orderservice.rest.message.OrdersResponse;
import order.orderservice.rest.message.SearchOrderDetails;
import order.orderservice.rest.model.Order;
import org.common.http.autoconfiguration.annotation.Api;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping(path = "api/order-service/worker")
public class WorkerRest {

    @Resource
    private Mapper mapper;
    @Resource
    private OrderService orderService;

    @Api
    @ApiOperation(value = "${order.operation.get-executor-candidate-orders}")
    @ApiImplicitParam(name = "member-id", dataType = "string", paramType = "query", defaultValue = "123")
    @GetMapping
    @ResponseStatus(OK)
    public OrdersResponse getAllOrdersByExecutorOrCandidateIds(@RequestParam("member-id") String memberId) {
        var result = orderService.findByExecutorOrCandidateIds(memberId);
        return new OrdersResponse(mapper.map(result, new ArrayList<>(), Order.class));
    }

    @Api
    @ApiOperation(value = "${order.operation.get-order-by-id}")
    @ApiImplicitParam(name = "order-id", dataType = "string", paramType = "path", defaultValue = "123")
    @GetMapping(path = "/{order-id}")
    @ResponseStatus(OK)
    public OrderResponse getOrderByOrderId(@PathVariable("order-id") String orderId) {
        var result = orderService.findByOrderIdRequired(orderId);
        return new OrderResponse(mapper.map(result, Order.class));
    }

    @Api
    @ApiOperation(value = "${order.operation.search-by-part-title}")
    @GetMapping(path = "/title-fragment/{subTitle}")
    @ResponseStatus(OK)
    public OrdersPageResponse getAllOrdersByPartOfTitle(@PathVariable("subTitle") String subTitle,
                                                        @RequestParam("pageNumber") Integer pageNumber,
                                                        @RequestParam("size") Integer size) {
        var result = orderService.findByPartOfTitle(subTitle, pageNumber, size);
        return mapper.map(result, OrdersPageResponse.class);
    }

    @Api
    @ApiOperation(value = "${order.operation.search-by-filters}")
    @PostMapping(path = "/search-by-filters")
    @ResponseStatus(OK)
    public OrdersPageResponse getAllOrdersByFilters(@Valid @RequestBody SearchOrderDetails searchDetails) {
        var details = mapper.map(searchDetails, order.orderservice.domain.model.search.SearchOrderDetails.class);
        var result = orderService.findByFilters(details);
        return mapper.map(result, OrdersPageResponse.class);
    }

    //TODO: add spring security for methods ->:
    @Api
    @ApiOperation(value = "${order.operation.choose-order}")
    @PutMapping(path = "/choose/{order-id}/{member-id}")
    @ResponseStatus(OK)
    public OrderResponse chooseOrder(@PathVariable("order-id") String orderId,
                             @PathVariable("member-id") String memberId) {
        var updatedOrder = orderService.chooseOrder(orderId, memberId);
        return new OrderResponse(mapper.map(updatedOrder, Order.class));
    }
}
