package order.orderservice.domain.service.processor;

import static java.util.Objects.isNull;
import static order.orderservice.util.Constant.Errors.EXECUTOR_SHOULD_BE_FROM_CANDIDATES;
import static order.orderservice.util.Constant.Errors.MEMBER_NOT_FUND;

import order.orderservice.domain.model.Member;
import order.orderservice.domain.model.Order;
import order.orderservice.domain.service.client.ProfileClientService;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.stereotype.Component;
import ort.tms.mutual_aid.profile_service.client.model.Profile;
import javax.annotation.Resource;
import java.util.Optional;
import java.util.Set;

@Component
public class ProfileService {

    @Resource
    private ProfileClientService profileClientService;
    @Resource
    private Mapper mapper;
    //TODO: have api-client for notification service;

    public void changeOrderExecutor(Order order, Member executor) {
        Set<Member> candidates = order.getCandidates();
        Member oldExecutor = order.getExecutor();
        Member newExecutor = getExecutorFromCandidatesRequired(executor, candidates);
        order.setExecutor(newExecutor);
        candidates.remove(newExecutor);
        candidates.add(oldExecutor);
        //TODO: notification oldExecutor and newExecutor.
    }

    public void populateOrderExecutor(Order order, String executorId) {
        Member executor = getExecutorFromCandidatesRequired(new Member(executorId), order.getCandidates());
        order.setExecutor(executor);
        order.getCandidates().remove(executor);
        //TODO: notification executor.
    }

    public void populateOrderExecutionCandidate(Order order, String candidateId) {
        Member candidate = retrieveMemberByIdRequired(candidateId);
        order.getCandidates().add(candidate);
        //TODO: notification order owner.
    }

    public Member retrieveMemberByIdRequired(String memberId) {
        Profile profile = profileClientService.getProfileById(memberId);
        if (isNull(profile)) {
            throw new ConflictException(MEMBER_NOT_FUND);
        }
        return mapper.map(profile, Member.class);
    }

    private Member getExecutorFromCandidatesRequired(Member executor, Set<Member> candidates) {
        Optional<Member> newExecutor = candidates.stream()
                .filter(candidate -> candidate.equals(executor))
                .findFirst();
        if (newExecutor.isEmpty()) {
            throw new ConflictException(EXECUTOR_SHOULD_BE_FROM_CANDIDATES);
        }
        return newExecutor.get();
    }
}