package order.orderservice.domain.service;

import static order.orderservice.util.Constant.Errors.CANNOT_CHANGE_ORDER_EXECUTOR;

import order.orderservice.domain.model.Member;
import order.orderservice.domain.model.Order;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.Set;

@Component
public class ProfileService {

    //TODO: have api-client for profile service.
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

    private Member getExecutorFromCandidatesRequired(Member executor, Set<Member> candidates) {
        Optional<Member> newExecutor = candidates.stream()
                .filter(candidate -> candidate.equals(executor))
                .findFirst();
        if (newExecutor.isEmpty()) {
            throw new ConflictException(CANNOT_CHANGE_ORDER_EXECUTOR);
        }
        return newExecutor.get();
    }

    private Member retrieveMemberByIdRequired(String memberId) {
        //TODO: get and extract member from profile-rest.
        return new Member(memberId, null, null,null);
    }
}
