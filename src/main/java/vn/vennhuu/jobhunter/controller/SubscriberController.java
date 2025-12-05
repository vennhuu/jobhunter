package vn.vennhuu.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.vennhuu.jobhunter.domain.Subscriber;
import vn.vennhuu.jobhunter.service.SubscriberService;
import vn.vennhuu.jobhunter.util.SecurityUtil;
import vn.vennhuu.jobhunter.util.annotation.APIMessage;
import vn.vennhuu.jobhunter.util.error.InvalidException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @APIMessage("Create a subscriber")
    public ResponseEntity<Subscriber> create(@Valid @RequestBody Subscriber sub) throws InvalidException {
        // check email
        boolean isExist = this.subscriberService.isExistsByEmail(sub.getEmail());
        if (isExist == true) {
            throw new InvalidException("Email " + sub.getEmail() + " đã tồn tại");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.subscriberService.create(sub));
    }

    @PutMapping("/subscribers")
    @APIMessage("Update a subscriber")
    public ResponseEntity<Subscriber> update(@RequestBody Subscriber subsRequest) throws InvalidException {
        // check id
        Subscriber subsDB = this.subscriberService.findById(subsRequest.getId());
        if (subsDB == null) {
            throw new InvalidException("Id " + subsRequest.getId() + " không tồn tại");
        }
        return ResponseEntity.ok().body(this.subscriberService.update(subsDB, subsRequest));
    }

    @PostMapping("/subscribers/skills")
    @APIMessage("Get subscriber's skill")
    public ResponseEntity<Subscriber> getSubscribersSkill() throws InvalidException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        return ResponseEntity.ok().body(this.subscriberService.findByEmail(email));
    }
}
