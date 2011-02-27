Trifork Gradle Plugins
======================

This collection of plugins in meant to make working with
[Gradle](http://gradle.org/) a breeze for Trifork projects.

Every time you have to customize your Gradle build, e.g. to work with a CI 
plugin or whatever, it is very likely that your effort could be turned into a
plugin.

Also you should check out the Trifork Gradle Project template for a good
starting point for your projects.

Any submissions are welcome. Please make a pull request and share your work.

Deployment Plugin
-----------------

    apply plugin: 'deployment'

It is important to have a well-defined and consistent way of sharing jar files
and other artifacts. Maven repos are the de-facto standard for artifact sharing
and by using it you get a high degree of interoperability between build
systems. You can apply this plugin to the subprojects your actually want to
deploy to nexus.

When using the deployment-plugin it is important to manage your build version.
The property version should always update it when you have made a release to
the repository. It is defined in the 'gradle.properties' file.

You will only be able to release an artifact of a given version once. If you
try to redeploy a release artifact, the deployment will fail (as it should). If
you have made a mistake in a release, the only thing you can do is to confess
you messed up and make a new release with a version bump.

When you apply the deployment-plugin you get a few extra tasks to help you
share your artifacts.

Deploy a release artifact:

    gradle deployRelease

While releases are fine when you are actually finished
with an iteration or some other milestone, it is not always convenient to use
release artifacts during active development. Therefore when you want to share
your diamonds in the rough, you can use Snapshot versions. Snapshots of a given
version can be deployed with any number of times. For people familiar with Ivy,
snapshots can be used as a 'latest integration' dependencies.

Deploy a snapshot artifact:

    gradle deploySnapshot

When making a release it is important to make sure that
you don't depend on any snapshot artifacts. Since snapshots change over time,
future snapshot versions will potentially break your release (which is bad).

So make sure you have no '-SNAPSHOT' dependencies when you call deployRelease
or at the very least, as few of them as possible.
