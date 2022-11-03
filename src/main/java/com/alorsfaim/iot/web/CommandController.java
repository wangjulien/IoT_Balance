package com.alorsfaim.iot.web;

import com.alorsfaim.iot.data.CommandDTO;
import com.alorsfaim.iot.service.BalanceMessageConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CommandController {


    private final BalanceMessageConsumer balanceMessageConsumer;

    public CommandController(BalanceMessageConsumer balanceMessageConsumer) {
        this.balanceMessageConsumer = balanceMessageConsumer;
    }

    @PostMapping("/master-d-commands")
    public void runMasterDCommand(@RequestBody CommandDTO commandDTO) {
        log.info("Run Master D command {}", commandDTO);
        balanceMessageConsumer.sendCommandMessage(commandDTO.getCommand());
    }

    @GetMapping("/terminate")
    public void terminate() {
        log.info("Stop message broker");
        balanceMessageConsumer.terminate();
    }
}
