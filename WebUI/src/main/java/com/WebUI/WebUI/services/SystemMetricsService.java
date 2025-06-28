package com.WebUI.WebUI.services;


import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.*;

@Service
public class SystemMetricsService {

    private final SystemInfo systemInfo = new SystemInfo();
    private final long appStartTime = System.currentTimeMillis();

    public Map<String, Object> fetchSystemMetrics() {
        Map<String, Object> result = new HashMap<>();

        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        CentralProcessor processor = hardware.getProcessor();
        GlobalMemory memory = hardware.getMemory();
        OperatingSystem os = systemInfo.getOperatingSystem();

        // CPU
        Map<String, Object> cpuMap = new HashMap<>();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(1000);
        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks);
        cpuMap.put("usage_percent", Math.round(cpuLoad * 10000.0) / 100.0);

        long[][] prevProcTicks = processor.getProcessorCpuLoadTicks();
        Util.sleep(1000);
        double[] coreLoads = processor.getProcessorCpuLoadBetweenTicks(prevProcTicks);

        List<Double> perCore = new ArrayList<>();
        for (double load : coreLoads) {
            perCore.add(Math.round(load * 10000.0) / 100.0);
        }
        cpuMap.put("per_core", perCore);
        result.put("cpu", cpuMap);

        // Memory
        Map<String, Object> memoryMap = new HashMap<>();
        long total = memory.getTotal();
        long available = memory.getAvailable();
        long used = total - available;
        memoryMap.put("total_mb", total / (1024 * 1024));
        memoryMap.put("used_mb", used / (1024 * 1024));
        memoryMap.put("free_mb", available / (1024 * 1024));
        memoryMap.put("usage_percent", Math.round((used * 100.0) / total * 100.0) / 100.0);
        result.put("memory", memoryMap);

        // Disk
        Map<String, Object> diskMap = new HashMap<>();
        long totalDisk = 0, freeDisk = 0;
        for (oshi.software.os.OSFileStore fs : os.getFileSystem().getFileStores()) {
            totalDisk += fs.getTotalSpace();
            freeDisk += fs.getUsableSpace();
        }
        diskMap.put("total_gb", totalDisk / (1024 * 1024 * 1024));
        diskMap.put("free_gb", freeDisk / (1024 * 1024 * 1024));
        diskMap.put("used_gb", (totalDisk - freeDisk) / (1024 * 1024 * 1024));
        result.put("disk", diskMap);

        // Network
        Map<String, Object> netMap = new HashMap<>();
        try {
            for (NetworkIF net : hardware.getNetworkIFs()) {
                net.updateAttributes();
                String[] ips = net.getIPv4addr();
                if (ips.length > 0) {
                    netMap.put("ip", ips[0]);
                    break;
                }
            }
            netMap.put("public_ip", getPublicIP());

        } catch (Exception e) {
            netMap.put("ip", "Unknown");
            netMap.put("public_ip", "Unknown");
        }
        result.put("network", netMap);

        // Uptime
        Map<String, Object> uptimeMap = new HashMap<>();
        uptimeMap.put("system_seconds", systemInfo.getOperatingSystem().getSystemUptime());
        uptimeMap.put("app_seconds", (System.currentTimeMillis() - appStartTime) / 1000);
        result.put("uptime", uptimeMap);

        return result;
    }

    private String getPublicIP() {
        try {
            URL url = new URL("http://checkip.amazonaws.com/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String ip = in.readLine();
            in.close();
            return ip;
        } catch (Exception e) {
            return "Unknown";
        }
    }
}
