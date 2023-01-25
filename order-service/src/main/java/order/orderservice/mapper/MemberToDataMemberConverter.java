package order.orderservice.mapper;

import order.orderservice.domain.model.Member;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class MemberToDataMemberConverter extends BaseConverter<Member, order.orderservice.persistent.mongo.entity.Member> {

    @Override
    protected order.orderservice.persistent.mongo.entity.Member getDestination() {
        return new order.orderservice.persistent.mongo.entity.Member();
    }

    @Override
    public void convert(Member source, order.orderservice.persistent.mongo.entity.Member destination) {
        destination.setMemberId(source.getMemberId());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setNickName(source.getNickName());
    }
}
