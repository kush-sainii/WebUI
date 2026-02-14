package com.WebUI.WebUI.controller;

import com.WebUI.WebUI.services.CommandExecutorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/command")
@CrossOrigin(origins = "*")
public class CommandController {

    private final CommandExecutorService executor;

    public CommandController(CommandExecutorService executor) {
        this.executor = executor;
    }

    @PostMapping
    public ResponseEntity<?> runCommand(@RequestBody Map<String, String> payload) {
        String cmd = payload.get("command");
        if (!executor.isAllowed(cmd)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Command not allowed");
        }
        return ResponseEntity.ok(executor.executeCommand(cmd));
    }
}
