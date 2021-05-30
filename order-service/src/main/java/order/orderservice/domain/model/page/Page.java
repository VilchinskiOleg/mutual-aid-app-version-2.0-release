package order.orderservice.domain.model.page;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class Page<T> {

    private List<T> payload;

    private Integer allPages;
    private Integer currentPage;
    private Integer sizeOfPage;
}
