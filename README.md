# configuration-cache-dependency-management

Samples to demonstrate improvements to dependency management support with configuration cache

You will need the `gpg` command installed to sign the published artifacts.

## Signing plugin

The signing plugin is now configuration cache compatible.

To try this out, use the `publish` task from the [maven-lib](maven-lib/build.gradle.kts) project.
This will publish a Jar and metadata to `maven-lib/build/repo/maven` along with signature files.

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

To try this out, use the `resolve` task from the [`dynamic-versions`](dynamic-versions/build.gradle.kts) project.
This resolves libraries from a Maven and an Ivy repository.

```shell
> ./gradlew publish

# Resolves version 1.0 of the libraries
> ./gradlew resolve

# Cache hit
> ./gradlew resolve

# Publish version 2.0 of the Ivy library
> ./gradlew ivy-lib:publish -DlibVersion=2.0

# Cache miss, resolves version 2.0 if the Ivy library
> ./gradlew resolve
```

Compare this with the behavior for Gradle 7.6, where changes to the Ivy repository are ignored:

```shell
> gradle76 resolve

# Publish version 3.0 of the Ivy library
> ./gradlew ivy-lib:publish -DlibVersion=3.0

# Incorrect cache hit, resolves incorrect version
> gradle76 resolve
```

## Ivy publishing

The Ivy publishing plugin is now configuration cache compatible.

To try this out, use the `publish` task from the [ivy-lib](ivy-lib/build.gradle.kts) project. This will
publish a Jar and metadata to `ivy-lib/build/repo/ivy`.
This project applies the signing plugin and the signature files are also published

```shell
> ./gradlew ivy-lib:publish

> ./gradlew clean

# Cache hit, publishes the jar and signatures again
> ./gradlew ivy-lib:publish
```

Compare this with the behaviour for Gradle 7.6. The cache miss build should fail with serialization problems.

```shell
# Fails with a serialization failure
> gradle76 ivy-lib:publish
```

## Dependency verification

Dependency verification is now compatible with the configuration cache.

To try this out, use the `resolve` task.

Note that the first build after using the `--write-verification-metadata` option results in a cache miss. This will
be improved later.

```shell
# Reset state
> ./gradlew clean publish

# Generate the verification file (gradle/verification-metadata.xml)
> ./gradlew --write-verification-metadata pgp,sha256 resolve

# Currently this is a cache miss. It should be a cache hit
> ./gradlew resolve

# Cache hit
> ./gradlew resolve

# Change the verification file so that ivy-lib.jar cannot be verified 
> ./gradlew breakVerificationMetadata

# Fails due to failed verification of ivy-lib.jar
> ./gradlew resolve

# Revert the change to verification file
> ./gradlew fixVerificationMetadata

> ./gradlew resolve
> ./gradlew resolve

# Publish new versions, these will not be verified
> ./gradlew publish -DlibVersion=2.0

# Fails due to failed verification
> ./gradlew resolve

# Cleanup
> rm gradle/verification-metadata.xml
```

Compare this with the behaviour for Gradle 7.6. Changes to the verification metadata file are ignored.

```shell
# Reset state
> ./gradlew clean publish
> gradle76 --write-verification-metadata pgp,sha256 resolve
> gradle76 resolve
> gradle76 breakVerificationMetadata
# Should fail but does not as changes to verification metadata file are ignored
> gradle76 resolve
> rm gradle/verification-metadata.xml
```

## Dependency reports

The `dependency` and `dependencyInsight` tasks are configuration cache compatible.

```shell
# Reset state
> ./gradlew clean publish

# `dependencies` report
> ./gradlew dynamic-versions:dependencies --configuration compileClasspath
> ./gradlew dynamic-versions:dependencies --configuration compileClasspath

# Publish new version
> ./gradlew publish -DlibVersion=2.0

# Cache miss (because of new version), reports on new version
> ./gradlew dynamic-versions:dependencies --configuration compileClasspath

# `dependencyInsight` report
> ./gradlew dynamic-versions:dependencyInsight --configuration compileClasspath --dependency maven-lib
> ./gradlew dynamic-versions:dependencyInsight --configuration compileClasspath --dependency maven-lib

# Publish new version
> ./gradlew publish -DlibVersion=3.0

# Cache miss, reports on new versions
> ./gradlew dynamic-versions:dependencyInsight --configuration compileClasspath --dependency maven-lib
```
