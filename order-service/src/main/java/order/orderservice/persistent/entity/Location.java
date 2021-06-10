package order.orderservice.persistent.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location {

    private String country;
    private String city;
    private String street;
    private String home;
    private Integer flat;
}
