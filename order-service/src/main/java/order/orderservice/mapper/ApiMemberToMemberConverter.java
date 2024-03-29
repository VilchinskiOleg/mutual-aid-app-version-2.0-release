package order.orderservice.mapper;

import order.orderservice.rest.model.Member;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class ApiMemberToMemberConverter extends BaseConverter<Member, order.orderservice.domain.model.Member> {

    @Override
    protected order.orderservice.domain.model.Member getDestination() {
        return new order.orderservice.domain.model.Member();
    }

    @Override
    public void convert(Member source, order.orderservice.domain.model.Member destination) {
        destination.setMemberId(source.getMemberId());
    }
}
