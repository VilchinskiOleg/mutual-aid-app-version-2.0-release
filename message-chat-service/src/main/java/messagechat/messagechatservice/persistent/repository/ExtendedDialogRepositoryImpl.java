package messagechat.messagechatservice.persistent.repository;

//public class ExtendedDialogRepositoryImpl implements ExtendedDialogRepository {
//
//    @Resource
//    private MongoTemplate mongoTemplate;
//
//    @Override
//    public Page<Dialog> findAllByMemberId(String memberId, PageRequest pageRequest) {
//        Criteria subCriteria = where(PROFILE_ID).is(memberId);
//        Query query = new Query(where(MEMBERS).elemMatch(subCriteria));
//        List<Dialog> dialogs = mongoTemplate.find(query.with(pageRequest), Dialog.class);
//        return getPage(dialogs,
//                       pageRequest,
//                       () -> mongoTemplate.count(query.with(pageRequest).skip(-1).limit(-1), Dialog.class));
//    }
//}