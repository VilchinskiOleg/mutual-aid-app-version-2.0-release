package org.tms.profile_service_rest.domain.service.processor;

import static java.time.Duration.between;
import static java.time.LocalDateTime.now;
import static org.tms.profile_service_rest.utils.Constant.Service.*;

import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.Random;
import java.util.UUID;

@Component
public class IdGeneratorService {

    private final Random random = new Random();

    public String generate() {
        long most64SigBits = get64MostSignificantBitsForVersion1();
        long least64SigBits = get64LeastSignificantBitsForVersion1();
        return new UUID(most64SigBits, least64SigBits).toString();
    }

    private long get64LeastSignificantBitsForVersion1() {
        long random63BitLong = random.nextLong() & GENERATE_ID_U_64_TICKS_MASK;
        long variant3BitFlag = GENERATE_ID_U_64_LOCAL_MASK;
        return random63BitLong + variant3BitFlag;
    }

    private long get64MostSignificantBitsForVersion1() {
        Duration duration = between(GLOBAL_MARK_START_COUNT_TIME_BY_GREGORIAN, now());
        long seconds = duration.getSeconds();
        long nanos = duration.getNano();
        long timeForUuidIn100Nanos = seconds * 10000000 + nanos * 100;
        long least12SignificantBitOfTime = (timeForUuidIn100Nanos & 0x000000000000FFFFL) >> 4;
        long version = 1 << 12;
        return (timeForUuidIn100Nanos & 0xFFFFFFFFFFFF0000L) + version + least12SignificantBitOfTime;
    }
}
