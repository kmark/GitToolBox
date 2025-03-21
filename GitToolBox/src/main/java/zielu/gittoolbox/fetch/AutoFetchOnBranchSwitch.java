package zielu.gittoolbox.fetch;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import git4idea.repo.GitRepository;
import java.time.Duration;
import java.util.Collection;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import zielu.gittoolbox.cache.RepoInfo;
import zielu.gittoolbox.util.AppUtil;

class AutoFetchOnBranchSwitch {
  private final Logger log = Logger.getInstance(getClass());
  private final AutoFetchSchedule schedule;
  private final AutoFetchExecutor executor;

  AutoFetchOnBranchSwitch(@NotNull AutoFetchSchedule schedule, @NotNull AutoFetchExecutor executor) {
    this.schedule = schedule;
    this.executor = executor;
  }

  void onBranchSwitch(@NotNull RepoInfo current, @NotNull GitRepository repository) {
    if (current.status().isTrackingRemote()) {
      Duration delay = schedule.calculateTaskDelayOnBranchSwitch(repository);
      log.info("Auto-fetch delay on branch switch is " + delay);
      if (!delay.isZero()) {
        executor.scheduleTask(delay, repository);
      }
    }
  }

  void onRepositoriesRemoved(@NotNull Collection<GitRepository> repositories) {
    schedule.repositoriesRemoved(repositories);
  }

  @NotNull
  static AutoFetchOnBranchSwitch getInstance(@NotNull Project project) {
    return AppUtil.getServiceInstance(project, AutoFetchOnBranchSwitch.class);
  }

  @NotNull
  static Optional<AutoFetchOnBranchSwitch> getExistingInstance(@NotNull Project project) {
    return AppUtil.getExistingServiceInstance(project, AutoFetchOnBranchSwitch.class);
  }
}
