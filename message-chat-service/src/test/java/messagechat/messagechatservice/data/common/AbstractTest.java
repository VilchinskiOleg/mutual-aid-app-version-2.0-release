package messagechat.messagechatservice.data.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Cleanup;
import lombok.NonNull;
import messagechat.messagechatservice.persistent.entity.*;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;
import org.junit.jupiter.api.BeforeEach;

import javax.validation.constraints.NotBlank;
import java.io.IOException;

import static java.time.LocalDateTime.now;

public abstract class AbstractTest extends DatabaseSourceTestConfig {

    private static final String MOCK_ENTITY_URL = "data/";
    private static final ObjectMapper READER = new ObjectMapper().registerModule(new JavaTimeModule());;


    @BeforeEach
    protected void initEach() {
        showTestDbUrl();
    }


    protected Dialog createDialogForCouple(@NonNull Session hibernateEntityManagerImpl,
                                         String dialogId) {
        return createDialogImpl(
                hibernateEntityManagerImpl,
                dialogId,
                messagechat.messagechatservice.domain.model.Dialog.Type.FACE_TO_FACE_DIALOG.name());
    }

    protected Dialog createChanel(@NonNull Session hibernateEntityManagerImpl,
                                         String dialogId) {
        return createDialogImpl(
                hibernateEntityManagerImpl,
                dialogId,
                messagechat.messagechatservice.domain.model.Dialog.Type.CHANNEL.name());
    }

    private Dialog createDialogImpl(@NonNull Session hibernateEntityManagerImpl,
                                  @NotBlank String dialogId,
                                  @NotBlank String dialogType) {
        var transaction = hibernateEntityManagerImpl.beginTransaction();
        try {
            var usrInfo1 = MemberInfo.builder()
                    .firstName("firstname of test-user-1")
                    .lastName("lastname of test-user-1")
                    .nickName("nickname of test-user-1").build();
            var usr1 = new Member("test-user-1", usrInfo1);

            var usrInfo2 = MemberInfo.builder()
                    .firstName("firstname of test-user-2")
                    .lastName("lastname of test-user-2")
                    .nickName("nickname of test-user-2").build();
            var usr2 = new Member("test-user-2", usrInfo2);

            var dialog = Dialog.builder()
                    .dialogId(dialogId)
                    .name("name of " + dialogId)
                    .status("ACTIVE")
                    .type(dialogType)
                    .createAt(now())
                    .createByMemberId("creator of " + dialogId).build();
            dialog.addMember(usr1);
            dialog.addMember(usr2);

            var msg1 = Message.builder()
                    .messageId("test-message-1")
                    .description("description of test-message-1")
                    .createAt(now()).build();
            msg1.setDialog(dialog);
            msg1.setAuthor(usr1);

            Thread.sleep(2000);

            var msg2 = Message.builder()
                    .messageId("test-message-2")
                    .description("description of test-message-2")
                    .createAt(now()).build();
            msg2.setDialog(dialog);
            msg2.setAuthor(usr2);

            //save dialog ([!]use methods):
            hibernateEntityManagerImpl.persist(dialog);
            transaction.commit();
            hibernateEntityManagerImpl.clear();
            return dialog;
        } catch (Exception ex) {
            transaction.rollback();
            return null;
        }
    }

    protected Dialog readDialogByDialogIdWithFetchedMembers(@NonNull Session hibernateEntityManagerImpl,
                                                            @NonNull String dialogId) {
        RootGraph<Dialog> dialogGraph = hibernateEntityManagerImpl.createEntityGraph(Dialog.class);
        dialogGraph.addAttributeNodes("dialogByMemberDetails");
        SubGraph<DialogByMember> dialogByMemberDetailsSubGraph = dialogGraph.addSubgraph("dialogByMemberDetails", DialogByMember.class);
        dialogByMemberDetailsSubGraph.addAttributeNodes("member");

        Dialog result = hibernateEntityManagerImpl.createQuery("from Dialog d where d.dialogId = :dialogId", Dialog.class)
                .setParameter("dialogId", dialogId)
                .setHint(GraphSemantic.LOAD.getJpaHintName(), dialogGraph)
                .getSingleResult();
        result.getMembers().forEach(member -> System.out.println(member.getProfileId()));
        hibernateEntityManagerImpl.clear();
        return result;
    }

    protected void cleanDb(@NonNull Session hibernateEntityManagerImpl) {
        var transaction = hibernateEntityManagerImpl.beginTransaction();
        try {
            //remove dialogs ([!]use methods):
            hibernateEntityManagerImpl.createQuery("select d from Dialog d", Dialog.class).getResultList()
                    .forEach(hibernateEntityManagerImpl::remove);
            //remove members ([!]use methods):
            hibernateEntityManagerImpl.createQuery("select m from Member m", Member.class).getResultList()
                    .forEach(hibernateEntityManagerImpl::remove);
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
        }
    }


    private <T> T readMockEntity(String urlPostfix, Class<T> clazz) throws IOException {
        @Cleanup var inStream = getClass().getClassLoader().getResourceAsStream(MOCK_ENTITY_URL.concat(urlPostfix));
        return READER.readValue(inStream, clazz);
    }
}