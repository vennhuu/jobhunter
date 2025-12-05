package vn.vennhuu.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.vennhuu.jobhunter.service.EmailService;
import vn.vennhuu.jobhunter.service.SubscriberService;
import vn.vennhuu.jobhunter.util.annotation.APIMessage;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService,
            SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }
    
    @GetMapping("/email")
    @APIMessage("Send simple email")
    // @Scheduled(cron = "*/30 * * * * *")
    // @Transactional
    public String sendSimpleEmail() {
        // this.emailService.sendSimpleEmail();
        // this.emailService.sendEmailSync("ads.vennhuu@gmail.com", "test send email",
        // "<h1> <b> hello </b> </h1>", false,
        // true);
        // this.emailService.sendEmailFromTemplateSync("ads.vennhuu@gmail.com", "test
        // send email", "job");
        this.subscriberService.sendSubscribersEmailJobs();
        return "ok";
    }
    
}
