package order.orderservice.rest.service;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import order.orderservice.domain.service.OrderService;
import order.orderservice.rest.message.OrderResponse;
import order.orderservice.rest.message.OrdersResponse;
import order.orderservice.rest.model.Order;
import org.common.http.autoconfiguration.annotation.Api;
import org.mapper.autoconfiguration.mapper.Mapper;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/order-service/customer")
public class CustomerRest {

    @Resource
    private Mapper mapper;
    @Resource
    private OrderService orderService;

    @Api
    @ApiOperation(value = "${order.operation.get-owner-orders}")
    @ApiImplicitParam(name = "member-id", dataType = "string", paramType = "query", defaultValue = "123")
    @GetMapping
    @ResponseStatus(OK)
    public OrdersResponse getAllOrdersByOwnerId(@RequestParam("member-id") String memberId) {

        return null;
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
    @ApiOperation(value = "${order.operation.create-order}")
    @PostMapping
    @ResponseStatus(CREATED)
    public OrderResponse createOrder(@Valid @RequestBody Order newOrderRequest) {
        var orderDetails = mapper.map(newOrderRequest, order.orderservice.domain.model.Order.class);
        var createdOrder = orderService.createOrder(orderDetails);
        return new OrderResponse(mapper.map(createdOrder, Order.class));
    }

    @Api
    @ApiOperation(value = "${order.operation.update-order}")
    @PutMapping(path = "/update-order/{order-id}")
    @ResponseStatus(OK)
    //@PreAuthorize("hasRole('OWNER')")     // TODO: how to permit this action for only owner of order ?
    public OrderResponse updateOrder(@Valid @RequestBody Order updatedOrderRequest,
                             @PathVariable("order-id") String orderId) {
        var orderDetails = mapper.map(updatedOrderRequest, order.orderservice.domain.model.Order.class);
        var updatedOrder = orderService.updateOrder(orderDetails, orderId);
        return new OrderResponse(mapper.map(updatedOrder, Order.class));
    }

    @Api
    @ApiOperation(value = "${order.operation.approve-order}")
    @PutMapping(path = "/approve-order/{order-id}")
    @ResponseStatus(OK)
    //@PreAuthorize("hasRole('OWNER')")     // TODO: how to permit this action for only owner of order ?
    public OrderResponse approveOrder(@PathVariable("order-id") String orderId,
                              @RequestParam("executor-id") String executorId) {
        var order = orderService.approveOrder(orderId, executorId);
        return new OrderResponse(mapper.map(order, Order.class));
    }

    @Api
    @ApiOperation(value = "${order.operation.close-order}")
    @PutMapping(path = "/close-order/{order-id}")
    @ResponseStatus(OK)
    //@PreAuthorize("hasRole('OWNER')")     // TODO: how to permit this action for only owner of order ?
    public OrderResponse closeOrder(@PathVariable("order-id") String orderId) {
        var order = orderService.closeOrder(orderId);
        return new OrderResponse(mapper.map(order, Order.class));
    }
}
