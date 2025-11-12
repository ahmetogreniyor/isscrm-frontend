package com.isscrm.isscrm_backend.controller;

import com.isscrm.isscrm_backend.model.DeviceJob;
import com.isscrm.isscrm_backend.service.DeviceJobService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/devices/{deviceId}/jobs")
@CrossOrigin(origins = "*")
public class DeviceJobController {

    private final DeviceJobService jobService;

    public DeviceJobController(DeviceJobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public List<DeviceJob> getJobs(@PathVariable Long deviceId) {
        return jobService.getJobsForDevice(deviceId);
    }

    @PostMapping
    public DeviceJob createJob(@PathVariable Long deviceId, @RequestBody DeviceJob job) {
        return jobService.createJob(deviceId, job);
    }

    @PutMapping("/{jobId}")
    public DeviceJob updateJobStatus(@PathVariable Long deviceId,
                                     @PathVariable Long jobId,
                                     @RequestParam String status,
                                     @RequestParam(required = false) String result) {
        return jobService.updateJobStatus(jobId, status, result);
    }
}
