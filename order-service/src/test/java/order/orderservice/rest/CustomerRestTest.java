package order.orderservice.rest;

import static java.time.LocalDateTime.now;
import static order.orderservice.domain.model.Order.Status.ACTIVE;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import lombok.SneakyThrows;
import order.orderservice.domain.service.OrderService;
import order.orderservice.domain.service.strategy.ManageOrderStateStrategy;
import order.orderservice.mapper.*;
import order.orderservice.rest.message.OrderResponse;
import order.orderservice.rest.service.CustomerRest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mapper.autoconfiguration.ModelMapperConfig;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import javax.annotation.Resource;
import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import order.orderservice.rest.model.Order;
import org.tms.common.auth.configuration.WebSecurityConfiguration;

/**
 * @WebMvcTest provides you only with rest layer (+ default security logics if it necessary).
 * So if you want to have some additional beans (not mock), you can use @ContextConfiguration,
 * but in that case you will have to declare your 'controller' classes one more in @ContextConfiguration.
 */
@TestInstance(PER_CLASS)
@WebMvcTest(controllers = CustomerRest.class)
@ContextConfiguration(classes = {
        ModelMapperConfig.class,
        ApiOrderToOrderConverter.class, OrderToApiOrderConverter.class,
        ApiMemberToMemberConverter.class, MemberToApiMemberConverter.class,
        ApiLocationToLocationConverter.class, LocationToApiLocationConverter.class,

        CustomerRest.class, WebSecurityConfiguration.class
})
public class CustomerRestTest {

    private static final String MOCK_VALID_ORDER_REQUEST_PATH = "ordermock/rest/validOrder.json";
    private static final String MOCK_INVALID_ORDER_REQUEST_PATH = "ordermock/rest/invalidOrder.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Resource
    private MockMvc mockMvc;
    @Resource
    private Mapper mapper;
    @MockBean
    private OrderService orderService;
    @MockBean
    private Map<String, ManageOrderStateStrategy> orderStateStrategies;

    @BeforeAll
    public static void initialAll(){
        objectMapper.registerModule(new JSR310Module());
        objectMapper.setSerializationInclusion(Include.NON_NULL);
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "mock-user")
    public void create_new_order_successfully_if_rest_model_is_valid() {
        final LocalDateTime newOrderCreateAt = now();
        var mockOrder = objectMapper.readValue(new ClassPathResource(MOCK_VALID_ORDER_REQUEST_PATH).getURL(), Order.class);
        var mockOrderStr = objectMapper.writeValueAsString(mockOrder);

        //mock domain logics for processing of 'new Order' inside controller:
        Mockito.when(orderService.createOrder(any())).thenAnswer(args -> {
            var order = (order.orderservice.domain.model.Order) args.getArguments()[0];
            order.setCreateAt(newOrderCreateAt);
            order.setStatus(ACTIVE);
            return order;
        });

        //validate:
        var mvcResult = mockMvc
                .perform(post("/api/order-service/customer")
                        .content(mockOrderStr)
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andReturn();

        mockOrder.setCreateAt(newOrderCreateAt);
        mockOrder.setStatus(ACTIVE.toString());
        var expectedOrderResponse = new OrderResponse(mockOrder);

        assertEquals(
                objectMapper.writeValueAsString(expectedOrderResponse),
                mvcResult.getResponse().getContentAsString()
        );
    }

    /**
     * Check my custom constraint @ValidOrder.
     * Try to provide order with UNPAID 'type' but with none null 'price' value.
     */
    @Test
    @SneakyThrows
    @WithMockUser(username = "mock-user")
    public void fail_new_order_request_if_rest_model_is_not_valid() {
        var mockOrder = objectMapper.readValue(new ClassPathResource(MOCK_INVALID_ORDER_REQUEST_PATH).getURL(), Order.class);
        var mockOrderStr = objectMapper.writeValueAsString(mockOrder);

        //validate:
        mockMvc
                .perform(post("/api/order-service/customer")
                        .content(mockOrderStr)
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }
}