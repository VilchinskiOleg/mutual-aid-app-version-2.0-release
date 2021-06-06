package order.orderservice.domain.model.page;

import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> {

    private List<T> payload;

    private Integer allPages;
    private Integer currentPage;
    private Integer sizeOfPage;
}
