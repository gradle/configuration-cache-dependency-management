# configuration-cache-dependency-management

Samples to demonstrate improvements to dependency management support with configuration cache

You will need the `gpg` command installed to sign the published artifacts.

## Signing plugin

The signing plugin is now configuration cache compatible.

To try this out, use the `publish` task from the [maven-lib](maven-lib/build.gradle.kts) project. This will
publish a Jar and metadata to `maven-lib/build/repo/maven` along with signature files.

```shell
> ./gradlew maven-lib:publish

> ./gradlew clean

# Cache hit, publishes the jar and signatures again
> ./gradlew maven-lib:publish
```

Compare this with the behaviour for Gradle 7.6. The cache miss build should fail with serialization problems.

```shell
> alias gradle76=# point this at a Gradle 7.6 installation

# Fails with a serialization failure
> gradle76 maven-lib:publish
```

## Dynamic versions with Ivy and Maven repositories

Dynamic dependencies are now supported for Ivy repositories. This was previously only supported for Maven repositories.

To try this out, use the [`resolve`](dynamic-versions/build.gradle.kts) task. This resolves libraries from
a Maven and an Ivy repository.

```shell
# Need to publish with --no-configuration-cache as Ivy publishing is not yet supported
> ./gradlew publish --no-configuration-cache

# Resolves version 1.0 of the libraries
> ./gradlew resolve

# Cache hit
> ./gradlew resolve

> ./gradle ivy-lib:publish --no-configuration-cache -DlibVersion=2.0

# Cache miss, resolves version 2.0 if the Ivy library
> ./gradlew resolve
```

Compare this with the behavior for Gradle 7.6, where changes to the Ivy repository are ignored:

```shell
> gradle76 resolve
> ./gradle ivy-lib:publish --no-configuration-cache -DlibVersion=3.0

# Incorrect cache hit, resolves incorrect version
> gradle76 resolve
```

## Dependency verification
