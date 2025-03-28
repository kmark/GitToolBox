package zielu.gittoolbox.ui.config.prj

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.SimpleTextAttributes.ERROR_ATTRIBUTES
import com.intellij.ui.SimpleTextAttributes.GRAYED_ATTRIBUTES
import git4idea.repo.GitRepository
import zielu.gittoolbox.config.AutoFetchExclusionConfig
import zielu.gittoolbox.config.RemoteConfig
import zielu.gittoolbox.repo.GtRepositoryImpl
import zielu.gittoolbox.util.GtUtil
import java.util.Optional
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode

internal class AutoFetchExclusionTreeRenderer(private val project: Project) : ColoredTreeCellRenderer() {
  override fun customizeCellRenderer(
    tree: JTree,
    value: Any?,
    selected: Boolean,
    expanded: Boolean,
    leaf: Boolean,
    row: Int,
    hasFocus: Boolean
  ) {
    when (val userValue = (value as DefaultMutableTreeNode).userObject) {
      is AutoFetchExclusionConfig -> {
        val repository = findRepository(value)
        if (repository.isPresent) {
          render(repository.get())
        } else {
          renderMissing(userValue.repositoryRootPath)
        }
      }
      is RemoteConfig -> {
        val parentConfig = value.parent as DefaultMutableTreeNode
        val repository = findRepository(parentConfig)
        render(userValue, repository)
      }
    }
  }

  private fun findRepository(node: DefaultMutableTreeNode): Optional<GitRepository> {
    val userObject = node.userObject
    if (userObject is AutoFetchExclusionConfig) {
      return GtUtil.getRepositoryForRoot(project, userObject.repositoryRootPath)
    }
    return Optional.empty()
  }

  private fun render(repository: GitRepository) {
    append(GtUtil.name(repository))
    append(" (${repository.root.presentableUrl})", GRAYED_ATTRIBUTES)
  }

  private fun render(remote: RemoteConfig, repository: Optional<GitRepository>) {
    append(remote.name)
    if (repository.isPresent) {
      GtRepositoryImpl(repository.get()).findRemote(remote.name)?.firstUrl?.let { url ->
        append(" ($url)", GRAYED_ATTRIBUTES)
      }
    }
  }

  private fun renderMissing(value: String) {
    val path = VfsUtilCore.urlToPath(value)
    append(path, ERROR_ATTRIBUTES)
  }
}
