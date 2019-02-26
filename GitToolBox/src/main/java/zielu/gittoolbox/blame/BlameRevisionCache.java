package zielu.gittoolbox.blame;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.annotate.FileAnnotation;
import com.intellij.openapi.vcs.history.VcsFileRevision;
import com.intellij.openapi.vfs.VirtualFile;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import zielu.gittoolbox.util.AppUtil;

public interface BlameRevisionCache {
  @NotNull
  Blame getForLine(@NotNull FileAnnotation annotation, int lineNumber);

  Blame getForFile(@NotNull VirtualFile file, @NotNull VcsFileRevision revision);

  @NotNull
  static Optional<BlameRevisionCache> getExistingInstance(@NotNull Project project) {
    return AppUtil.getExistingServiceInstance(project, BlameRevisionCache.class);
  }
}
