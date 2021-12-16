package messagechat.messagechatservice.mapper.response;

import messagechat.messagechatservice.rest.message.response.PageResponse;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.data.domain.Page;

public abstract class PageToPageResponseConverter<S, D extends PageResponse> extends BaseConverter<Page<S>, D> {

    @Override
    public void convert(Page<S> source, D destination) {
        destination.setCurrentPage(source.getNumber());
        destination.setSizeOfPage(source.getSize());
        destination.setAllPages(source.getTotalPages());
    }
}