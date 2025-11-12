package com.isscrm.isscrm_backend.service;

import com.isscrm.isscrm_backend.model.Device;
import com.isscrm.isscrm_backend.model.DeviceJob;
import com.isscrm.isscrm_backend.repository.DeviceJobRepository;
import com.isscrm.isscrm_backend.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeviceJobService {

    private final DeviceJobRepository jobRepo;
    private final DeviceRepository deviceRepo;

    public DeviceJobService(DeviceJobRepository jobRepo, DeviceRepository deviceRepo) {
        this.jobRepo = jobRepo;
        this.deviceRepo = deviceRepo;
    }

    public List<DeviceJob> getJobsForDevice(Long deviceId) {
        return jobRepo.findByDeviceId(deviceId);
    }

    public DeviceJob createJob(Long deviceId, DeviceJob job) {
        Device device = deviceRepo.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found"));
        job.setDevice(device);
        jobRepo.save(job);
        return job;
    }

    public DeviceJob updateJobStatus(Long jobId, String status, String result) {
        DeviceJob job = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        if (status.equalsIgnoreCase("RUNNING")) job.markRunning();
        else if (status.equalsIgnoreCase("SUCCESS")) job.markSuccess(result);
        else if (status.equalsIgnoreCase("FAILED")) job.markFailed(result);
        return jobRepo.save(job);
    }
}
