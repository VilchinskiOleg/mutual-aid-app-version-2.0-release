package messagechat.messagechatservice.service;


import messagechat.messagechatservice.MessageChatServiceApplication;
import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.domain.model.Member;
import messagechat.messagechatservice.domain.service.DialogService;
import messagechat.messagechatservice.domain.service.client.ProfileClientService;
import messagechat.messagechatservice.persistent.repository.DialogRepository;
import messagechat.messagechatservice.service.common.DatabaseSourceTestConfig;
import messagechat.messagechatservice.service.common.ProfileMockTestExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tms.common.auth.configuration.utils.AuthenticationUtils;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static messagechat.messagechatservice.domain.model.Dialog.Status.NOT_ACTIVE;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("local")
@SpringBootTest(
        classes = {MessageChatServiceApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ExtendWith(SpringExtension.class)
public class DialogServiceTest extends DatabaseSourceTestConfig implements ProfileMockTestExtension {

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("datasource.message-chat.jdbc-url",postgresDB::getJdbcUrl);
        registry.add("datasource.message-chat.username", () -> TEST_DB_USERNAME);
        registry.add("datasource.message-chat.password", () -> TEST_DB_PASSWORD);
    }


    @Resource
    private DialogService dialogService;
    @Resource
    private DialogRepository dialogRepository;

    @MockBean
    private ProfileClientService profileClientService;


    @BeforeAll
    static void initAll() {
        showTestDbUrl();
    }


    @Test
    void success_when_new_user_try_to_join_chanel() {
        final String firstUserId = "1296234-assdfgsdf-230914";
        final String secondUserId = "1231234-asdfsdf-234sd637";
        final String thirdUserId = "1dgoy1234-lkayrf-7709437";
        when(profileClientService.getProfileById((String) any())).thenAnswer(args -> generateProfile(args.getArgument(0)));
        String chanelName = "TEST_CHANEL_NAME";

        // Create new Chanel:
        Dialog chanel;
        try (MockedStatic<AuthenticationUtils> utilities = Mockito.mockStatic(AuthenticationUtils.class)) {
            utilities.when(AuthenticationUtils::fetchAuthorOfRequestUserIdFromAuthContext).thenReturn(firstUserId);
            chanel = dialogService.createNewChanel(chanelName, Set.of(firstUserId, secondUserId));
        }
        assertEquals(2, chanel.getMembers().size());

        // Update Chanel by joining new User:
        var dialogData = Dialog.builder()
                .internalId(chanel.getInternalId())
                .members(Set.of(new Member(firstUserId), new Member(secondUserId), new Member(thirdUserId)))
                .build();
        dialogService.updateDialog(dialogData, thirdUserId);

        // Verify:
        var updatedChanel = dialogRepository.findByDialogId(chanel.getInternalId()).get();
        assertEquals(3, updatedChanel.getMembers().size());
    }

    @Test
    void check_optimistic_lock_for_update_one_Dialog_by_several_users_simultaneously() {
        final String firstUserId = "1296234-assdfgsdf-230914";
        final String secondUserId = "1231234-asdfsdf-234sd637";
        final String thirdUserId = "1dgoy1234-lkayrf-7709437";
        when(profileClientService.getProfileById((String) any())).thenAnswer(args -> generateProfile(args.getArgument(0)));
        var executorService = Executors.newFixedThreadPool(2);
        String chanelName0 = "TEST_CHANEL_NAME_0";
        String chanelName1 = "TEST_CHANEL_NAME_1";
        String chanelName2 = "TEST_CHANEL_NAME_3";
        final var latch = new CountDownLatch(1);

        // Create new Chanel:
        Dialog chanel;
        try (MockedStatic<AuthenticationUtils> utilities = Mockito.mockStatic(AuthenticationUtils.class)) {
            utilities.when(AuthenticationUtils::fetchAuthorOfRequestUserIdFromAuthContext).thenReturn(firstUserId);
            chanel = dialogService.createNewChanel(chanelName0, Set.of(firstUserId, secondUserId));
        }

        // Update existed Chanel by two different Threads simultaneously:
        // [1.] Add new Member to the chanel.
        var dialogDataFirstThreadChange = Dialog.builder()
                .internalId(chanel.getInternalId())
                .name(chanelName1)
                .status(NOT_ACTIVE)
                .members(Set.of(new Member(firstUserId), new Member(secondUserId), new Member(thirdUserId)))
                .build();
        Future<Dialog> resultByFirstThread = executorService.submit(() -> {
            latch.await();
            return dialogService.updateDialog(dialogDataFirstThreadChange, thirdUserId);
        });
        // [2.] Remove existed Member from the chanel.
        var dialogDataSecondThreadChange = Dialog.builder()
                .internalId(chanel.getInternalId())
                .name(chanelName2)
                .status(NOT_ACTIVE)
                .members(Set.of(new Member(firstUserId), new Member(thirdUserId)))
                .build();
        Future<Dialog> resultBySecondThread = executorService.submit(() -> {
            latch.await();
            return dialogService.updateDialog(dialogDataSecondThreadChange, secondUserId);
        });

        executorService.shutdown();
        latch.countDown();

        // Waiting for result:
        await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .until(() -> resultByFirstThread.isDone() && resultBySecondThread.isDone());

        // Validate:
        var updatedChanel = dialogService.findDialogByInternalIdRequired(chanel.getInternalId());
        if (secondUserId.equals(updatedChanel.getModifyByMemberId())) {
            // REMOVING operation was accomplished:
            assertEquals(1, updatedChanel.getMembers().size());
            assertFalse(updatedChanel.getMembers().contains(new Member(secondUserId)));
        } else {
            // ADDING operation was accomplished:
            assertEquals(thirdUserId, updatedChanel.getModifyByMemberId());
            assertEquals(3, updatedChanel.getMembers().size());
            assertTrue(updatedChanel.getMembers().contains(new Member(thirdUserId)));
        }
    }
}