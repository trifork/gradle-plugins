package com.trifork.gradle

import org.gradle.api.*
import org.gradle.api.plugins.*
import org.gradle.api.tasks.bundling.*

class DeploymentPlugin implements Plugin<Project> {

	def void apply(Project p) {

		def convention = new DeploymentPluginConvention()
		p.convention.plugins.deployment = convention

		if (!p.plugins.hasPlugin('maven')) {
			p.apply plugin: 'maven'
		}

		p.task([dependsOn: 'uploadArchives'], 'deploySnapshot')
		p.task([dependsOn: 'uploadArchives'], 'deployRelease')

		// In order to deploy the artifacts we need some dependencies.
		// The actually jars needed depend on how we are going to deploy.
		// By default we just use HTTP to deploy.

		p.configurations {
			deployerJars
		}

		p.dependencies {
			deployerJars "org.apache.maven.wagon:wagon-http:1.0-beta-2"
		}

		// Make a jar containing the sources.

		p.task([type: Jar, dependsOn: p.compileJava], 'sourcesJar') {
			from p.sourceSets.main.allSource
			classifier = 'sources'
		}

		p.artifacts {
			archives p.tasks.sourcesJar
		}

		p.uploadArchives.dependsOn p.tasks.sourcesJar

		p.gradle.taskGraph.whenReady { graph ->

			// Determine if this is a release or a snapshot

			if (!graph.hasTask(p.tasks.deployRelease)) p.version += '-SNAPSHOT'

			p.uploadArchives {
				repositories.mavenDeployer {
					configuration = p.configurations.deployerJars
					repository(id: 'trifork-releases', url: 'http://nexus.ci81.trifork.com/content/repositories/releases/')
					snapshotRepository(id: 'trifork-snapshots', url: 'http://nexus.ci81.trifork.com/content/repositories/snapshots/')
				}
			}
		}
	}
}

class DeploymentPluginConvention {
	String nextReleaseVersion

	def deployment(Closure closure) {
		closure.delegate = this
		closure()
	}
}
