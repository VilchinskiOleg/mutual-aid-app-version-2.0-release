package messagechat.messagechatservice.persistent.repository;

import lombok.RequiredArgsConstructor;
import messagechat.messagechatservice.persistent.entity.Dialog;
import messagechat.messagechatservice.persistent.entity.DialogByMember;
import messagechat.messagechatservice.persistent.entity.Member;
import org.hibernate.graph.GraphSemantic;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.Optional;

@RequiredArgsConstructor
public class ExtendedDialogRepositoryImpl implements ExtendedDialogRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<Dialog> findByDialogIdWithOptimisticLock(String dialogId) {
        var dialogGraph = entityManager.createEntityGraph(Dialog.class);
        dialogGraph.addAttributeNodes("dialogByMemberDetails");
        var dialogByMemberDetailsSubGraph = dialogGraph.addSubgraph("dialogByMemberDetails", DialogByMember.class);
        dialogByMemberDetailsSubGraph.addAttributeNodes("member");
        var memberSubGraph = dialogByMemberDetailsSubGraph.addSubgraph("member", Member.class);
        memberSubGraph.addAttributeNodes("memberInfo");

        return Optional.ofNullable(entityManager.createQuery("from Dialog d where d.dialogId = :dialogId", Dialog.class)
                .setParameter("dialogId", dialogId)
                .setLockMode(LockModeType.OPTIMISTIC)
                .setHint(GraphSemantic.LOAD.getJpaHintName(), dialogGraph)
                .getSingleResult());
    }

//    @Override
//    public Page<Dialog> findAllByMemberId(String memberId, PageRequest pageRequest) {
//        Criteria subCriteria = where(PROFILE_ID).is(memberId);
//        Query query = new Query(where(MEMBERS).elemMatch(subCriteria));
//        List<Dialog> dialogs = mongoTemplate.find(query.with(pageRequest), Dialog.class);
//        return getPage(dialogs,
//                       pageRequest,
//                       () -> mongoTemplate.count(query.with(pageRequest).skip(-1).limit(-1), Dialog.class));
//    }
}