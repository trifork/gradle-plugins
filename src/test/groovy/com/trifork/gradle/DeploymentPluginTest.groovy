import org.gradle.api.*
import org.gradle.api.plugins.*
import org.testng.annotations.*

class DeploymentPluginTest {

	Project project

	@BeforeTest
	public void setup() {

		project = ProjectBuilder.builder().build()
		project.apply plugin: 'deployment'
	}
	
	@AfterTest
	public void teardown() {

		project = null
	}

	@Test
	public void pluginAddsReleaseTaskToProject() {

		assertTrue(project.tasks.deployRelease != null)
	}
	
	
	@Test
	public void pluginAddsSnapshotTaskToProject() {

		assertTrue(project.tasks.deploySnapshot != null)
	}
}
