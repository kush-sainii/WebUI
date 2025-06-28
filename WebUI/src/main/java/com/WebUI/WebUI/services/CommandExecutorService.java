package com.WebUI.WebUI.services;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CommandExecutorService {

    public List<String> executeCommand(String command) {
        List<String> output = new ArrayList<>();
        try {
            // Use 'bash -c' on Linux/Mac or 'cmd /c' on Windows
            String[] fullCommand = System.getProperty("os.name").toLowerCase().contains("win")
                    ? new String[]{"cmd", "/c", command}
                    : new String[]{"bash", "-c", command};

            ProcessBuilder builder = new ProcessBuilder(fullCommand);
            Process process = builder.start();

            // Read output
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.add(line);
                }
            }

            process.waitFor();
        } catch (Exception e) {
            output.add("Error: " + e.getMessage());
        }
        return output;
    }

    private static final Set<String> ALLOWED_COMMANDS = Set.of(
            "systeminfo",
            "wmic cpu get loadpercentage",
            "netstat -ano",
            "mkdir"
    );

    public boolean isAllowed(String input) {
        return ALLOWED_COMMANDS.contains(input.trim());
    }
}