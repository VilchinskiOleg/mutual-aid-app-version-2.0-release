package order.orderservice.rest.service;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import order.orderservice.domain.service.OrderService;
import order.orderservice.rest.model.Order;
import org.mapper.autoconfiguration.mapper.Mapper;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/order-service/customer")
public class CustomerRest {

    @Resource
    private Mapper mapper;
    @Resource
    private OrderService orderService;

    @ApiOperation(value = "${order.operation.get-owner-orders}")
    @ApiImplicitParam(name = "member-id", dataType = "string", paramType = "query", defaultValue = "123")
    @GetMapping
    @ResponseStatus(OK)
    public List<Order> getAllOrdersByOwnerId(@RequestParam("member-id") String memberId) {

        return null;
    }

    @ApiOperation(value = "${order.operation.get-order-by-id}")
    @ApiImplicitParam(name = "order-id", dataType = "string", paramType = "path", defaultValue = "123")
    @GetMapping(path = "/{order-id}")
    @ResponseStatus(OK)
    public Order getOrderByOrderId(@PathVariable("order-id") String orderId) {
        var result = orderService.findByOrderId(orderId);
        return mapper.map(result, Order.class);
    }

    @ApiOperation(value = "${order.operation.create-order}")
    @PostMapping
    @ResponseStatus(CREATED)
    public Order createOrder(@Valid @RequestBody Order newOrderRequest) {
        var orderDetails = mapper.map(newOrderRequest, order.orderservice.domain.model.Order.class);
        var createdOrder = orderService.createOrder(orderDetails);
        return mapper.map(createdOrder, Order.class);
    }

    @ApiOperation(value = "${order.operation.update-order}")
    @PutMapping
    @ResponseStatus(OK)
    public Order updateOrder(@Valid @RequestBody Order updatedOrderRequest) {

        return null;
    }

    @ApiOperation(value = "${order.operation.approve-order}")
    @PutMapping(path = "/approve-order/")
    @ResponseStatus(OK)
    //@PreAuthorize("hasRole('OWNER')")     // TODO: how to permit this action for only owner of order ?
    public Order approveOrder() {

        return null;
    }

    @ApiOperation(value = "${order.operation.close-order}")
    @PutMapping(path = "/close-order/")
    @ResponseStatus(OK)
    //@PreAuthorize("hasRole('OWNER')")     // TODO: how to permit this action for only owner of order ?
    public Order closeOrder() {

        return null;
    }

    //TODO: remove closed orders by [sheduling/job] after some time (after closing)
}
