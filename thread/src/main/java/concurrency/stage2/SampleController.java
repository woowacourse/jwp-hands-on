package concurrency.stage2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class SampleController {

    private static final Logger log = LoggerFactory.getLogger(SampleController.class);

    private static final AtomicInteger count = new AtomicInteger(0);

    private final HelloWorldService helloWorldService;

    @Autowired
    public SampleController(final HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    /**
     * 동시에 들어온 요청들에 대해 `accept-count`와 `max-connections`의 합만큼을 커넥션 풀에서 관리하며 처리 및 대기 상태로 유지!
     * 그 이상을 넘어선 요청은 컨트롤러에 도달 불가!
     * 바쁘던 쓰레드에서 현재 작업이 완료되면 커넥션 풀의 요청들 하나씩 받아서 처리!
     */
    @GetMapping("/test")
    @ResponseBody
    public String helloWorld() throws InterruptedException {
        Thread.sleep(500);
        log.info("http call count : {}", count.incrementAndGet());
        return helloWorldService.helloWorld();
    }
}
