package com.alorsfaim.iot.web;

import com.alorsfaim.iot.data.CommandDTO;
import com.alorsfaim.iot.service.CommandRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CommandController {

    private final CommandRunner commandRunner;

    public CommandController(CommandRunner commandRunner) {
        this.commandRunner = commandRunner;
    }

    @PostMapping("/master-d-commands")
    public void runMasterDCommand(@RequestBody CommandDTO commandDTO) {
        log.info("Run Master D command {}", commandDTO);
        commandRunner.runCommand(commandDTO.getCommand());
    }
}
