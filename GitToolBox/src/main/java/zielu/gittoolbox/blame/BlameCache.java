package zielu.gittoolbox.blame;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;

public interface BlameCache {
  Topic<BlameCacheListener> CACHE_UPDATES = Topic.create("blame cache updates", BlameCacheListener.class);

  @NotNull
  BlameAnnotation getAnnotation(@NotNull VirtualFile file);

  void refreshForRoot(@NotNull VirtualFile root);

  void invalidate(@NotNull VirtualFile file);

  static BlameCache getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, BlameCache.class);
  }
}
