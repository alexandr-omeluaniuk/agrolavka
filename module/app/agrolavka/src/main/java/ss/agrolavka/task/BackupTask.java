package ss.agrolavka.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ss.agrolavka.service.BackupService;
import ss.martin.security.api.AlertService;

@Component
class BackupTask {

    @Autowired
    private BackupService backupService;

    @Autowired
    private AlertService alertService;

    @Scheduled(cron = "0 15 2 * * *")
    public void backupCron() {
        try {
            backupService.createBackup();
        } catch (Exception e) {
            alertService.sendAlert("Backup task problem", e);
        }
    }
}
