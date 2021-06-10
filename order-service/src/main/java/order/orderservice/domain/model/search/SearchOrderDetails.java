package order.orderservice.domain.model.search;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class SearchOrderDetails {

    private Integer numberOfPage;
    private Integer sizeOfPage;

    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private String type;
    private String priority;

    private String locationCountry;
    private String locationCity;
    private String locationStreet;
    private String locationHome;
}
