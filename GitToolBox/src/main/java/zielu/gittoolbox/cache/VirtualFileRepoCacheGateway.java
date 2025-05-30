package zielu.gittoolbox.cache;

import static zielu.gittoolbox.cache.VirtualFileRepoCache.CACHE_CHANGE;

import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBus;
import org.jetbrains.annotations.NotNull;
import zielu.gittoolbox.metrics.Metrics;
import zielu.gittoolbox.metrics.ProjectMetrics;
import zielu.gittoolbox.util.GatewayBase;

class VirtualFileRepoCacheGateway extends GatewayBase {
  private final Metrics metrics;
  private final MessageBus messageBus;

  VirtualFileRepoCacheGateway(@NotNull Project project, @NotNull ProjectMetrics metrics) {
    super(project);
    this.metrics = metrics;
    this.messageBus = project.getMessageBus();
  }

  Metrics getMetrics() {
    return metrics;
  }

  void fireCacheChanged() {
    messageBus.syncPublisher(CACHE_CHANGE).updated();
  }
}
