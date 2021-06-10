package order.orderservice.mapper;

import order.orderservice.domain.model.Member;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class MemberToDataMemberConverter extends BaseConverter<Member, order.orderservice.persistent.entity.Member> {

    @Override
    protected order.orderservice.persistent.entity.Member getDestination() {
        return new order.orderservice.persistent.entity.Member();
    }

    @Override
    public void convert(Member source, order.orderservice.persistent.entity.Member destination) {
        destination.setMemberId(source.getMemberId());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setNickName(source.getNickName());
    }
}
