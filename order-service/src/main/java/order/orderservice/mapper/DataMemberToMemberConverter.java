package order.orderservice.mapper;

import order.orderservice.persistent.mongo.entity.Member;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class DataMemberToMemberConverter extends BaseConverter<Member, order.orderservice.domain.model.Member> {

    @Override
    protected order.orderservice.domain.model.Member getDestination() {
        return new order.orderservice.domain.model.Member();
    }

    @Override
    public void convert(Member source, order.orderservice.domain.model.Member destination) {
        destination.setMemberId(source.getMemberId());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setNickName(source.getNickName());
    }
}
