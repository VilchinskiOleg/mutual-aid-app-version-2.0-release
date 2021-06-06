package order.orderservice.mapper;

import order.orderservice.domain.model.Member;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class MemberToApiMemberConverter extends BaseConverter<Member, order.orderservice.rest.model.Member> {

    @Override
    protected order.orderservice.rest.model.Member getDestination() {
        return new order.orderservice.rest.model.Member();
    }

    @Override
    public void convert(Member source, order.orderservice.rest.model.Member destination) {
        destination.setMemberId(source.getMemberId());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setNickName(source.getNickName());
    }
}
